package data_access;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import entities.Ingredient;
import entities.Rating;
import entities.Recipe;
import entities.User;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.community.CommunityDataAccessInterface;
import use_case.community.CommunityUserRecipeDataAccessInterface;
import use_case.community.input_data.CommunityPublishInputData;



/**
 * Data Access Object for Community features using Firestore REST API.
 * Handles anonymous authentication automatically for accessing Firestore.
 * Reference: https://firebase.google.com/docs/firestore/use-rest-api
 */
public class DBCommunityDataAccessObject implements CommunityDataAccessInterface {
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final String PROJECT_ID = "test-backend-89c2d";
    private static final String BASE_URL = "https://firestore.googleapis.com/v1/projects/" + PROJECT_ID + "/databases/(default)/documents";
    private static final String ANONYMOUS_SIGNIN_ENDPOINT = "https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=";
    
    private final String webapiKey;
    private final String ratingsEndpoint;
    private final OkHttpClient client;
    private final CommunityUserRecipeDataAccessInterface userRecipeDataAccess;
    
    // Authentication credentials
    private String idToken;
    private String refreshToken;
    private int expiresIn;

    public DBCommunityDataAccessObject(CommunityUserRecipeDataAccessInterface userRecipeDataAccess) {
        this.userRecipeDataAccess = Objects.requireNonNull(userRecipeDataAccess, "userRecipeDataAccess cannot be null");
        this.webapiKey = Constants.WEBAPI_KEY;
        this.ratingsEndpoint = BASE_URL + "/ratings";
        this.client = new OkHttpClient();
        
        // Authenticate anonymously on construction
        authenticateAnonymously();
    }

    @Override
    public List<Recipe> getLikedRecipes(String username) {
        if (username == null) {
            return Collections.emptyList();
        }

        List<Recipe> liked = userRecipeDataAccess.getLikedRecipesForUser(username);
        return liked == null ? Collections.emptyList() : liked;
    }

    @Override
    public Recipe getSelectedRecipe(int recipeID) {
        return userRecipeDataAccess.getCurrentUserLikedRecipe(recipeID).orElse(null);
    }


