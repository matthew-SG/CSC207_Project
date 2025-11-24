package use_case.load_meal_plan;

/**
 * The Output Boundary for the View Meal Plans Use Case
 */
public interface ViewMealPlansOutputBoundary {

    /**
     * Prepares the success view for the View Meal Plans Output Data Use Case
     * @param viewMealPlansOutputData
     */
    void prepareSuccessView(ViewMealPlansOutputData viewMealPlansOutputData);
}
