package interface_adapter.meal_plan;

/**
 * The state for the MealPlanGenerator ViewModel
 */
public class MealPlanGeneratorState {
    private String targetCalories = "";
    private String targetProtein = "";
    private String targetCarbs = "";
    private String targetFats = "";
    private String insufficientRecipesError;
    private String inputsError;
    private String noMealPlansError;

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

    public String getInsufficientRecipesError() { return insufficientRecipesError; }

    public void setInsufficientRecipesError(String insufficientRecipesError) {
        this.insufficientRecipesError = insufficientRecipesError;
    }

    public String getInputsError()  { return inputsError; }

    public void setInputsError(String inputsError) {
        this.inputsError = inputsError;
    }

    public String getNoMealPlansError() { return noMealPlansError; }

    public void setNoMealPlansError(String noMealPlansError) {
        this.noMealPlansError = noMealPlansError;
    }
}
