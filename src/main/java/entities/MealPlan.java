package entities;

import java.util.List;

/**
 * Class representing the Meal Plan entity.
 */
public class MealPlan {

    private final List<Recipe> recipes;
    private final double targetCalories;
    private final double targetProtein;
    private final double targetCarbs;
    private final double targetFats;

    public MealPlan(List<Recipe> recipes, double targetCalories, double targetProtein, double targetCarbs,
                    double targetFats) {
        this.recipes = recipes;
        this.targetCalories = targetCalories;
        this.targetProtein = targetProtein;
        this.targetCarbs = targetCarbs;
        this.targetFats = targetFats;
    }

    public double getTargetCalories() {
        return this.targetCalories;
    }

    public double getTargetProtein() {
        return this.targetProtein;
    }

    public double getTargetCarbs() {
        return this.targetCarbs;
    }

    public double getTargetFats() {
        return this.targetFats;
    }

    public List<Recipe> getRecipes() {
        return this.recipes;
    }
}
