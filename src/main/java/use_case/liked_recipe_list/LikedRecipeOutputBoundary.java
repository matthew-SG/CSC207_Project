package use_case.liked_recipe_list;

import use_case.step_by_step.StepByStepInputData;

public interface LikedRecipeOutputBoundary {

    void prepareLikedRecipeView(LikedRecipeOutputData likedRecipeOutputData);
    void prepareHandsfree(StepByStepInputData stepByStepInputData);
    void prepareFailView(String error);
}