    /**
     * Fetches all ratings from Firestore.
     * 
     * @return a list of all Rating objects from the database
     */
    @Override
    public List<Rating> getCurrentRatings() {
        List<Rating> ratings = new ArrayList<>();
        
        try {
            // Build the request to get all ratings with authentication
            Request.Builder requestBuilder = new Request.Builder()
                    .url(ratingsEndpoint + "?key=" + webapiKey)
                    .get();
            
            // Add authorization header if we have an ID token
            if (idToken != null) {
                requestBuilder.addHeader("Authorization", "Bearer " + idToken);
            }
            
            Request request = requestBuilder.build();

            // Execute the request
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    JSONObject jsonResponse = new JSONObject(responseBody);
                    
                    // Parse the documents array
                    if (jsonResponse.has("documents")) {
                        JSONArray documents = jsonResponse.getJSONArray("documents");
                        
                        for (int i = 0; i < documents.length(); i++) {
                            JSONObject doc = documents.getJSONObject(i);
                            Rating rating = parseRatingFromDocument(doc);
                            if (rating != null) {
                                ratings.add(rating);
                            }
                        }
                    }
                } else {
                    System.err.println("Failed to fetch ratings. Response code: " + response.code());
                }
            }
        } catch (IOException e) {
            System.err.println("Error fetching ratings: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected error fetching ratings: " + e.getMessage());
            e.printStackTrace();
        }
        
        return ratings;
    }

    /**
     * Publishes a new review to Firestore with an auto-incremented ID.
     * 
     * @param data the review data to publish
     * @return updated list of all ratings after publishing
     */
    @Override
    public List<Rating> publishReview(CommunityPublishInputData data) {
        try {
            // Get the next available rating ID
            int nextRatingId = getNextRatingId();

            // Resolve recipe details with fallbacks
            Recipe recipeDetails = resolveRecipeDetails(data);
            String resolvedRecipeName = coalesce(
                recipeDetails.getRecipeName(),
                data.getRecipeName(),
                "Unnamed Recipe"
            );
            String resolvedRecipeImage = coalesce(
                recipeDetails.getRecipeImage(),
                data.getRecipeImageURL(),
                ""
            );
            int resolvedRecipeId = recipeDetails.getRecipeId() != 0 ? recipeDetails.getRecipeId() : data.getRecipeID();
            String resolvedUserName = coalesce(data.getUserName(), userRecipeDataAccess.getCurrentUsername(), "Anonymous");

            // Create Firestore document structure
            JSONObject fields = new JSONObject();
            fields.put("stars", createIntegerValue(sanitizeRating(data.getRating())));
            fields.put("comment", createStringValue(coalesce(data.getComment(), "")));
            fields.put("userName", createStringValue(resolvedUserName));
            fields.put("recipeName", createStringValue(resolvedRecipeName));
            fields.put("recipeId", createIntegerValue(resolvedRecipeId));
            fields.put("recipeImageUrl", createStringValue(resolvedRecipeImage));
            fields.put("recipeDetails", buildRecipeDetailsValue(recipeDetails));
            
            JSONObject document = new JSONObject();
            document.put("fields", fields);
            
            // Build the request to create a new rating document with specific ID and authentication
            RequestBody body = RequestBody.create(document.toString(), JSON);
            Request.Builder requestBuilder = new Request.Builder()
                    .url(ratingsEndpoint + "/" + nextRatingId + "?key=" + webapiKey)
                    .patch(body);
            
            // Add authorization header if we have an ID token
            if (idToken != null) {
                requestBuilder.addHeader("Authorization", "Bearer " + idToken);
            }
            
            Request request = requestBuilder.build();

            // Execute the request
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    System.out.println("Review published successfully with ID: " + nextRatingId);
                } else if (response.body() != null) {
                    String errorBody = response.body().string();
                    System.err.println("Failed to publish review. Response: " + errorBody);
                }
            }
        } catch (IOException e) {
            System.err.println("Error publishing review: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected error publishing review: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Return updated list of all ratings
        return getCurrentRatings();
    }

    private Recipe resolveRecipeDetails(CommunityPublishInputData data) {
    return userRecipeDataAccess.getCurrentUserLikedRecipe(data.getRecipeID())
                .orElseGet(() -> {
                    System.err.println("[CommunityDAO] Unable to find recipe " + data.getRecipeID() + " locally. Using fallback payload.");
                    return buildFallbackRecipe(data);
                });
    }

    private Recipe buildFallbackRecipe(CommunityPublishInputData data) {
        int recipeId = data != null ? data.getRecipeID() : 0;
        String recipeName = data != null ? coalesce(data.getRecipeName(), "Unnamed Recipe") : "Unnamed Recipe";
        String recipeImage = data != null ? coalesce(data.getRecipeImageURL(), "") : "";
        Recipe fallback = new Recipe(
                recipeId,
                recipeName,
                recipeImage,
                new ArrayList<>(),
                "",
                new HashMap<>()
        );
        fallback.setSteps("");
        return fallback;
    }

    private String coalesce(String... values) {
        if (values == null) {
            return "";
        }
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return "";
    }

    private int sanitizeRating(int rating) {
        if (rating < 1) {
            return 1;
        }
        if (rating > 5) {
            return 5;
        }
        return rating;
    }

    private JSONObject buildRecipeDetailsValue(Recipe recipe) {
        Recipe safeRecipe = recipe != null ? recipe : buildFallbackRecipe(null);
        JSONObject recipeFields = new JSONObject();
        recipeFields.put("recipeId", createIntegerValue(safeRecipe.getRecipeId()));
        recipeFields.put("recipeName", createStringValue(coalesce(safeRecipe.getRecipeName(), "Unnamed Recipe")));
        recipeFields.put("recipeImage", createStringValue(coalesce(safeRecipe.getRecipeImage(), "")));
        recipeFields.put("mealType", createStringValue(coalesce(safeRecipe.getMealType(), "")));
        recipeFields.put("steps", createStringValue(coalesce(safeRecipe.getSteps(), "")));
        recipeFields.put("ingredients", createArrayValue(buildIngredientArray(safeRecipe)));
        recipeFields.put("nutritionalValues", createMapValue(buildNutritionFields(safeRecipe)));
        return createMapValue(recipeFields);
    }

    private JSONArray buildIngredientArray(Recipe recipe) {
        JSONArray ingredientValues = new JSONArray();
        List<Ingredient> ingredients = recipe.getIngredients();
        if (ingredients == null) {
            return ingredientValues;
        }
        for (Ingredient ingredient : ingredients) {
            if (ingredient == null) {
                continue;
            }
            JSONObject ingredientFields = new JSONObject();
            ingredientFields.put("name", createStringValue(coalesce(ingredient.getName(), "")));
            ingredientFields.put("quantity", createDoubleValue(ingredient.getQuantity()));
            ingredientFields.put("unit", createStringValue(coalesce(ingredient.getUnit(), "")));
            ingredientValues.put(createMapValue(ingredientFields));
        }
        return ingredientValues;
    }

    private JSONObject buildNutritionFields(Recipe recipe) {
        JSONObject nutritionFields = new JSONObject();
        Map<String, Double> nutrition = recipe.getNutritionalValues();
        if (nutrition == null) {
            return nutritionFields;
        }
        for (Map.Entry<String, Double> entry : nutrition.entrySet()) {
            String key = entry.getKey();
            if (key == null || key.isBlank()) {
                continue;
            }
            Double value = entry.getValue();
            double safeValue = value != null && Double.isFinite(value) ? value : 0d;
            nutritionFields.put(key, createDoubleValue(safeValue));
        }
        return nutritionFields;
    }

    private JSONObject createArrayValue(JSONArray values) {
        JSONObject arrayValue = new JSONObject();
        arrayValue.put("values", values != null ? values : new JSONArray());
        JSONObject wrapper = new JSONObject();
        wrapper.put("arrayValue", arrayValue);
        return wrapper;
    }

    private JSONObject createMapValue(JSONObject fields) {
        JSONObject mapValue = new JSONObject();
        mapValue.put("fields", fields != null ? fields : new JSONObject());
        JSONObject wrapper = new JSONObject();
        wrapper.put("mapValue", mapValue);
        return wrapper;
    }

    private JSONObject createDoubleValue(double value) {
        double safeValue = Double.isFinite(value) ? value : 0d;
        JSONObject doubleValue = new JSONObject();
        doubleValue.put("doubleValue", safeValue);
        return doubleValue;
    }

    /**
     * Gets the next available rating ID by finding the maximum existing ID and adding 1.
     * 
     * @return the next available rating ID
     */
    private int getNextRatingId() {
        List<Rating> currentRatings = getCurrentRatings();
        int maxId = 0;
        
        for (Rating rating : currentRatings) {
            if (rating.getRatingId() > maxId) {
                maxId = rating.getRatingId();
            }
        }
        
        return maxId + 1;
    }

    /**
     * Parses a Rating object from a Firestore document JSON object.
     * 
     * @param doc the Firestore document JSON
     * @return a Rating object, or null if parsing fails
     */
    private Rating parseRatingFromDocument(JSONObject doc) {
        try {
            // Extract rating ID from document name (e.g., ".../ratings/1" -> 1)
            String name = doc.getString("name");
            int ratingId = Integer.parseInt(name.substring(name.lastIndexOf('/') + 1));
            
            JSONObject fields = doc.getJSONObject("fields");
            
            // Parse fields with proper type handling
            int recipeId = parseIntegerField(fields, "recipeId");
            String userName = parseStringField(fields, "userName");
            int stars = parseIntegerField(fields, "stars");
            String comment = parseStringField(fields, "comment");
            String recipeName = parseStringField(fields, "recipeName");
            String recipeImageUrl = parseStringField(fields, "recipeImageUrl");

            Rating rating = new Rating(ratingId, recipeId, userName, stars, comment, recipeName, recipeImageUrl);
            Recipe detailedRecipe = parseDetailedRecipe(fields);
            if (detailedRecipe != null) {
                rating.setDetailedRecipe(detailedRecipe);
            }
            return rating;
        } catch (Exception e) {
            System.err.println("Error parsing rating document: " + e.getMessage());
            return null;
        }
    }

    private Recipe parseDetailedRecipe(JSONObject fields) {
        JSONObject recipeDetailsField = fields.optJSONObject("recipeDetails");
        JSONObject recipeDetailsMap = extractMapFields(recipeDetailsField);
        if (recipeDetailsMap == null) {
            return null;
        }

        int recipeId = parseIntegerField(recipeDetailsMap, "recipeId");
        String recipeName = coalesce(parseStringField(recipeDetailsMap, "recipeName"), "Unnamed Recipe");
        String recipeImage = coalesce(parseStringField(recipeDetailsMap, "recipeImage"), "");
        String mealType = parseStringField(recipeDetailsMap, "mealType");
        String steps = parseStringField(recipeDetailsMap, "steps");

        List<Ingredient> ingredients = parseIngredientList(recipeDetailsMap);
        Map<String, Double> nutrition = parseNutritionMap(recipeDetailsMap);

        Recipe recipe = new Recipe(recipeId, recipeName, recipeImage, ingredients, mealType, nutrition);
        recipe.setSteps(steps);
        return recipe;
    }

    private JSONObject extractMapFields(JSONObject fieldWrapper) {
        if (fieldWrapper == null || !fieldWrapper.has("mapValue")) {
            return null;
        }
        JSONObject mapValue = fieldWrapper.optJSONObject("mapValue");
        if (mapValue == null) {
            return null;
        }
        return mapValue.optJSONObject("fields");
    }

    private List<Ingredient> parseIngredientList(JSONObject recipeDetailsMap) {
        List<Ingredient> ingredients = new ArrayList<>();
        if (recipeDetailsMap == null) {
            return ingredients;
        }

        JSONObject ingredientsField = recipeDetailsMap.optJSONObject("ingredients");
        if (ingredientsField == null) {
            return ingredients;
        }

        JSONObject arrayValue = ingredientsField.optJSONObject("arrayValue");
        if (arrayValue == null) {
            return ingredients;
        }

        JSONArray values = arrayValue.optJSONArray("values");
        if (values == null) {
            return ingredients;
        }

        for (int i = 0; i < values.length(); i++) {
            JSONObject value = values.optJSONObject(i);
            JSONObject ingredientFields = extractMapFields(value);
            if (ingredientFields == null) {
                continue;
            }

            String name = parseStringField(ingredientFields, "name");
            double quantity = parseDoubleField(ingredientFields, "quantity");
            String unit = parseStringField(ingredientFields, "unit");

            ingredients.add(new Ingredient(name, quantity, unit));
        }

        return ingredients;
    }

    private Map<String, Double> parseNutritionMap(JSONObject recipeDetailsMap) {
        Map<String, Double> nutrition = new HashMap<>();
        if (recipeDetailsMap == null) {
            return nutrition;
        }

        JSONObject nutritionField = recipeDetailsMap.optJSONObject("nutritionalValues");
        JSONObject nutritionFields = extractMapFields(nutritionField);
        if (nutritionFields == null) {
            return nutrition;
        }

        for (String key : nutritionFields.keySet()) {
            JSONObject valueWrapper = nutritionFields.optJSONObject(key);
            double value = parseDoubleValueNode(valueWrapper);
            nutrition.put(key, value);
        }

        return nutrition;
    }

    /**
     * Helper method to parse integer fields from Firestore document.
     */
    private int parseIntegerField(JSONObject fields, String fieldName) {
        if (fields.has(fieldName)) {
            JSONObject field = fields.getJSONObject(fieldName);
            if (field.has("integerValue")) {
                return Integer.parseInt(field.getString("integerValue"));
            } else if (field.has("stringValue")) {
                // Handle case where integer is stored as string
                return Integer.parseInt(field.getString("stringValue"));
            }
        }
        return 0;
    }

    /**
     * Helper method to parse string fields from Firestore document.
     */
    private String parseStringField(JSONObject fields, String fieldName) {
        if (fields.has(fieldName)) {
            JSONObject field = fields.getJSONObject(fieldName);
            if (field.has("stringValue")) {
                return field.getString("stringValue");
            }
        }
        return "";
    }

    private double parseDoubleField(JSONObject fields, String fieldName) {
        if (fields == null || !fields.has(fieldName)) {
            return 0d;
        }
        JSONObject field = fields.optJSONObject(fieldName);
        return parseDoubleValueNode(field);
    }

    private double parseDoubleValueNode(JSONObject field) {
        if (field == null) {
            return 0d;
        }
        try {
            if (field.has("doubleValue")) {
                return field.getDouble("doubleValue");
            }
            if (field.has("integerValue")) {
                return Double.parseDouble(field.getString("integerValue"));
            }
            if (field.has("stringValue")) {
                return Double.parseDouble(field.getString("stringValue"));
            }
        } catch (NumberFormatException ignore) {
            return 0d;
        }
        return 0d;
    }

    /**
     * Creates a Firestore integer value object.
     */
    private JSONObject createIntegerValue(int value) {
        JSONObject intValue = new JSONObject();
        intValue.put("integerValue", String.valueOf(value));
        return intValue;
    }

    /**
     * Creates a Firestore string value object.
     */
    private JSONObject createStringValue(String value) {
        JSONObject strValue = new JSONObject();
        strValue.put("stringValue", value);
        return strValue;
    }

    /**
     * Authenticates anonymously with Firebase Authentication to get auth tokens.
     * This is required for accessing Firestore with anonymous access enabled.
     */
    private void authenticateAnonymously() {
        try {
            // Create JSON request body for anonymous sign-in
            JSONObject requestBody = new JSONObject();
            requestBody.put("returnSecureToken", true);

            // Build the request
            RequestBody body = RequestBody.create(requestBody.toString(), JSON);
            Request request = new Request.Builder()
                    .url(ANONYMOUS_SIGNIN_ENDPOINT + webapiKey)
                    .post(body)
                    .build();

            // Execute the request
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    JSONObject jsonResponse = new JSONObject(responseBody);
                    
                    this.idToken = jsonResponse.getString("idToken");
                    this.refreshToken = jsonResponse.getString("refreshToken");
                    this.expiresIn = Integer.parseInt(jsonResponse.getString("expiresIn"));
                    
                    System.out.println("DBCommunityDAO: Anonymous authentication successful");
                } else {
                    System.err.println("DBCommunityDAO: Anonymous authentication failed. Response code: " + response.code());
                }
            }
        } catch (IOException e) {
            System.err.println("DBCommunityDAO: Error during anonymous authentication: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("DBCommunityDAO: Unexpected error during anonymous authentication: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gets the current ID token for Firebase authentication.
     * 
     * @return the current ID token, or null if not authenticated
     */
    public String getIdToken() {
        return idToken;
    }

    /**
     * Refreshes the authentication token if it's expired or about to expire.
     * Can be called before making requests if needed.
     */
    public void refreshAuthToken() {
        authenticateAnonymously();
    }

}
