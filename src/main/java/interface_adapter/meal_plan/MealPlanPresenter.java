package interface_adapter.meal_plan;

import interface_adapter.ViewManagerModel;
import use_case.meal_plan.MealPlanOutputBoundary;
import use_case.meal_plan.MealPlanOutputData;

/**
 * Presenter for the Meal Plan Use Case.
 */
public class MealPlanPresenter implements MealPlanOutputBoundary {

    private final MealPlanGeneratorViewModel mealPlanGeneratorViewModel;
    private final MealPlanGeneratedViewModel mealPlanGeneratedViewModel;
    private final ViewManagerModel viewManagerModel;

    public MealPlanPresenter(MealPlanGeneratorViewModel mealPlanGeneratorViewModel,
                             MealPlanGeneratedViewModel mealPlanGeneratedViewModel1,
                             ViewManagerModel viewManagerModel) {
        this.mealPlanGeneratorViewModel = mealPlanGeneratorViewModel;
        this.mealPlanGeneratedViewModel = mealPlanGeneratedViewModel1;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(MealPlanOutputData response) {
        // On success, swap to the generated meal plan view
        final MealPlanGeneratedState mealPlanGeneratedState = mealPlanGeneratedViewModel.getState();
        mealPlanGeneratedState.setRecipeImages(response.getRecipeImages());
        mealPlanGeneratedState.setRecipeNames(response.getRecipeNames());
        mealPlanGeneratedState.setRecipeIngredients(response.getIngredients());
        mealPlanGeneratedState.setRecipeNutritionalValues(response.getNutritionalValues());
        mealPlanGeneratedViewModel.firePropertyChange();

        viewManagerModel.getState().viewName = mealPlanGeneratedViewModel.getViewName();
        viewManagerModel.firePropertyChange();
    }

    /**
     * Prepares the fail view for the meal plan generator use case.
     * @param listError the error to be displayed due to the saved recipes
     * @param inputError the error to be displayed due to the input
     */
    public void prepareFailView(String listError, String inputError) {
        final MealPlanGeneratorState mealPlanGeneratorState = mealPlanGeneratorViewModel.getState();
        mealPlanGeneratorState.setInsufficientRecipesError(listError);
        mealPlanGeneratorState.setInputsError(inputError);
        mealPlanGeneratorViewModel.firePropertyChange();
    }
}
