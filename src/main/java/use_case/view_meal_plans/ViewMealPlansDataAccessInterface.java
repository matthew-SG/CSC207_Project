package use_case.view_meal_plans;

import java.util.List;

import entities.MealPlan;

/**
 * DAO Interface for the View Meal Plans Use Case.
 */
public interface ViewMealPlansDataAccessInterface {

    /**
     * Retrieves the saved meal plans of the current user.
     * @return the saved meal plans of the current user
     */
    List<MealPlan> getMealPlans();
}
