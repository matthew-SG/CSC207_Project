package data_access;

import entities.Cuisine;
import entities.DietaryRestriction;
import entities.Intolerance;
import entities.Recipe;
import entities.Ingredient;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.recipe_generator.RecipeDataAccessInterface;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;


public class RecipeDataAccessObject implements RecipeDataAccessInterface {
    private static final String API_KEY = "7265c428408440ef96740ae1a4040acd";
    private static final String API_BASE_URL = "https://api.spoonacular.com/recipes/complexSearch";

    @Override
    public List<Recipe> getRecipes(DietaryRestriction dietaryRestriction,
                                   List<Intolerance> intolerances,
                                   Cuisine cuisine,
                                   Integer minCalories,
                                   Integer maxCalories,
                                   Integer minProtein,
                                   Integer maxProtein) {

        // first we have to build the api url with all the filters
        String apiUrl = buildApiUrl(dietaryRestriction, intolerances, cuisine,
                minCalories, maxCalories, minProtein, maxProtein);

        // this block is to log all the details in console it can be removed if needed
        System.out.println("[RECIPE-GEN DAO] diet = " + dietaryRestriction);
        System.out.println("[RECIPE-GEN DAO] intolerances = " + intolerances);
        System.out.println("[RECIPE-GEN DAO] cuisine = " + cuisine);
        System.out.println("[RECIPE-GEN DAO] Spoonacular request URL: " + apiUrl);


        // get the recipes from API
        String jsonResponse = callSpoonacular(apiUrl);

        if (jsonResponse == null) {
            System.err.println("[RECIPE-GEN DAO] API call failed response was empty.");
            throw new RuntimeException("Recipe API call failed");
        }

// Parse JSON into Recipe objects
        List<Recipe> recipes = parseRecipesFromJson(jsonResponse);

// Filter by calorie and protein min / max
        return filterRecipesByNutrition(recipes, minCalories, maxCalories, minProtein, maxProtein);

    }

    /**
     * Builds the complete Spoonacular API URL with all query parameters
     */
    private String buildApiUrl(DietaryRestriction dietaryRestriction, List<Intolerance> intolerances,
                               Cuisine cuisine, Integer minCalories, Integer maxCalories,
                               Integer minProtein, Integer maxProtein) {

        StringBuilder url = new StringBuilder(API_BASE_URL);
        url.append("?apiKey=").append(API_KEY);
        url.append("&number=10");
        url.append("&addRecipeNutrition=true");
        url.append("&addRecipeInformation=true");
        url.append("&instructionsRequired=true");

        // Add dietary filters
        String dietParam = mapDiet(dietaryRestriction);
        if (!dietParam.isEmpty()) {
            url.append("&diet=").append(dietParam);
        }

        String intolerancesParam = mapIntolerances(intolerances);
        if (!intolerancesParam.isEmpty()) {
            url.append("&intolerances=").append(intolerancesParam);
        }

        String cuisineParam = mapCuisine(cuisine);
        if (!cuisineParam.isEmpty()) {
            url.append("&cuisine=").append(cuisineParam);
        }

        // Add nutrition bounds for protein and calories
        if (minCalories != null) url.append("&minCalories=").append(minCalories);
        if (maxCalories != null) url.append("&maxCalories=").append(maxCalories);
        if (minProtein != null) url.append("&minProtein=").append(minProtein);
        if (maxProtein != null) url.append("&maxProtein=").append(maxProtein);

        return url.toString();
    }

    /**
     * Makes HTTP request to Spoonacular API.
     * Returns the JSON body on success, or null if the call failed.
     */
    private String callSpoonacular(String urlString) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int status = connection.getResponseCode();
            System.out.println("[RECIPE-GEN DAO] HTTP status: " + status);

            // Treat any non-2xx status as a failure: return null so upper layers
            // can decide how to notify the user.
            if (status < 200 || status >= 300) {
                System.err.println("[RECIPE-GEN DAO] Spoonacular call failed with status " + status);
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            return response.toString();

        } catch (Exception e) {
            System.err.println("[RECIPE-GEN DAO] Error calling Spoonacular API: " + e.getMessage());
            return null;

        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception ignored) {}
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * Converts the JSON response into Recipe objects
     */
    private List<Recipe> parseRecipesFromJson(String json) {
        List<Recipe> recipes = new ArrayList<>();

        try {
            JSONArray results = new JSONObject(json).getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                Recipe recipe = parseRecipeFromJson(results.getJSONObject(i));
                if (recipe != null) recipes.add(recipe);
            }
        } catch (Exception e) {
            System.err.println("Error parsing JSON: " + e.getMessage());
        }

