package use_case.likedRecipeList;

import use_case.step_by_step.StepByStepInputData;

public interface LikedRecipeOutputBoundary {

    void prepareLikedRecipeView(LikedRecipeOutputData likedRecipeOutputData);
    void prepareHandsfree(StepByStepInputData stepByStepInputData);
    void prepareFailView(String error);
}
