package use_case.delete_meal_plan;

import java.util.List;

import entities.MealPlan;

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
     * Deletes the specified meal plan of the current user
     * @param index the index of the meal plan in the user's saved meal plans list
     */
    void deleteMealPlan(int index);
}
