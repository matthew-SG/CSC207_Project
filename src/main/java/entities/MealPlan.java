package entities;

import java.util.List;

public class MealPlan {

    private final List<Recipe> recipes;
    private final int targetCalories;
    private final int targetProtein;
    private final int targetCarbs;
    private final int targetFats;

    public MealPlan(List<Recipe> recipes, int targetCalories, int targetProtein, int targetCarbs, int targetFats) {
        this.recipes = recipes;
        this.targetCalories = targetCalories;
        this.targetProtein = targetProtein;
        this.targetCarbs = targetCarbs;
        this.targetFats = targetFats;
    }

    public int getTargetCalories() {
        return this.targetCalories;
    }

    public int getTargetProtein() {
        return this.targetProtein;
    }

    public int getTargetCarbs() { return this.targetCarbs; }

    public int getTargetFats() { return this.targetFats; }

    public List<Recipe> getRecipes() { return this.recipes; }
}
