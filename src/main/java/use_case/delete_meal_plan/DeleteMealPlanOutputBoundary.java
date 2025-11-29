package use_case.delete_meal_plan;

import use_case.view_meal_plans.ViewMealPlansOutputData;

/**
 * The output boundary for the delete meal plan use case
 */
public interface DeleteMealPlanOutputBoundary {

    /**
     * Prepares the success view for the delete meal plan use case
     * @param viewMealPlansOutputData the output data for the view meal plans view
     */
    void prepareSuccessView(ViewMealPlansOutputData viewMealPlansOutputData);

    /**
     * Prepares the failure view for the delete meal plan use case
     * @param error the error message to be displayed
     */
    void prepareFailureView(String error);
}
