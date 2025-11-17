package data_access;

import entities.Recipe;
import entities.User;
import use_case.approve_recipe.ApproveRecipeDataAccessInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * API data access object for fetching real recipes from Spoonacular API.
 * User data is stored locally in memory (not persisted to database).
 */
public class SpoonacularApproveRecipeDataAccessObject implements ApproveRecipeDataAccessInterface {
    private static final String API_KEY = "5b07df6820b74cf1b2eae9c1b440f014";
    private static final String API_BASE_URL = "https://api.spoonacular.com/recipes";

    private final Map<String, User> users;
    private List<Recipe> cachedRecipes;

    public SpoonacularApproveRecipeDataAccessObject(Map<String, User> users) {
        this.users = users;
        this.cachedRecipes = null;
    }

    @Override
    public List<Recipe> getAvailableRecipes() {
        // If we already fetched recipes, return cached version
        if (cachedRecipes != null) {
            return new ArrayList<>(cachedRecipes);
        }

        // Fetch random recipes from API
        try {
            cachedRecipes = fetchRandomRecipes(10);
            return new ArrayList<>(cachedRecipes);
        } catch (Exception e) {
            System.err.println("Error fetching recipes from API: " + e.getMessage());
            e.printStackTrace();
            // Fallback to dummy recipes on error
            return getFallbackRecipes();
        }
    }

    private List<Recipe> fetchRandomRecipes(int number) throws IOException {
        String urlString = API_BASE_URL + "/random?number=" + number + "&apiKey=" + API_KEY;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("API returned response code: " + responseCode);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return parseRecipesFromJson(response.toString());
    }

    private List<Recipe> parseRecipesFromJson(String jsonResponse) {
        List<Recipe> recipes = new ArrayList<>();

        try {
            // Simple JSON parsing - extract recipes array
            int recipesStart = jsonResponse.indexOf("\"recipes\":[") + 11;
            int recipesEnd = jsonResponse.lastIndexOf("]");
            String recipesArrayStr = jsonResponse.substring(recipesStart, recipesEnd);

            // Split by recipe objects
            String[] recipeObjects = recipesArrayStr.split("\\},\\{");

            for (String recipeStr : recipeObjects) {
                // Clean up the string
                recipeStr = recipeStr.replace("{", "").replace("}", "");

                // Extract id
                int id = extractIntValue(recipeStr, "\"id\":");

                // Extract title
                String title = extractStringValue(recipeStr, "\"title\":\"");

                // Extract image URL
                String image = extractStringValue(recipeStr, "\"image\":\"");
                if (image.isEmpty()) {
                    image = "https://via.placeholder.com/300x300?text=No+Image";
                }

                if (id != -1 && !title.isEmpty()) {
                    Recipe recipe = new Recipe(id, title, image, "main course");
                    recipes.add(recipe);
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing JSON: " + e.getMessage());
            return getFallbackRecipes();
        }

        return recipes.isEmpty() ? getFallbackRecipes() : recipes;
    }

    private int extractIntValue(String json, String key) {
        try {
            int start = json.indexOf(key);
            if (start == -1) return -1;
            start += key.length();

            int end = json.indexOf(",", start);
            if (end == -1) end = json.length();

            String valueStr = json.substring(start, end).trim();
            return Integer.parseInt(valueStr);
        } catch (Exception e) {
            return -1;
        }
    }

    private String extractStringValue(String json, String key) {
        try {
            int start = json.indexOf(key);
            if (start == -1) return "";
            start += key.length();

            int end = json.indexOf("\"", start);
            if (end == -1) return "";

            return json.substring(start, end);
        } catch (Exception e) {
            return "";
        }
    }

    private List<Recipe> getFallbackRecipes() {
        // Fallback dummy recipes if API fails
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(new Recipe(1, "Spaghetti Carbonara",
                "https://via.placeholder.com/300x300?text=Spaghetti+Carbonara", "Dinner"));
        recipes.add(new Recipe(2, "Chicken Stir Fry",
                "https://via.placeholder.com/300x300?text=Chicken+Stir+Fry", "Lunch"));
        recipes.add(new Recipe(3, "Caesar Salad",
                "https://via.placeholder.com/300x300?text=Caesar+Salad", "Lunch"));
        return recipes;
    }

    @Override
    public Recipe getRecipeById(int recipeId) {
        if (cachedRecipes == null) {
            getAvailableRecipes();
        }

        for (Recipe recipe : cachedRecipes) {
            if (recipe.getRecipeId() == recipeId) {
                return recipe;
            }
        }
        return null;
    }

    @Override
    public User getUser(String username) {
        return users.get(username);
    }

    @Override
    public void saveRecipeToUser(String username, Recipe recipe) {
        User user = users.get(username);
        if (user != null) {
            // Check if recipe is already in saved recipes
            boolean alreadySaved = user.getSavedRecipes().stream()
                    .anyMatch(r -> r.getRecipeId() == recipe.getRecipeId());

            if (!alreadySaved) {
                user.getSavedRecipes().add(recipe);
                System.out.println("Recipe '" + recipe.getRecipeName() + "' added to " + username + "'s saved recipes!");
            }
        }
    }
}
