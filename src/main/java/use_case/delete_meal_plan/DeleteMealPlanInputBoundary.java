package use_case.delete_meal_plan;

/**
 * Input boundary for the delete meal plan use case.
 */
public interface DeleteMealPlanInputBoundary {

    /**
     * Executes the delete meal plan use case.
     * @param deleteMealPlanInputData the input data for the use case
     */
    void execute(DeleteMealPlanInputData deleteMealPlanInputData);
}
