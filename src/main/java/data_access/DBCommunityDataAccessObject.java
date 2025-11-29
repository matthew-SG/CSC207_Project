package data_access;

import entities.Rating;
import entities.Recipe;
import entities.User;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.community.CommunityDataAccessInterface;
import use_case.community.input_data.CommunityPublishInputData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    HashMap<Integer, Recipe> likedRecipes = new HashMap<>(); // TODO: replace the dummy with actual accumulated data
    ArrayList<Rating> ratings = new ArrayList<>();
    
    // Authentication credentials
    private String idToken;
    private String refreshToken;
    private int expiresIn;

    public DBCommunityDataAccessObject() {
        likedRecipes.put(1, new Recipe(1, "pizza",
                "https://www.tasteofhome.com/wp-content/uploads/2018/01/Homemade-Pizza_EXPS_FT23_376_EC_120123_3.jpg",
                new ArrayList<>(),"american", new HashMap<>()));
        likedRecipes.put(2, new Recipe(2, "hamburger",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4d/Cheeseburger.jpg/2560px-Cheeseburger.jpg",
                new ArrayList<>(), "american", new HashMap<>()));
        ratings.add(new Rating(1, 1, "test@1.ca", 4, "yummy", "hamburger", "https://en.wikipedia.org/wiki/Cheeseburger#/media/File:Cheeseburger.jpg"));
        ratings.add(new Rating(2, 2, "test@1.ca", 4, "good","pizza", "https://www.tasteofhome.com/wp-content/uploads/2018/01/Homemade-Pizza_EXPS_FT23_376_EC_120123_3.jpg"));


        this.webapiKey = Constants.WEBAPI_KEY;
        this.ratingsEndpoint = BASE_URL + "/ratings";
        this.client = new OkHttpClient();
        
        // Authenticate anonymously on construction
        authenticateAnonymously();
    }

    @Override
    public List<Recipe> getLikedRecipes(User user) {
        return new ArrayList<>(likedRecipes.values());
    }

    @Override
    public Recipe getSelectedRecipe(int recipeID) {
        return likedRecipes.get(recipeID);
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
            
            // Create Firestore document structure
            JSONObject fields = new JSONObject();
            fields.put("stars", createIntegerValue(data.getRating()));
            fields.put("comment", createStringValue(data.getComment()));
            fields.put("userName", createStringValue(data.getUserName()));
            fields.put("recipeName", createStringValue(data.getRecipeName()));
            fields.put("recipeId", createIntegerValue(data.getRecipeID()));
            fields.put("recipeImageUrl", createStringValue(data.getRecipeImageURL()));
            
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
            
            return new Rating(ratingId, recipeId, userName, stars, comment, recipeName, recipeImageUrl);
        } catch (Exception e) {
            System.err.println("Error parsing rating document: " + e.getMessage());
            return null;
        }
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


    public static void main(String[] args) {
        DBCommunityDataAccessObject db = new DBCommunityDataAccessObject();
        List<Rating> ratings = db.getCurrentRatings();

        CommunityPublishInputData data = new CommunityPublishInputData("mockname", 4, 4, "not too bad",
                "nice", "https://media.istockphoto.com/id/1457433817/photo/group-of-healthy-food-for-flexitarian-diet.jpg?s=612x612&w=0&k=20&c=v48RE0ZNWpMZOlSp13KdF1yFDmidorO2pZTu2Idmd3M=");
        db.publishReview(data);
    }
}
