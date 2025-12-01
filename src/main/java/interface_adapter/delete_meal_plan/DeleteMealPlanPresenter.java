package interface_adapter.delete_meal_plan;

import interface_adapter.view_meal_plans.ViewMealPlansState;
import interface_adapter.view_meal_plans.ViewMealPlansViewModel;
import use_case.delete_meal_plan.DeleteMealPlanOutputBoundary;

/**
 * Presenter for the delete meal plan use case
 */
public class DeleteMealPlanPresenter implements DeleteMealPlanOutputBoundary {
    private final ViewMealPlansViewModel viewMealPlansViewModel;

    public DeleteMealPlanPresenter(ViewMealPlansViewModel viewMealPlansViewModel) {
        this.viewMealPlansViewModel = viewMealPlansViewModel;
    }

    @Override
    public void prepareFailureView(String error) {
        final ViewMealPlansState viewMealPlansState = viewMealPlansViewModel.getState();
        viewMealPlansState.setOneMealPlanError(error);
        viewMealPlansViewModel.firePropertyChange();

    }
}
