package use_case.meal_plan;

import java.util.List;

import entities.Recipe;

/**
 * Strategy interface for the methods at which the meal plans are generated from a user's liked recipe.
 */
public interface MealPlanGeneratorStrategy {

    /**
     * The generates the three recipes for the meal plan for the user according to the implemented strategy.
     * @param recipeTriplets the list of unique combinations of recipe triplets from the user's liked recips
     * @param targetCalories the target calories of the meal plan
     * @param targetProtein the target protein of the meal plan
     * @param targetCarbs the target carbs of the meal plan
     * @param targetFats the target fat of the meal plan
     * @return the three selected recipes for the meal plan
     */
    List<Recipe> generateMealPlan(List<List<Recipe>> recipeTriplets, double targetCalories, double targetProtein,
                                  double targetCarbs, double targetFats);
}
