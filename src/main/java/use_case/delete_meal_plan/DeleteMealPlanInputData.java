package use_case.delete_meal_plan;

/**
 * Input data for the delete meal plan use case
 */
public class DeleteMealPlanInputData {
    private final int index;

    public DeleteMealPlanInputData(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
