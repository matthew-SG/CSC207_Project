package use_case.meal_plan;

import entities.Ingredient;
import entities.MealPlan;
import entities.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The Meal Plan Use Case Interactor
 */
public class MealPlanInteractor implements MealPlanInputBoundary{
    private final MealPlanUserDataAccessInterface userDataAccessObject;
    private final MealPlanOutputBoundary mealPlanPresenter;

    public MealPlanInteractor(MealPlanUserDataAccessInterface userDataAccessObject,
                              MealPlanOutputBoundary mealPlanPresenter) {
        this.userDataAccessObject = userDataAccessObject;
        this.mealPlanPresenter = mealPlanPresenter;
    }

    public void execute(MealPlanInputData mealPlanInputData) {
        final List<Recipe> savedRecipes = userDataAccessObject.getSavedRecipes();
        final int calories = mealPlanInputData.getTargetCalories();
        final int protein = mealPlanInputData.getTargetProtein();
        final int carbs = mealPlanInputData.getTargetCarbs();
        final int fats = mealPlanInputData.getTargetFats();
        String[] recipeNames = new String[3];
        String[] recipeImages = new String[3];
        List<Ingredient> recipeIngredients = new ArrayList<>();
        List<Map<String, Double>> recipeNutritionalValues = new ArrayList<>();
        if (savedRecipes.size() < 3) {
            mealPlanPresenter.prepareFailView("At least 3 saved recipes must be saved for meal plan generation.");
        } else if (calories < 0 || protein < 0 || carbs < 0 || fats < 0) {
            mealPlanPresenter.prepareFailView("All input values must be positive.");
        } else if (savedRecipes.size() == 3) {
            MealPlan mealPlan = new MealPlan(savedRecipes, calories, protein, carbs, fats);
            userDataAccessObject.saveMealPlan(mealPlan);
            int i = 0;
            for (Recipe recipe : savedRecipes) {

            }

        }
    }

}
