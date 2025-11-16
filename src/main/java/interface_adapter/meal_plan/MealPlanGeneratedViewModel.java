package interface_adapter.meal_plan;

import interface_adapter.ViewModel;

/**
 * The View Model for the generated Meal Plans
 */
public class MealPlanGeneratedViewModel extends ViewModel<MealPlanState> {

    public MealPlanGeneratedViewModel() {
        super("meal plan generated");
        setState(new MealPlanState());
    }
}
