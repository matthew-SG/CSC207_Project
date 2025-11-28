package use_case.view_meal_plans;

import entities.MealPlan;

import java.util.List;

/**
 * DAO Interface for the View Meal Plans Use Case
 */
public interface ViewMealPlansDataAccessInterface {

    /**
     * Retrieves the saved meal plans of the current user
     * @return the saved meal plans of the current user
     */
    public List<MealPlan> getMealPlans();
}
