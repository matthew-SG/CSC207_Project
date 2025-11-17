package use_case.approve_recipe;

/**
 * Output boundary for the approve recipe use case.
 */
public interface ApproveRecipeOutputBoundary {
    /**
     * Prepare the view to display recipes for approval.
     * @param outputData contains recipes to display
     */
    void prepareRecipeView(ApproveRecipeOutputData outputData);

    /**
     * Prepare success view after approving a recipe.
     * @param outputData contains updated recipe list
     */
    void prepareApproveSuccessView(ApproveRecipeOutputData outputData);

    /**
     * Prepare view after declining a recipe.
     * @param outputData contains remaining recipes
     */
    void prepareDeclineView(ApproveRecipeOutputData outputData);

    /**
     * Prepare failure view with error message.
     * @param error the error message
     */
    void prepareFailView(String error);
}
