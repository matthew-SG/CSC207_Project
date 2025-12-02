package use_case.liked_recipe_list;

import use_case.step_by_step.StepByStepInputData;

/**
 * Output boundary interface for the Liked Recipe use case.
 * Defines methods for presenting liked recipe data, hands-free navigation,
 * and error messages to the view layer.
 */
public interface LikedRecipeOutputBoundary {

    /**
     * Prepares and presents the liked recipes view with the provided data.
     * This displays the list of all recipes the user has saved.
     * @param likedRecipeOutputData contains recipe IDs, names, images, nutrition,
     *                              ingredients, and steps for all liked recipes
     */
    void prepareLikedRecipeView(LikedRecipeOutputData likedRecipeOutputData);

    /**
     * Prepares and launches the hands-free step-by-step instruction view.
     * This transitions the user to a guided cooking experience with
     * navigation through recipe steps.
     * @param stepByStepInputData contains the recipe instructions and starting step index
     */
    void prepareHandsfree(StepByStepInputData stepByStepInputData);

    /**
     * Prepares and displays an error message to the user.
     * Called when any operation in the liked recipe use case fails.
     * @param error the error message to display
     */
    void prepareFailView(String error);
}
