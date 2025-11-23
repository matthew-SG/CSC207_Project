package use_case.load_meal_plan;

import entities.MealPlan;

/**
 * The Input Data for the Load Meal Plan Use Case
 */
public class LoadMealPlanInputData {

    private final MealPlan mealPlan;

    public LoadMealPlanInputData(MealPlan mealPlan) {
        this.mealPlan = mealPlan;
    }

    MealPlan getMealPlan() {
        return mealPlan;
    }
}
