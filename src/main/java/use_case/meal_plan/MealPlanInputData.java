package use_case.meal_plan;

/**
 * The Input Data for the Meal Plan Use Case
 */
public class MealPlanInputData {

    private final int targetCalories;
    private final int targetProtein;
    private final int targetCarbs;
    private final int targetFats;

    public MealPlanInputData(int targetCalories, int targetProtein, int targetCarbs, int targetFats) {
        this.targetCalories = targetCalories;
        this.targetProtein = targetProtein;
        this.targetCarbs = targetCarbs;
        this.targetFats = targetFats;
    }

    int getTargetCalories() {return  targetCalories;}

    int getTargetProtein() {return  targetProtein;}

    int getTargetCarbs() {return  targetCarbs;}

    int getTargetFats() {return  targetFats;}
}
