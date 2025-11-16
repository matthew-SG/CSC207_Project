package use_case.meal_plan;

/**
 * The Output Boundary for the Meal Plan Use Case.
 */
public interface MealPlanOutputBoundary {

    /**
     * Prepares the success view for the Meal Plan Use Case.
     * @param mealPlanOutputData the output data
     */
    void prepareSuccessView(MealPlanOutputData mealPlanOutputData);

    /**
     * Prepares the failure view for the Meal Plan Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
