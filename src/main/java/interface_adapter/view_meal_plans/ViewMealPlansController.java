package interface_adapter.view_meal_plans;

import use_case.view_meal_plans.ViewMealPlansInputBoundary;

/**
 * Controller for the View Meal Plans Use Case
 */
public class ViewMealPlansController {
    private final ViewMealPlansInputBoundary viewMealPlansInteractor;

    public ViewMealPlansController(ViewMealPlansInputBoundary viewMealPlansInteractor) {
        this.viewMealPlansInteractor = viewMealPlansInteractor;
    }

    public void execute() {
        viewMealPlansInteractor.execute();
    }

}
