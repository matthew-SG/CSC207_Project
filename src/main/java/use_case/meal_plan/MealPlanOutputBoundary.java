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
     * @param error the error to be displayed
     */
    void prepareFailView(String error);
    
    /**
     * Prepares the fail view for the Meal Plan Use Case
     * @param caloriesError the explanation of the error for the calories input
     * @param proteinError the explanation of the error for the protein input
     * @param carbsError the explanation of the error for the carbs input
     * @param fatsError the explanation of the error for the fats input
     */
    void prepareFailView(String caloriesError, String proteinError, String carbsError, String fatsError);
}
