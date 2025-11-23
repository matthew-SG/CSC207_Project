package use_case.load_meal_plan;

/**
 * Output Data for the Load Meal Plan Use Case
 */
public class LoadMealPlanOutputData {

    private final String firstRecipeName;
    private final double targetCalories;
    private final double targetProtein;
    private final double targetCarbs;
    private final double targetFats;

    public LoadMealPlanOutputData(String firstRecipeName, double targetCalories, double targetProtein,
                                  double targetCarbs, double targetFats) {
        this.firstRecipeName = firstRecipeName;
        this.targetCalories = targetCalories;
        this.targetProtein = targetProtein;
        this.targetCarbs = targetCarbs;
        this.targetFats = targetFats;
    }

    public String getFirstRecipeName() {
        return firstRecipeName;
    }

    public double getTargetCalories() {
        return targetCalories;
    }

    public double getTargetProtein() {
        return targetProtein;
    }

    public double getTargetCarbs() {
        return targetCarbs;
    }

    public double getTargetFats() {
        return targetFats;
    }
}
