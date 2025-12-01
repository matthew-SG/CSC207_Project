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


// this class depends on a few things including Recipe entity cuisine Enum etc., it uses the org.json lib for parsing

public class RecipeDataAccessObject implements RecipeDataAccessInterface {
    private static final String API_KEY = "5b07df6820b74cf1b2eae9c1b440f014";
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

        // this block is to log all the details in console its for testing purposes
        System.out.println("[RECIPE-GEN DAO] diet = " + dietaryRestriction);
        System.out.println("[RECIPE-GEN DAO] intolerances = " + intolerances);
        System.out.println("[RECIPE-GEN DAO] cuisine = " + cuisine);
        System.out.println("[RECIPE-GEN DAO] Spoonacular request URL: " + apiUrl);


        // get the recipes from API (we are actually calling the api)
        String jsonResponse = callSpoonacular(apiUrl);

        // then Parse JSON into Recipe objects
        List<Recipe> recipes = null;
        if (jsonResponse != null) {
            recipes = parseRecipesFromJson(jsonResponse);
        }

        if (recipes == null || recipes.isEmpty()) {
            System.out.println("[RECIPE-GEN DAO] No recipes found from API or parsing error. Using default fallback.");
            recipes = getDefaultRecipes();
        }

// Filter by calorie and protein min / max
        return filterRecipesByNutrition(recipes, minCalories, maxCalories, minProtein, maxProtein);

    }


     // this method is responsible for building the api url with all query parameters

    private String buildApiUrl(DietaryRestriction dietaryRestriction, List<Intolerance> intolerances,
                               Cuisine cuisine, Integer minCalories, Integer maxCalories,
                               Integer minProtein, Integer maxProtein) {

        StringBuilder url = new StringBuilder(API_BASE_URL);
        url.append("?apiKey=").append(API_KEY); // translates the enums and lists into the query string format Spoonacular requires
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

    // this method is responsible for making the HTTP request (get request) to the API
    // Returns the JSON body on success, or null if the call failed.
    private String callSpoonacular(String urlString) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            // these time out bounds are here to prevent the UI from hanging when the API is un-reachable or slow after 5 seconds its treated as a failure
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int status = connection.getResponseCode();
            System.out.println("[RECIPE-GEN DAO] HTTP status: " + status);

            // Treat any non-2xx status as a failure (rate limit, server error etc.): return null so upper layers
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

    // this method is responsible for processing the Json returned by the API call and extracting the specific data we need
    // to create a list of Recipe objects
    private List<Recipe> parseRecipesFromJson(String json) {
        List<Recipe> recipes = new ArrayList<>();

        try {
            JSONArray results = new JSONObject(json).getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                Recipe recipe = parseRecipeFromJson(results.getJSONObject(i)); // parseRecipeFromJson is called to handle whether a single recipe fails
                if (recipe != null) recipes.add(recipe);
            } // if the entire list fails (all recipes fail) then robust error handling happens (user is given error message)
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
     * Returns default dummy recipe (fallback if API fails)
     */
    private List<Recipe> getDefaultRecipes() {
        List<Recipe> recipes = new ArrayList<>();

        Recipe veggieTacos = new Recipe(1, "Veggie Tacos", "https://example.com/tacos.jpg", "DINNER");
        veggieTacos.getIngredients().add(new Ingredient("Tortilla", 2, "pieces"));
        veggieTacos.getIngredients().add(new Ingredient("Black beans", 100, "g"));
        veggieTacos.getIngredients().add(new Ingredient("Cheddar cheese", 30, "g"));
        veggieTacos.getNutritionalValues().put("calories", 450.0);
        veggieTacos.getNutritionalValues().put("protein", 18.0);

        recipes.add(veggieTacos);
        return recipes;
    }

    // the calories and protein were filtered both locally and externally by the api for testing purposes for when the api fails
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

    // the following mapping methods are responsible for converting the enums I have stored locally to
    // the exact strings spoonacular expects in the URL the empty string handles cases when there's no filter added
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