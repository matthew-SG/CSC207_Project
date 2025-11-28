package data_access;

import entities.Recipe;
import entities.User;
import use_case.approve_recipe.ApproveRecipeDataAccessInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Dummy data access object for the approve recipe use case.
 * Provides sample recipes for MVP testing.
 */
public class DummyApproveRecipeDataAccessObject implements ApproveRecipeDataAccessInterface {
    private final List<Recipe> availableRecipes;
    private final Map<String, User> users;

    public DummyApproveRecipeDataAccessObject(Map<String, User> users) {
        this.users = users;
        this.availableRecipes = createSampleRecipes();
    }

    private List<Recipe> createSampleRecipes() {
        List<Recipe> recipes = new ArrayList<>();

        // Recipe 1: Spaghetti Carbonara
        Recipe recipe1 = new Recipe(1, "Spaghetti Carbonara",
                "https://via.placeholder.com/300x300?text=Spaghetti+Carbonara", new ArrayList<>(),
                "Dinner", new HashMap<>());
        recipes.add(recipe1);

        // Recipe 2: Chicken Stir Fry
        Recipe recipe2 = new Recipe(2, "Chicken Stir Fry",
                "https://via.placeholder.com/300x300?text=Chicken+Stir+Fry", new ArrayList<>(),
                "Lunch", new HashMap<>());
        recipes.add(recipe2);

        // Recipe 3: Caesar Salad
        Recipe recipe3 = new Recipe(3, "Caesar Salad",
                "https://via.placeholder.com/300x300?text=Caesar+Salad", new ArrayList<>(),
                "Lunch",new HashMap<>());
        recipes.add(recipe3);

        // Recipe 4: Pancakes
        Recipe recipe4 = new Recipe(4, "Pancakes",
                "https://via.placeholder.com/300x300?text=Pancakes", new ArrayList<>(),
                "Breakfast", new HashMap<>());
        recipes.add(recipe4);

        // Recipe 5: Grilled Salmon
        Recipe recipe5 = new Recipe(5, "Grilled Salmon",
                "https://via.placeholder.com/300x300?text=Grilled+Salmon", new ArrayList<>(),
                "Dinner", new HashMap<>());
        recipes.add(recipe5);

        return recipes;
    }

    @Override
    public List<Recipe> getAvailableRecipes() {
        return new ArrayList<>(availableRecipes);
    }

    @Override
    public Recipe getRecipeById(int recipeId) {
        for (Recipe recipe : availableRecipes) {
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
            }
        }
    }
}
