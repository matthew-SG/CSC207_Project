package entities;

public class MealPlan {

    private final int targetCalories;
    private final int targetProtein;
    private final int targetPrice;

    public MealPlan(int targetCalories, int targetProtein, int targetPrice) {
        this.targetCalories = targetCalories;
        this.targetProtein = targetProtein;
        this.targetPrice = targetPrice;
    }

    public int getTargetCalories() {
        return this.targetCalories;
    }

    public int getTargetProtein() {
        return this.targetProtein;
    }

    public int getTargetPrice() {
        return this.targetPrice;
    }
}
