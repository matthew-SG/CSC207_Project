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
     * Prepares the fail view for the Meal Plan Use Case
     * @param listError the error to be displayed due to the saved recipes
     * @param inputError the error to be displayed due to the input
     */
    void prepareFailView(String listError, String inputError);
}
