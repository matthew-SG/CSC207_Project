package use_case.load_meal_plan;

import java.util.List;

import entities.MealPlan;
import entities.Recipe;
import use_case.meal_plan.MealPlanOutputData;

/**
 * Interactor for the Load Meal Plan Use Case.
 */
public class LoadMealPlanInteractor implements LoadMealPlanInputBoundary {
    private final LoadMealPlanDataAccessInterface dataAccessObject;
    private final LoadMealPlanOutputBoundary loadMealPlanPresenter;

    public LoadMealPlanInteractor(LoadMealPlanDataAccessInterface dataAccessObject,
                                  LoadMealPlanOutputBoundary loadMealPlanPresenter) {
        this.dataAccessObject = dataAccessObject;
        this.loadMealPlanPresenter = loadMealPlanPresenter;
    }

    /**
     * Executes the load meal plan use case.
     * @param loadMealPlanInputData the input data
     */
    public void execute(LoadMealPlanInputData loadMealPlanInputData) {
        final int index = loadMealPlanInputData.getIndex();
        final List<MealPlan> mealPlans = dataAccessObject.getMealPlans();

        // Gathers the meal plan's recipes
        final MealPlan mealPlan = mealPlans.get(index);
        final List<Recipe> mealPlanRecipes = mealPlan.getRecipes();

        // Builds the output data and sends it to the presenter
        final MealPlanOutputData mealPlanOutputData = new MealPlanOutputData.Builder()
                .buildRecipeNames(mealPlanRecipes)
                .buildRecipeImages(mealPlanRecipes)
                .buildRecipeIngredients(mealPlanRecipes)
                .buildRecipeNutritionalValues(mealPlanRecipes)
                .build();
        loadMealPlanPresenter.prepareSuccessView(mealPlanOutputData);
    }
}
