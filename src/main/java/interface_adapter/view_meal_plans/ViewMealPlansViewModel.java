package interface_adapter.view_meal_plans;

import interface_adapter.ViewModel;

/**
 * View Model for the View Meal Plans Use Case
 */
public class ViewMealPlansViewModel extends ViewModel<ViewMealPlansState> {

    public ViewMealPlansViewModel() {
        super("view meal plans");
        setState(new ViewMealPlansState());
    }
}
