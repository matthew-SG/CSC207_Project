package interface_adapter.delete_meal_plan;

import use_case.delete_meal_plan.DeleteMealPlanInputBoundary;
import use_case.delete_meal_plan.DeleteMealPlanInputData;

/**
 * Controller for the delete meal plan use case
 */
public class DeleteMealPlanController {
    private final DeleteMealPlanInputBoundary deleteMealPlanInteractor;

    public DeleteMealPlanController(DeleteMealPlanInputBoundary deleteMealPlanInteractor) {
        this.deleteMealPlanInteractor = deleteMealPlanInteractor;
    }

    /**
     * Executes the delete meal plan use case
     * @param index index of the meal plan to be deleted in the user's list of meal plans
     */
    public void execute(int index) {
        DeleteMealPlanInputData deleteMealPlanInputData = new DeleteMealPlanInputData(index);

        deleteMealPlanInteractor.execute(deleteMealPlanInputData);
    }
}
