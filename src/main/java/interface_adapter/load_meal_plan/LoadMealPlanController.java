package interface_adapter.load_meal_plan;

import use_case.load_meal_plan.LoadMealPlanInputData;
import use_case.load_meal_plan.LoadMealPlanInteractor;

/**
 * Controller for the Load Meal Plan Use Case
 */
public class LoadMealPlanController {
    private final LoadMealPlanInteractor loadMealPlanIntereactor;

    public LoadMealPlanController(LoadMealPlanInteractor loadMealPlanInteractor) {
        this.loadMealPlanIntereactor = loadMealPlanInteractor;
    }

    /**
     * Executes the Load Meal Plan Use Case
     * @param index the index of the recipe
     */
    public void execute(int index) {
        LoadMealPlanInputData loadMealPlanInputData = new LoadMealPlanInputData(index);

        loadMealPlanIntereactor.execute(loadMealPlanInputData);
    }
}
