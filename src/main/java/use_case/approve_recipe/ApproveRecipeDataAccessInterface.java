package use_case.approve_recipe;

import entities.Recipe;
import entities.User;

import java.util.List;

/**
 * Data access interface for the approve recipe use case.
 */
public interface ApproveRecipeDataAccessInterface {
    /**
     * Get available recipes for user to approve.
     * @return list of available recipes
     */
    List<Recipe> getAvailableRecipes();

    /**
     * Get a recipe by its ID.
     * @param recipeId the recipe ID
     * @return the recipe, or null if not found
     */
    Recipe getRecipeById(int recipeId);

    /**
     * Get the current user.
     * @param username the username
     * @return the user
     */
    User getUser(String username);

    /**
     * Save a recipe to the user's liked recipes.
     * @param username the username
     * @param recipe the recipe to save
     */
    void saveRecipeToUser(String username, Recipe recipe);
}
