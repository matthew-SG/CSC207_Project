package use_case.approve_recipe;

/**
 * Input boundary for the approve recipe use case.
 */
public interface ApproveRecipeInputBoundary {
    /**
     * Load recipes for the user to approve or decline.
     */
    void loadRecipes();

    /**
     * User approves/likes a recipe.
     * @param inputData contains recipe and user info
     */
    void approveRecipe(ApproveRecipeInputData inputData);

    /**
     * User declines/dislikes a recipe.
     * @param inputData contains recipe and user info
     */
    void declineRecipe(DeclineRecipeInputData inputData);
}
