package use_case.delete_meal_plan;

import entities.MealPlan;
import use_case.view_meal_plans.ViewMealPlansOutputBoundary;
import use_case.view_meal_plans.ViewMealPlansOutputData;

import java.util.ArrayList;
import java.util.List;

/**
 * Interactor for the delete meal plan use case
 */
public class DeleteMealPlanInteractor implements DeleteMealPlanInputBoundary {
    private final DeleteMealPlanDataAccessInterface dataAccessObject;
    private final DeleteMealPlanOutputBoundary deleteMealPlanPresenter;
    private final ViewMealPlansOutputBoundary viewMealPlansPresenter;

    public DeleteMealPlanInteractor(DeleteMealPlanDataAccessInterface dataAccessObject,
                                    DeleteMealPlanOutputBoundary deleteMealPlanPresenter,
                                    ViewMealPlansOutputBoundary viewMealPlansPresenter) {
        this.dataAccessObject = dataAccessObject;
        this.deleteMealPlanPresenter = deleteMealPlanPresenter;
        this.viewMealPlansPresenter = viewMealPlansPresenter;
    }

    @Override
    public void execute(DeleteMealPlanInputData deleteMealPlanInputData) {
        final int index = deleteMealPlanInputData.getIndex();
        List<MealPlan> mealPlans = dataAccessObject.getMealPlans();

        if (mealPlans.size() == 1) {
            deleteMealPlanPresenter.prepareFailureView("Cannot delete only meal plan!");
        } else {
            dataAccessObject.deleteMealPlan(index);
            mealPlans = dataAccessObject.getMealPlans();

            final List<String> firstRecipeNames = getFirstRecipeNames(mealPlans);
            final List<Double> targetCalories = getTargetCalories(mealPlans);
            final List<Double> targetProtein = getTargetProtein(mealPlans);
            final List<Double> targetCarbs = getTargetCarbs(mealPlans);
            final List<Double> targetFats = getTargetFats(mealPlans);

            ViewMealPlansOutputData viewMealPlansOutputData = new ViewMealPlansOutputData(firstRecipeNames,
                    targetCalories, targetProtein, targetCarbs, targetFats);

            viewMealPlansPresenter.prepareSuccessView(viewMealPlansOutputData);
        }
    }

    /**
     * Returns a list of the target carbs of every Meal Plan in mealPlans
     * @param mealPlans the list of MealPlans
     * @return the list of the target fats for each MealPlan in mealPlans
     */
    private static List<Double> getTargetFats(List<MealPlan> mealPlans) {
        List<Double> result = new ArrayList<>();
        for (MealPlan mealPlan : mealPlans) {
            result.add(mealPlan.getTargetFats());
        }
        return result;
    }

    /**
     * Returns a list of the target carbs of every Meal Plan in mealPlans
     * @param mealPlans the list of MealPlans
     * @return  the list of the target carbs for each MealPlan in mealPlans
     */
    private static List<Double> getTargetCarbs(List<MealPlan> mealPlans) {
        List<Double> result = new  ArrayList<>();
        for (MealPlan mealPlan : mealPlans) {
            result.add(mealPlan.getTargetCarbs());
        }
        return result;
    }

    /**
     * Returns a list of the target calories of every Meal Plan in mealPlans
     * @param mealPlans the list of MealPlans
     * @return the list of the target calories for each MealPlan in mealPlans
     */
    private static List<Double> getTargetProtein(List<MealPlan> mealPlans) {
        List<Double> result = new ArrayList<>();
        for (MealPlan mealPlan : mealPlans) {
            result.add(mealPlan.getTargetProtein());
        }
        return result;
    }

    /**
     * Retrieves a list of all the target calories of each meal plan in mealPlans
     * @param mealPlans the list of MealPlans to be iterated over
     * @return the list of the target calories for each MealPlan
     */
    private static List<Double> getTargetCalories(List<MealPlan> mealPlans) {
        List<Double> result = new ArrayList<>();
        for  (MealPlan mealPlan : mealPlans) {
            result.add(mealPlan.getTargetCalories());
        }

        return result;
    }

    /**
     * Retrieves the name of the first recipe of each meal plan in mealPlans
     * @param mealPlans the list of MealPlans to be iterated over
     * @return the list of the names of first recipes of each MealPlan
     */
    private static List<String> getFirstRecipeNames(List<MealPlan> mealPlans) {
        List<String> result = new ArrayList<>();
        for (MealPlan mealPlan : mealPlans) {
            result.add(mealPlan.getRecipes().get(0).getRecipeName());
        }

        return result;
    }
}
