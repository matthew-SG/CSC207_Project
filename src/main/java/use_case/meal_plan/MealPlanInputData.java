package use_case.meal_plan;

/**
 * The Input Data for the Meal Plan Use Case.
 */
public class MealPlanInputData {

    private final String targetCalories;
    private final String targetProtein;
    private final String targetCarbs;
    private final String targetFats;

    public MealPlanInputData(String targetCalories, String targetProtein, String targetCarbs, String targetFats) {
        this.targetCalories = targetCalories;
        this.targetProtein = targetProtein;
        this.targetCarbs = targetCarbs;
        this.targetFats = targetFats;
    }

    String getTargetCalories() {
        return targetCalories;
    }

    String getTargetProtein() {
        return targetProtein;
    }

    String getTargetCarbs() {
        return targetCarbs;
    }

    String getTargetFats() {
        return targetFats;
    }
}
