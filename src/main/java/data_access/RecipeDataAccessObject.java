package data_access;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// this class depends on a few things including Recipe entity cuisine Enum etc., it uses the org.json lib for parsing
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import entities.Cuisine;
import entities.DietaryRestriction;
import entities.Ingredient;
import entities.Intolerance;
import entities.Recipe;
import use_case.recipe_generator.RecipeDataAccessInterface;

/**
 * Data access object for building recipes from API calls.
 */
public class RecipeDataAccessObject implements RecipeDataAccessInterface {
    private static final String API_KEY = "5b07df6820b74cf1b2eae9c1b440f014";
    private static final String API_BASE_URL = "https://api.spoonacular.com/recipes/complexSearch";
    private static final String NUTRITION = "nutrition";

    @Override
    public List<Recipe> getRecipes(DietaryRestriction dietaryRestriction,
                                   List<Intolerance> intolerances,
                                   Cuisine cuisine,
                                   Integer minCalories,
                                   Integer maxCalories,
                                   Integer minProtein,
                                   Integer maxProtein) {

        // first we have to build the api url with all the filters
        final String apiUrl = buildApiUrl(dietaryRestriction, intolerances, cuisine,
                minCalories, maxCalories, minProtein, maxProtein);

        // this block is to log all the details in console its for testing purposes
        System.out.println("[RECIPE-GEN DAO] diet = " + dietaryRestriction);
        System.out.println("[RECIPE-GEN DAO] intolerances = " + intolerances);
        System.out.println("[RECIPE-GEN DAO] cuisine = " + cuisine);
        System.out.println("[RECIPE-GEN DAO] Spoonacular request URL: " + apiUrl);

        // get the recipes from API (we are actually calling the api)
        final String jsonResponse = callSpoonacular(apiUrl);

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

    /**
     * Builds the complete Spoonacular API URL with all query parameters.
     * @param dietaryRestriction the dietary restriction
     * @param intolerances the intolerances
     * @param cuisine the cuisine
     * @param minCalories the minimum calories
     * @param maxCalories the maximum calories
     * @param minProtein the minimum protein
     * @param maxProtein the maximum protein
     * @return the API url
     */
    private String buildApiUrl(DietaryRestriction dietaryRestriction, List<Intolerance> intolerances,
                               Cuisine cuisine, Integer minCalories, Integer maxCalories,
                               Integer minProtein, Integer maxProtein) {

        final StringBuilder url = new StringBuilder(API_BASE_URL);
        // translates the enums and lists into the query string format Spoonacular requires
        url.append("?apiKey=").append(API_KEY);
        url.append("&number=10");
        url.append("&addRecipeNutrition=true");
        url.append("&addRecipeInformation=true");
        url.append("&instructionsRequired=true");

        // Add dietary filters
        final String dietParam = mapDiet(dietaryRestriction);
        if (!dietParam.isEmpty()) {
            url.append("&diet=").append(dietParam);
        }

        final String intolerancesParam = mapIntolerances(intolerances);
        if (!intolerancesParam.isEmpty()) {
            url.append("&intolerances=").append(intolerancesParam);
        }

        final String cuisineParam = mapCuisine(cuisine);
        if (!cuisineParam.isEmpty()) {
            url.append("&cuisine=").append(cuisineParam);
        }

        // Add nutrition bounds for protein and calories
        if (minCalories != null) {
            url.append("&minCalories=").append(minCalories);
        }
        if (maxCalories != null) {
            url.append("&maxCalories=").append(maxCalories);
        }
        if (minProtein != null) {
            url.append("&minProtein=").append(minProtein);
        }
        if (maxProtein != null) {
            url.append("&maxProtein=").append(maxProtein);
        }

        url.append("&sort=random");

        return url.toString();
    }

    /**
     * Makes HTTP request to Spoonacular API.
     * @param urlString the url of the api call
     * @return the JSON string of the generated recipes
     */
    private String callSpoonacular(String urlString) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        final int timeoutValue = 5000;
        final int statusUpperBound = 200;
        final int statusLowerBound = 300;

        try {
            final URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            // these time out bounds are here to prevent the UI from hanging when the API is un-reachable or slow
            //      after 5 seconds its treated as a failure
            connection.setConnectTimeout(timeoutValue);
            connection.setReadTimeout(timeoutValue);

            final int status = connection.getResponseCode();
            System.out.println("[RECIPE-GEN DAO] HTTP status: " + status);

            // Treat any non-2xx status as a failure (rate limit, server error etc.): return null so upper layers
            // can decide how to notify the user.
            if (status < statusUpperBound || status >= statusLowerBound) {
                System.err.println("[RECIPE-GEN DAO] Spoonacular call failed with status " + status);
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            final StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            return response.toString();

        }
        catch (IOException ex) {
            System.err.println("[RECIPE-GEN DAO] Error calling Spoonacular API: " + ex.getMessage());
            return null;

        }
        finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
            catch (IOException ignored) {
                System.out.println("Ignored exception");
            }
        }
    }

