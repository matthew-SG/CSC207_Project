package use_case.delete_meal_plan;

import entities.MealPlan;

import java.util.List;

/**
 * DAI for the Delete Meal Plan Use Case
 */
public interface DeleteMealPlanDataAccessInterface {

    /**
     * Retreives the meal plans of the current user
     * @return the list of meal plans of the current user
     */
    List<MealPlan> getMealPlans();

    /**
     * Saves the meal plans of the current user to the database
     */
    void saveMealPlans();
}
