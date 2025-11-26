package interface_adapter.load_meal_plan;

import interface_adapter.ViewManagerModel;
import interface_adapter.meal_plan.MealPlanGeneratedState;
import interface_adapter.meal_plan.MealPlanGeneratedViewModel;
import use_case.load_meal_plan.LoadMealPlanOutputBoundary;
import use_case.meal_plan.MealPlanOutputData;

/**
 * Presenter for the Load Meal Plan Use Case
 */
public class LoadMealPlanPresenter implements LoadMealPlanOutputBoundary {
    private final MealPlanGeneratedViewModel mealPlanGeneratedViewModel;
    private final ViewManagerModel viewManagerModel;

    public LoadMealPlanPresenter(MealPlanGeneratedViewModel mealPlanGeneratedViewModel, ViewManagerModel viewManagerModel) {
        this.mealPlanGeneratedViewModel = mealPlanGeneratedViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    public void prepareSuccessView(MealPlanOutputData response) {
        // On success, swap to the Meal Plan Generated View
        final MealPlanGeneratedState mealPlanGeneratedState = mealPlanGeneratedViewModel.getState();
        mealPlanGeneratedState.setRecipeNames(response.getRecipeNames());
        mealPlanGeneratedState.setRecipeImages(response.getRecipeImages());
        mealPlanGeneratedState.setRecipeIngredients(response.getIngredients());
        mealPlanGeneratedState.setRecipeNutritionalValues(response.getNutritionalValues());
        mealPlanGeneratedViewModel.firePropertyChange();

        viewManagerModel.getState().viewName =  mealPlanGeneratedViewModel.getViewName();
        viewManagerModel.firePropertyChange();
    }
}
