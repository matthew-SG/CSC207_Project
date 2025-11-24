package use_case.load_meal_plan;

import entities.MealPlan;

import java.util.List;

/**
 * The Input Data for the View Meal Plans Use Case
 */
public class ViewMealPlansInputData {

    private final List<MealPlan> mealPlans;

    public ViewMealPlansInputData(List<MealPlan> mealPlans) {
        this.mealPlans = mealPlans;
    }

    List<MealPlan> getMealPlans() {
        return mealPlans;
    }
}
