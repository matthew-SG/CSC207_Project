package use_case.meal_plan;

import java.util.List;

import entities.MealPlan;
import entities.Recipe;

/**
 * DAO interface for the Meal Plan Use Case.
 */
public interface MealPlanUserDataAccessInterface {

    /**
     * Returns the saved recipes of the current user of the application.
     * @return the saved recipes of the current user
     */
    List<Recipe> getSavedRecipes();

    /**
     * Saves the generated meal plan to the current user of the application.
     * @param mealPlan the generated meal plan
     */
    void saveMealPlan(MealPlan mealPlan);
}
