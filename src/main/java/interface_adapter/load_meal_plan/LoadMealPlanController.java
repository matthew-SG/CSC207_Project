package interface_adapter.load_meal_plan;

import use_case.load_meal_plan.LoadMealPlanInputBoundary;
import use_case.load_meal_plan.LoadMealPlanInputData;

/**
 * Controller for the Load Meal Plan Use Case
 */
public class LoadMealPlanController {
    private final LoadMealPlanInputBoundary loadMealPlanIntereactor;

    public LoadMealPlanController(LoadMealPlanInputBoundary loadMealPlanInteractor) {
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
