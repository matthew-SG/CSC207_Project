package data_access;

import entities.Recipe;
import entities.User;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.approve_recipe.ApproveRecipeDataAccessInterface;

import java.io.IOException;
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
    private final OkHttpClient client;
    private List<Recipe> cachedRecipes;

    public SpoonacularApproveRecipeDataAccessObject(Map<String, User> users) {
        this.users = users;
        this.client = new OkHttpClient();
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
        } catch (IOException e) {
            System.err.println("Error fetching recipes from API: " + e.getMessage());
            // Return empty list on error
            return new ArrayList<>();
        }
    }

    private List<Recipe> fetchRandomRecipes(int number) throws IOException {
        String url = API_BASE_URL + "/random?number=" + number + "&apiKey=" + API_KEY;

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response);
            }

            String responseBody = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseBody);
            JSONArray recipesArray = jsonResponse.getJSONArray("recipes");

            List<Recipe> recipes = new ArrayList<>();
            for (int i = 0; i < recipesArray.length(); i++) {
                JSONObject recipeJson = recipesArray.getJSONObject(i);

                int id = recipeJson.getInt("id");
                String title = recipeJson.getString("title");
                String image = recipeJson.optString("image", "https://via.placeholder.com/300x300?text=No+Image");

                Recipe recipe = new Recipe(id, title, image, "main course");
                recipes.add(recipe);
            }

            return recipes;
        }
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
