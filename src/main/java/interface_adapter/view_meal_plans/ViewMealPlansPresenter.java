package interface_adapter.view_meal_plans;

import interface_adapter.ViewManagerModel;
import interface_adapter.meal_plan.MealPlanGeneratorState;
import interface_adapter.meal_plan.MealPlanGeneratorViewModel;
import use_case.view_meal_plans.ViewMealPlansOutputBoundary;
import use_case.view_meal_plans.ViewMealPlansOutputData;

/**
 * Presenter for the View Meal Plans Use Case
 */
public class ViewMealPlansPresenter implements ViewMealPlansOutputBoundary {
    private final MealPlanGeneratorViewModel mealPlanGeneratorViewModel;
    private final ViewMealPlansViewModel viewMealPlansViewModel;
    private final ViewManagerModel viewManagerModel;

    public ViewMealPlansPresenter(MealPlanGeneratorViewModel mealPlanGeneratorViewModel,
                                  ViewMealPlansViewModel viewMealPlansViewModel, ViewManagerModel viewManagerModel) {
        this.mealPlanGeneratorViewModel = mealPlanGeneratorViewModel;
        this.viewMealPlansViewModel = viewMealPlansViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(ViewMealPlansOutputData response) {
        // On success, swap to the list of the user's saved meal plans
        final ViewMealPlansState viewMealPlansState = viewMealPlansViewModel.getState();
        viewMealPlansState.setFirstRecipeNames(response.getFirstRecipeNames());
        viewMealPlansState.setTargetCalories(response.getTargetCalories());
        viewMealPlansState.setTargetProtein(response.getTargetProtein());
        viewMealPlansState.setTargetCarbs(response.getTargetCarbs());
        viewMealPlansState.setTargetFats(response.getTargetFats());
        viewMealPlansViewModel.firePropertyChange();

        viewManagerModel.getState().viewName =  mealPlanGeneratorViewModel.getViewName();
        viewManagerModel.firePropertyChange();
    }

    public void prepareFailView(String error) {
        final MealPlanGeneratorState mealPlanGeneratorState = mealPlanGeneratorViewModel.getState();
        mealPlanGeneratorState.setNoMealPlansError(error);
        mealPlanGeneratorViewModel.firePropertyChange();
    }


}