    /**
     * Converts the JSON from the api API call into a list of Recipe objects.
     * @param json the JSON string to be converted
     * @return the list of recipe objects contained within the JSOn
     */
    private List<Recipe> parseRecipesFromJson(String json) {
        final List<Recipe> recipes = new ArrayList<>();

        // if the entire list fails (all recipes fail) then robust error handling happens (user is given error message)
        try {
            final JSONArray results = new JSONObject(json).getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                // parseRecipeFromJson is called to handle whether a single recipe fails
                final Recipe recipe = parseRecipeFromJson(results.getJSONObject(i));
                if (recipe != null) {
                    recipes.add(recipe);
                }
            }
        }
        catch (JSONException ex) {
            System.err.println("Error parsing JSON: " + ex.getMessage());
        }

        return recipes;
    }

    /**
     * Parses a single recipe object from JSON.
     * @param json the JSON object to be parsed
     * @return a recipe contained within the JSON
     */
    private Recipe parseRecipeFromJson(JSONObject json) {
        try {
            final int id = json.getInt("id");
            final String title = json.getString("title");
            final String image = json.optString("image", "");

            final List<Ingredient> ingredients = new ArrayList<>();
            final Map<String, Double> nutritionalValues = new HashMap<>();

            final Recipe recipe = new Recipe(
                    id,
                    title,
                    image,
                    ingredients,
                    "UNKNOWN",
                    nutritionalValues
            );

            final String instructions = json.optString("instructions", "");
            if (!instructions.isBlank()) {
                recipe.setSteps(instructions);
            }

            // Extract nutrients and ingredients
            if (json.has(NUTRITION)) {
                final JSONArray nutrients = json.getJSONObject(NUTRITION).getJSONArray("nutrients");
                for (int i = 0; i < nutrients.length(); i++) {
                    final JSONObject nutrient = nutrients.getJSONObject(i);
                    final String name = nutrient.getString("name");
                    final double amount = nutrient.getDouble("amount");

                    recipe.getNutritionalValues().put(name, amount);
                }

                final JSONArray jsonIngredients = json.getJSONObject(NUTRITION).getJSONArray("ingredients");
                for (int i = 0; i < jsonIngredients.length(); i++) {
                    final JSONObject ingredient = jsonIngredients.getJSONObject(i);
                    final String name = ingredient.getString("name");
                    final double amount = ingredient.getDouble("amount");
                    final String unit = ingredient.getString("unit");
                    recipe.getIngredients().add(new Ingredient(name, amount, unit));
                }
            }

            return recipe;
        }
        catch (JSONException ex) {
            return null;
        }
    }

    /**
     * Returns default dummy recipe (fallback if API fails).
     * @return the list of dummy recipes
     */
    @SuppressWarnings({"checkstyle:MagicNumber", "checkstyle:SuppressWarnings"})
    private List<Recipe> getDefaultRecipes() {
        final List<Recipe> recipes = new ArrayList<>();

        final Recipe veggieTacos = new Recipe(1, "Veggie Tacos", "https://example.com/tacos.jpg", "DINNER");
        veggieTacos.getIngredients().add(new Ingredient("Tortilla", 2, "pieces"));
        veggieTacos.getIngredients().add(new Ingredient("Black beans", 100, "g"));
        veggieTacos.getIngredients().add(new Ingredient("Cheddar cheese", 30, "g"));
        veggieTacos.getNutritionalValues().put("calories", 450.0);
        veggieTacos.getNutritionalValues().put("protein", 18.0);

        recipes.add(veggieTacos);
        return recipes;
    }

    // the calories and protein were filtered both locally and externally by the api for testing purposes for when
    //      the api fails
    /**
     * Filters recipes by calorie and protein bounds.
     * @param recipes the list of recipes to filter
     * @param minCalories the minimum calories
     * @param maxCalories the maximum calories
     * @param minProtein the minimum protein
     * @param maxProtein the maximum protein
     * @return the list of filtered recipes
     */
    private List<Recipe> filterRecipesByNutrition(List<Recipe> recipes, Integer minCalories, Integer maxCalories,
                                                  Integer minProtein, Integer maxProtein) {
        final List<Recipe> filtered = new ArrayList<>();

        for (Recipe recipe : recipes) {
            if (meetsNutritionRequirements(recipe, minCalories, maxCalories, minProtein, maxProtein)) {
                filtered.add(recipe);
            }
        }

        return filtered;
    }

    /**
     * Checks if a recipe meets all nutrition requirements.
     * @param recipe the recipe to be checked
     * @param minCalories the minimum required calories
     * @param maxCalories the maximum desired calories
     * @param minProtein the minimum required protein
     * @param maxProtein the maximum required protein
     * @return whether the recipe meets the requirements
     */
    private boolean meetsNutritionRequirements(Recipe recipe,
                                               Integer minCalories, Integer maxCalories,
                                               Integer minProtein, Integer maxProtein) {
        final Double calories = recipe.getNutritionalValues().get("calories");
        final Double protein = recipe.getNutritionalValues().get("protein");
        boolean result = true;

        if (minCalories != null && calories != null && calories < minCalories) {
            result = false;
        }
        if (maxCalories != null && calories != null && calories > maxCalories) {
            result = false;
        }
        if (minProtein != null && protein != null && protein < minProtein) {
            result = false;
        }
        if (maxProtein != null && protein != null && protein > maxProtein) {
            result = false;
        }

        return result;
    }

    // the following mapping methods are responsible for converting the enums I have stored locally to
    // the exact strings spoonacular expects in the URL the empty string handles cases when there's no filter added
    private String mapDiet(DietaryRestriction dietaryRestriction) {
        if (dietaryRestriction == null || dietaryRestriction == DietaryRestriction.NONE) {
            return "";
        }

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
        if (cuisine == null || cuisine == Cuisine.ANY) {
            return "";
        }

        return switch (cuisine) {
            case MEXICAN -> "mexican";
            case JAPANESE -> "japanese";
            case INDIAN -> "indian";
            case CHINESE -> "chinese";
            default -> "";
        };
    }

    private String mapIntolerances(List<Intolerance> intolerances) {
        if (intolerances == null || intolerances.isEmpty()) {
            return "";
        }

        final List<String> tokens = new ArrayList<>();

        for (Intolerance intolerance : intolerances) {
            if (intolerance == null || intolerance == Intolerance.NONE) {
                continue;
            }

            final String token = switch (intolerance) {
                case DAIRY -> "dairy";
                case NUTS -> "tree_nut";
                case SHELLFISH -> "shellfish";
                case GLUTEN -> "gluten";
                case SOY -> "soy";
                case SEAFOOD -> "seafood";
                case SESAME -> "sesame";
                default -> null;
            };

            if (token != null) {
                tokens.add(token);
            }
        }

        return String.join(",", tokens);
    }
}
