package interface_adapter.meal_plan;

import interface_adapter.ViewManagerModel;
import use_case.meal_plan.MealPlanOutputBoundary;
import use_case.meal_plan.MealPlanOutputData;

/**
 * Presenter for the Meal Plan Use Case
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

        viewManagerModel.setState(mealPlanGeneratedViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    public void prepareFailView(String caloriesError, String proteinError, String carbsError, String fatsError) {
        final MealPlanGeneratorState mealPlanGeneratorState = mealPlanGeneratorViewModel.getState();
        mealPlanGeneratorState.setCaloriesError(caloriesError);
        mealPlanGeneratorState.setProteinError(proteinError);
        mealPlanGeneratorState.setCarbsError(carbsError);
        mealPlanGeneratorState.setFatsError(fatsError);
        mealPlanGeneratorViewModel.firePropertyChange();
    }
}
