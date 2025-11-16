package use_case.meal_plan;

/**
 * The Input Data for the Meal Plan Use Case
 */
public class MealPlanInputData {

    private final double targetCalories;
    private final double targetProtein;
    private final double targetCarbs;
    private final double targetFats;

    public MealPlanInputData(double targetCalories, double targetProtein, double targetCarbs, double targetFats) {
        this.targetCalories = targetCalories;
        this.targetProtein = targetProtein;
        this.targetCarbs = targetCarbs;
        this.targetFats = targetFats;
    }

    double getTargetCalories() {return  targetCalories;}

    double getTargetProtein() {return  targetProtein;}

    double getTargetCarbs() {return  targetCarbs;}

    double getTargetFats() {return  targetFats;}
}
