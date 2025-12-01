package use_case.load_meal_plan;

/**
 * Input data for the Load Meal Plan Use Case.
 */
public class LoadMealPlanInputData {
    private final int index;

    public LoadMealPlanInputData(int index) {
        this.index = index;
    }

    int getIndex() {
        return index;
    }
}
