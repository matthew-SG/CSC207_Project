package use_case.delete_meal_plan;

/**
 * The output boundary for the delete meal plan use case.
 */
public interface DeleteMealPlanOutputBoundary {

    /**
     * Prepares the failure view for the delete meal plan use case.
     * @param error the error message to be displayed
     */
    void prepareFailureView(String error);
}
