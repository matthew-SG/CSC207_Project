package use_case.meal_plan;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import entities.Recipe;

/**
 * Strategy for meal plan generator that prioritizes having the error between the target carbs and the meal plan
 *      carbs being as low as possible.
 */
public class PrioritizeCarbsErrorStrategy implements MealPlanGeneratorStrategy {
    @Override
    public List<Recipe> generateMealPlan(List<List<Recipe>> recipeTriplets, double targetCalories, double targetProtein,
                                         double targetCarbs, double targetFats) {
        // The weight that the carbs have on the error
        final int carbsWeight = 3;

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
            // Weighs the sum more towards the error of the calories
            currentError = Math.abs(currentCalories - targetCalories)
                    + Math.abs(currentProtein - targetProtein) + carbsWeight * (Math.abs(currentCarbs - targetCarbs)
                    + Math.abs(currentFats - targetFats));
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