        return recipes;
    }

    /**
     * Parses a single recipe object from JSON
     */
    private Recipe parseRecipeFromJson(JSONObject json) {
        try {
            int id = json.getInt("id");
            String title = json.getString("title");
            String image = json.optString("image", "");

            List<Ingredient> ingredients = new ArrayList<>();
            Map<String, Double> nutritionalValues = new HashMap<>();

            Recipe recipe = new Recipe(
                    id,
                    title,
                    image,
                    ingredients,
                    "UNKNOWN",
                    nutritionalValues
            );

            String instructions = json.optString("instructions", "");
            if (!instructions.isBlank()) {
                recipe.setSteps(instructions);
            }

            // Extract calories and protein
            if (json.has("nutrition")) {
                JSONArray nutrients = json.getJSONObject("nutrition").getJSONArray("nutrients");
                for (int i = 0; i < nutrients.length(); i++) {
                    JSONObject nutrient = nutrients.getJSONObject(i);
                    String name = nutrient.getString("name");
                    double amount = nutrient.getDouble("amount");

                    if (name.equalsIgnoreCase("Calories")) {
                        recipe.getNutritionalValues().put("calories", amount);
                    } else if (name.equalsIgnoreCase("Protein")) {
                        recipe.getNutritionalValues().put("protein", amount);
                    }
                }
            }

            return recipe;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Filters recipes by calorie and protein bounds
     */
    private List<Recipe> filterRecipesByNutrition(List<Recipe> recipes,
                                                  Integer minCalories, Integer maxCalories,
                                                  Integer minProtein, Integer maxProtein) {
        List<Recipe> filtered = new ArrayList<>();

        for (Recipe recipe : recipes) {
            if (meetsNutritionRequirements(recipe, minCalories, maxCalories, minProtein, maxProtein)) {
                filtered.add(recipe);
            }
        }

        return filtered;
    }

    /**
     * Checks if a recipe meets all nutrition requirements
     */
    private boolean meetsNutritionRequirements(Recipe recipe,
                                               Integer minCalories, Integer maxCalories,
                                               Integer minProtein, Integer maxProtein) {
        Double calories = recipe.getNutritionalValues().get("calories");
        Double protein = recipe.getNutritionalValues().get("protein");

        if (minCalories != null && calories != null && calories < minCalories) return false;
        if (maxCalories != null && calories != null && calories > maxCalories) return false;
        if (minProtein != null && protein != null && protein < minProtein) return false;
        if (maxProtein != null && protein != null && protein > maxProtein) return false;

        return true;
    }

    private String mapDiet(DietaryRestriction dietaryRestriction) {
        if (dietaryRestriction == null || dietaryRestriction == DietaryRestriction.NONE) return "";

        return switch (dietaryRestriction) {
            case VEGAN -> "vegan";
            case VEGETARIAN -> "vegetarian";
            case KOSHER -> "kosher";
            case HALAL -> "halal";
            case PESCATARIAN -> "pescatarian";
            default -> "";
        };
    }

    private String mapCuisine(Cuisine cuisine) {
        if (cuisine == null || cuisine == Cuisine.ANY) return "";

        return switch (cuisine) {
            case MEXICAN -> "mexican";
            case JAPANESE -> "japanese";
            case INDIAN -> "indian";
            case CHINESE -> "chinese";
            default -> "";
        };
    }

    private String mapIntolerances(List<Intolerance> intolerances) {
        if (intolerances == null || intolerances.isEmpty()) return "";

        List<String> tokens = new ArrayList<>();

        for (Intolerance intolerance : intolerances) {
            if (intolerance == null || intolerance == Intolerance.NONE) continue;

            String token = switch (intolerance) {
                case DAIRY -> "dairy";
                case NUTS -> "tree_nut";
                case SHELLFISH -> "shellfish";
                case GLUTEN -> "gluten";
                case SOY -> "soy";
                case SEAFOOD -> "seafood";
                case SESAME -> "sesame";
                default -> null;
            };

            if (token != null) tokens.add(token);
        }

        return String.join(",", tokens);
    }
}