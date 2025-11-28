package use_case.load_meal_plan;

/**
 * Input Boundary for the Load Meal Plan Use Case
 */
public interface LoadMealPlanInputBoundary {

    /**
     * Executes the Load Meal Plan Use Case
     * @param loadMealPlanInputData the input data
     */
    void execute(LoadMealPlanInputData loadMealPlanInputData);
}
