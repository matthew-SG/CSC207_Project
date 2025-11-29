package interface_adapter.view_meal_plans;

import java.util.ArrayList;
import java.util.List;

/**
 * The state for the ViewMealPlans ViewModel
 */
public class ViewMealPlansState {
    private List<String> firstRecipeNames = new ArrayList<>();
    private List<Double> targetCalories = new ArrayList<>();
    private List<Double> targetProtein = new ArrayList<>();
    private List<Double> targetCarbs = new ArrayList<>();
    private List<Double> targetFats = new ArrayList<>();
    private String oneMealPlanError;

    public List<String> getFirstRecipeNames() {
        return firstRecipeNames;
    }

    public void setFirstRecipeNames(List<String> firstRecipeNames) {
        this.firstRecipeNames = firstRecipeNames;
    }

    public List<Double> getTargetCalories() {
        return targetCalories;
    }

    public void setTargetCalories(List<Double> targetCalories) {
        this.targetCalories = targetCalories;
    }

    public List<Double> getTargetProtein() {
        return targetProtein;
    }

    public void setTargetProtein(List<Double> targetProtein) {
        this.targetProtein = targetProtein;
    }

    public List<Double> getTargetCarbs() {
        return targetCarbs;
    }

    public void setTargetCarbs(List<Double> targetCarbs) {
        this.targetCarbs = targetCarbs;
    }

    public List<Double> getTargetFats() {
        return targetFats;
    }

    public void setTargetFats(List<Double> targetFats) {
        this.targetFats = targetFats;
    }

    public String getOneMealPlanError() {
        return oneMealPlanError;
    }

    public void setOneMealPlanError(String oneMealPlanError) {
        this.oneMealPlanError = oneMealPlanError;
    }
}
