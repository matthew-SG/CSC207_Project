package use_case.load_meal_plan;

import java.util.List;

/**
 * Output Data for the View Meal Plans Use Case
 */
public class ViewMealPlansOutputData {

    private final List<String> firstRecipeNames;
    private final List<Double> targetCalories;
    private final List<Double> targetProtein;
    private final List<Double> targetCarbs;
    private final List<Double> targetFats;

    public ViewMealPlansOutputData(List<String> firstRecipeName, List<Double> targetCalories, List<Double> targetProtein,
                                   List<Double> targetCarbs, List<Double> targetFats) {
        this.firstRecipeNames = firstRecipeName;
        this.targetCalories = targetCalories;
        this.targetProtein = targetProtein;
        this.targetCarbs = targetCarbs;
        this.targetFats = targetFats;
    }

    public List<String> getFirstRecipeNames() {
        return firstRecipeNames;
    }

    public List<Double> getTargetCalories() {
        return targetCalories;
    }

    public List<Double> getTargetProtein() {
        return targetProtein;
    }

    public List<Double> getTargetCarbs() {
        return targetCarbs;
    }

    public List<Double> getTargetFats() {
        return targetFats;
    }
}
