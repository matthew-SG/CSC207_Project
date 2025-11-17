package interface_adapter.meal_plan;

import interface_adapter.ViewModel;

/**
 * The View Model for the Meal Plan generator
 */
public class MealPlanGeneratorViewModel extends ViewModel<MealPlanGeneratorState> {

    public MealPlanGeneratorViewModel() {
        super("meal plan generator");
        setState(new MealPlanGeneratorState());
    }
}
