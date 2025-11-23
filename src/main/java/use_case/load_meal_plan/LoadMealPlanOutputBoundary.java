package use_case.load_meal_plan;

/**
 * The Output Boundary for the Load Meal Plan Use Case
 */
public interface LoadMealPlanOutputBoundary {

    /**
     * Prepares the success view for the Load Meal Plan Output Data Use Case
     * @param loadMealPlanOutputData
     */
    void prepareSuccessView(LoadMealPlanOutputData loadMealPlanOutputData);
}
