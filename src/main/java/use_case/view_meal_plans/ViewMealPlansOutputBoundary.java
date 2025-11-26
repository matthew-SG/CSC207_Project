package use_case.view_meal_plans;

/**
 * The Output Boundary for the View Meal Plans Use Case
 */
public interface ViewMealPlansOutputBoundary {

    /**
     * Prepares the success view for the View Meal Plans Use Case
     * @param viewMealPlansOutputData
     */
    void prepareSuccessView(ViewMealPlansOutputData viewMealPlansOutputData);

    /**
     * Prepares the failure view for the View Meal Plans Use Case
     * @param error the error to be displayed to the user
     */
    void prepareFailView(String error);
}
