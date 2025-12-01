package use_case.view_meal_plans;

import java.util.ArrayList;
import java.util.List;

import entities.MealPlan;

/**
 * The View Meal Plans Use Case Interactor.
 */
public class ViewMealPlansInteractor implements ViewMealPlansInputBoundary {
    private final ViewMealPlansDataAccessInterface viewMealPlansDataAccessObject;
    private final ViewMealPlansOutputBoundary viewMealPlansPresenter;

    public ViewMealPlansInteractor(ViewMealPlansDataAccessInterface viewMealPlansDataAccessObject,
                                   ViewMealPlansOutputBoundary viewMealPlansPresenter) {
        this.viewMealPlansPresenter = viewMealPlansPresenter;
        this.viewMealPlansDataAccessObject = viewMealPlansDataAccessObject;
    }

    /**
     * Executes the view meal plan use case.
     */
    public void execute() {
        final List<MealPlan> mealPlans = viewMealPlansDataAccessObject.getMealPlans();

        if (mealPlans.isEmpty()) {
            viewMealPlansPresenter.prepareFailView("You currently have no meal plans saved!");
        }
        else {
            final List<String> firstRecipeNames = getFirstRecipeNames(mealPlans);
            final List<Double> targetCalories = getTargetCalories(mealPlans);
            final List<Double> targetProtein = getTargetProtein(mealPlans);
            final List<Double> targetCarbs = getTargetCarbs(mealPlans);
            final List<Double> targetFats = getTargetFats(mealPlans);

            final ViewMealPlansOutputData viewMealPlansOutputData = new ViewMealPlansOutputData(firstRecipeNames,
                    targetCalories, targetProtein, targetCarbs, targetFats);

            viewMealPlansPresenter.prepareSuccessView(viewMealPlansOutputData);
        }
    }

    /**
     * Returns a list of the target carbs of every Meal Plan in mealPlans.
     * @param mealPlans the list of MealPlans
     * @return the list of the target fats for each MealPlan in mealPlans
     */
    private static List<Double> getTargetFats(List<MealPlan> mealPlans) {
        final List<Double> result = new ArrayList<>();
        for (MealPlan mealPlan : mealPlans) {
            result.add(mealPlan.getTargetFats());
        }
        return result;
    }

    /**
     * Returns a list of the target carbs of every Meal Plan in mealPlans.
     * @param mealPlans the list of MealPlans
     * @return  the list of the target carbs for each MealPlan in mealPlans
     */
    private static List<Double> getTargetCarbs(List<MealPlan> mealPlans) {
        final List<Double> result = new ArrayList<>();
        for (MealPlan mealPlan : mealPlans) {
            result.add(mealPlan.getTargetCarbs());
        }
        return result;
    }

    /**
     * Returns a list of the target calories of every Meal Plan in mealPlans.
     * @param mealPlans the list of MealPlans
     * @return the list of the target calories for each MealPlan in mealPlans
     */
    private static List<Double> getTargetProtein(List<MealPlan> mealPlans) {
        final List<Double> result = new ArrayList<>();
        for (MealPlan mealPlan : mealPlans) {
            result.add(mealPlan.getTargetProtein());
        }
        return result;
    }

    /**
     * Retrieves a list of all the target calories of each meal plan in mealPlans.
     * @param mealPlans the list of MealPlans to be iterated over
     * @return the list of the target calories for each MealPlan
     */
    private static List<Double> getTargetCalories(List<MealPlan> mealPlans) {
        final List<Double> result = new ArrayList<>();
        for (MealPlan mealPlan : mealPlans) {
            result.add(mealPlan.getTargetCalories());
        }

        return result;
    }

    /**
     * Retrieves the name of the first recipe of each meal plan in mealPlans.
     * @param mealPlans the list of MealPlans to be iterated over
     * @return the list of the names of first recipes of each MealPlan
     */
    private static List<String> getFirstRecipeNames(List<MealPlan> mealPlans) {
        final List<String> result = new ArrayList<>();
        for (MealPlan mealPlan : mealPlans) {
            result.add(mealPlan.getRecipes().get(0).getRecipeName());
        }

        return result;
    }
}
