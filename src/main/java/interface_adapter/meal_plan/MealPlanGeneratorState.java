package interface_adapter.meal_plan;

/**
 * The state for the MealPlanGenerator ViewModel
 */
public class MealPlanGeneratorState {
    private String targetCalories = "";
    private String targetProtein = "";
    private String targetCarbs = "";
    private String targetFats = "";

    public String getTargetCalories() {
        return targetCalories;
    }

    public void setTargetCalories(String targetCalories) { this.targetCalories = targetCalories; }

    public String getTargetProtein() { return targetProtein; }

    public void setTargetProtein(String targetProtein) { this.targetProtein = targetProtein; }

    public String getTargetCarbs() { return targetCarbs; }

    public void setTargetCarbs(String targetCarbs) { this.targetCarbs = targetCarbs; }

    public String getTargetFats() { return targetFats; }

    public void setTargetFats(String targetFats) { this.targetFats = targetFats; }
}
