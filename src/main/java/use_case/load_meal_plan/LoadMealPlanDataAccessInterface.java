package use_case.load_meal_plan;

import entities.MealPlan;

import java.util.List;

/**
 * DAI for the Load Meal Plan Use Case
 */
public interface LoadMealPlanDataAccessInterface {

    /**
     * Retrieves the meal plans of the current user
     * @return the meal plans of the current user
     */
    List<MealPlan> getMealPlans();
}
