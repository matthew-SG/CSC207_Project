package entities;

/**
 * Represents a user's nutritional goals.
 */
public class NutritionGoal {
    private double targetCalories;
    private double targetProtein;
    private double targetCarbs;
    private double targetFat;

    /**
     * Creates a NutritionGoal with default values.
     */
    public NutritionGoal() {
        this.targetCalories = 2000.0;
        this.targetProtein = 50.0;
        this.targetCarbs = 250.0;
        this.targetFat = 70.0;
    }

    /**
     * Creates a NutritionGoal with specified values.
     */
    public NutritionGoal(double targetCalories, double targetProtein, double targetCarbs, double targetFat) {
        this.targetCalories = targetCalories;
        this.targetProtein = targetProtein;
        this.targetCarbs = targetCarbs;
        this.targetFat = targetFat;
    }

    public double getTargetCalories() {
        return targetCalories;
    }

    public void setTargetCalories(double targetCalories) {
        this.targetCalories = targetCalories;
    }

    public double getTargetProtein() {
        return targetProtein;
    }

    public void setTargetProtein(double targetProtein) {
        this.targetProtein = targetProtein;
    }

    public double getTargetCarbs() {
        return targetCarbs;
    }

    public void setTargetCarbs(double targetCarbs) {
        this.targetCarbs = targetCarbs;
    }

    public double getTargetFat() {
        return targetFat;
    }

    public void setTargetFat(double targetFat) {
        this.targetFat = targetFat;
    }
}
