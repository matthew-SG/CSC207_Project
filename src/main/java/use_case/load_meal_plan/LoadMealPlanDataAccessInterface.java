package use_case.load_meal_plan;

import java.util.List;

import entities.MealPlan;

/**
 * DAI for the Load Meal Plan Use Case.
 */
public interface LoadMealPlanDataAccessInterface {

    /**
     * Retrieves the meal plans of the current user.
     * @return the meal plans of the current user
     */
    List<MealPlan> getMealPlans();
}
