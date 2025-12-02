package use_case.liked_recipe_list;

import java.util.List;

import entities.Ingredient;
import entities.InstructionStep;
import entities.Recipe;
import entities.User;

/**
 * Data Access Interface for the Liked Recipe use case.
 * Handles persistence of user's liked recipes.
 */
public interface LikedRecipeDataAccessInterface {

    /**
     * Saves a recipe to the user's liked recipes list.
     * @param username the username of the current user
     * @param recipe the recipe to save
     */
    void saveLikedRecipe(String username, Recipe recipe);

    /**
     * Removes a recipe from the user's liked recipes list.
     * @param username the username of the current user
     * @param recipeId the ID of the recipe to remove
     */
    void deleteLikedRecipe(String username, int recipeId);

    /**
     * Retrieves all liked recipes for the current user.
     * @param username the username of the current user
     * @return list of liked recipes
     */
    List<Recipe> getLikedRecipes(String username);

    /**
     * Gets a recipe by its ID (for fetching full recipe details when liking).
     * @param recipeId the recipe ID
     * @return the recipe, or null if not found
     */
    Recipe getRecipeById(int recipeId);

    /**
     * Gets the current logged-in username.
     * @return the current username
     */
    String getCurrentUsername();

    /**
     * Fetches analyzed instructions for a recipe from the API.
     * @param recipeId the recipe ID
     * @return list of instruction steps
     */
    List<InstructionStep> getAnalyzedInstructions(int recipeId);

    /**
     * Gets a user from the users map.
     * @param username the username to retrieve
     * @return the User object, or null if not found
     */
    User getUser(String username);

    /**
     * Adds the given ingredients to the user's grocery list and persists it.
     * @param  username the username of the user
     * @param  ingredients the list of ingredients to be added to the grocery list
     */
    void addIngredientsToGroceryList(String username, List<Ingredient> ingredients);
}
