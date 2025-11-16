package interface_adapter.approve_recipe;

import use_case.approve_recipe.ApproveRecipeInputBoundary;
import use_case.approve_recipe.ApproveRecipeInputData;
import use_case.approve_recipe.DeclineRecipeInputData;

/**
 * Controller for the approve recipe use case.
 */
public class ApproveRecipeController {
    private final ApproveRecipeInputBoundary interactor;

    public ApproveRecipeController(ApproveRecipeInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Load available recipes for approval.
     */
    public void loadRecipes() {
        interactor.loadRecipes();
    }

    /**
     * Approve/like a recipe.
     * @param recipeId the ID of the recipe to approve
     * @param username the current user's username
     */
    public void approveRecipe(int recipeId, String username) {
        ApproveRecipeInputData inputData = new ApproveRecipeInputData(recipeId, username);
        interactor.approveRecipe(inputData);
    }

    /**
     * Decline/dislike a recipe.
     * @param recipeId the ID of the recipe to decline
     * @param username the current user's username
     */
    public void declineRecipe(int recipeId, String username) {
        DeclineRecipeInputData inputData = new DeclineRecipeInputData(recipeId, username);
        interactor.declineRecipe(inputData);
    }
}
