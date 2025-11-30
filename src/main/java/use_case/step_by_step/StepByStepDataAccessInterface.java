package use_case.step_by_step;

import entities.BasicRecipe;
import java.util.List;

public interface StepByStepDataAccessInterface {
    void saveLikedRecipe(String username, BasicRecipe recipe);

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
    List<BasicRecipe> getLikedRecipes(String username);

    /**
     * Gets the current logged-in username.
     * @return the current username
     */
    String getCurrentUsername();
}
