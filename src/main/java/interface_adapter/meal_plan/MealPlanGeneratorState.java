package interface_adapter.meal_plan;

/**
 * The state for the MealPlanGenerator ViewModel
 */
public class MealPlanGeneratorState {
    private String targetCalories = "";
    private String caloriesError;
    private String targetProtein = "";
    private String proteinError;
    private String targetCarbs = "";
    private String carbsError;
    private String targetFats = "";
    private String fatsError;

    public String getTargetCalories() {
        return targetCalories;
    }

    public void setTargetCalories(String targetCalories) { this.targetCalories = targetCalories; }

    public String getCaloriesError() { return caloriesError; }

    public void setCaloriesError(String caloriesError) { this.caloriesError = caloriesError; }

    public String getTargetProtein() { return targetProtein; }

    public void setTargetProtein(String targetProtein) { this.targetProtein = targetProtein; }

    public String getProteinError() { return proteinError; }

    public void setProteinError(String proteinError) { this.proteinError = proteinError; }

    public String getTargetCarbs() { return targetCarbs; }

    public void setTargetCarbs(String targetCarbs) { this.targetCarbs = targetCarbs; }

    public String getCarbsError() { return carbsError; }

    public void setCarbsError(String carbsError) { this.carbsError = carbsError; }

    public String getTargetFats() { return targetFats; }

    public void setTargetFats(String targetFats) { this.targetFats = targetFats; }

    public String getFatsError() { return fatsError; }

    public void setFatsError(String fatsError) {  this.fatsError = fatsError; }
}
