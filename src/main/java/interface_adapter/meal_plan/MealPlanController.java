package interface_adapter.meal_plan;

import use_case.meal_plan.MealPlanInputBoundary;
import use_case.meal_plan.MealPlanInputData;

/**
 * Controller for the Meal Plan Use Case
 */
public class MealPlanController {
    private final MealPlanInputBoundary userMealPlanUseCaseInteractor;

    public MealPlanController(MealPlanInputBoundary userMealPlanUseCaseInteractor) {
        this.userMealPlanUseCaseInteractor = userMealPlanUseCaseInteractor;
    }

    /**
     * Executes the Meal Plan Use Case
     * @param targetCalories the target calories
     * @param targetProtein the target protein
     * @param targetCarbs the target carbs
     * @param targetFats the target fats
     */
    public void execute(String targetCalories, String targetProtein, String targetCarbs, String targetFats) {
        final MealPlanInputData mealPlanInputData = new MealPlanInputData(targetCalories, targetProtein, targetCarbs,
                targetFats);

        userMealPlanUseCaseInteractor.execute(mealPlanInputData);
    }

}
