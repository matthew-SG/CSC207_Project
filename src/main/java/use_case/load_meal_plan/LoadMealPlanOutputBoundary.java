package use_case.load_meal_plan;

import use_case.meal_plan.MealPlanOutputData;

/**
 * Output Boundary for the Load Meal Plan Use Case.
 */
public interface LoadMealPlanOutputBoundary {

    /**
     * Prepares the success view for the Load Meal Plan Use Case.
     * @param mealPlanOutputData the output data
     */
    void prepareSuccessView(MealPlanOutputData mealPlanOutputData);
}
