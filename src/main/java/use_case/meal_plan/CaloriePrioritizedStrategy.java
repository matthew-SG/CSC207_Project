package use_case.meal_plan;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import entities.Recipe;

/**
 * Strategy for meal plan generator that prioritizes getting as close to the target calories as possible.
 * Calories are weighted more heavily than other nutrients (protein, carbs, fats).
 * This strategy is useful for users who primarily focus on calorie intake for their diet.
 */
public class CaloriePrioritizedStrategy implements MealPlanGeneratorStrategy {
    private static final double CALORIE_WEIGHT = 3.0;

    @Override
    public List<Recipe> generateMealPlan(List<List<Recipe>> recipeTriplets, double targetCalories, double targetProtein,
                                         double targetCarbs, double targetFats) {
        double currentCalories;
        double currentProtein;
        double currentCarbs;
        double currentFats;
        double currentError;
        double lowestError = -1;
        List<Recipe> result = new ArrayList<>();

        for (List<Recipe> recipeTriplet : recipeTriplets) {
            currentCalories = 0;
            currentProtein = 0;
            currentCarbs = 0;
            currentFats = 0;

            for (Recipe recipe : recipeTriplet) {
                final Map<String, Double> recipeNutritionalValues = recipe.getNutritionalValues();
                currentCalories += recipeNutritionalValues.get("Calories");
                currentProtein += recipeNutritionalValues.get("Protein");
                currentCarbs += recipeNutritionalValues.get("Carbohydrates");
                currentFats += recipeNutritionalValues.get("Fat");
            }
            // Calories are weighted more heavily (3x) compared to other nutrients
            currentError = CALORIE_WEIGHT * Math.abs(currentCalories - targetCalories)
                    + Math.abs(currentProtein - targetProtein)
                    + Math.abs(currentCarbs - targetCarbs)
                    + Math.abs(currentFats - targetFats);
            if (currentError == 0) {
                result = recipeTriplet;
                break;
            }
            else if (lowestError == -1 || currentError < lowestError) {
                lowestError = currentError;
                result = recipeTriplet;
            }
        }
        return result;
    }
}
