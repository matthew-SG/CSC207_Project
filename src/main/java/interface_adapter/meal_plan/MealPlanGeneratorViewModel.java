package interface_adapter.meal_plan;

import interface_adapter.ViewModel;

/**
 * The View Model for the Meal Plan generator
 */
public class MealPlanGeneratorViewModel extends ViewModel<MealPlanGeneratorState> {

    private static final String TITLE_LABEL = "Meal Plan Generator View";
    private static final String CALORIES_LABEL = "Enter target calories for the day";
    private static final String PROTEIN_LABEL = "Enter target protein for the day (in grams)";
    private static final String CARBS_LABEL = "Enter target carbs for the day (in grams)";
    private static final String FATS_LABEL =  "Enter target fats for the day (in grams)";

    private static final String GENERATE_LABEL = "Generate";

    public MealPlanGeneratorViewModel() {
        super("meal plan generator");
        setState(new MealPlanGeneratorState());
    }
}
