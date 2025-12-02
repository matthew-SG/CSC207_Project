package use_case.meal_plan;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import entities.Recipe;

/**
 * Strategy for meal plan generator that focuses on getting the total error from the target nutrients as low as
 *      possible, with no nutrient being weighed heavier than the others.
 */
public class LowestNutritionalErrorStrategy implements MealPlanGeneratorStrategy {

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
            currentError = Math.abs(currentCalories - targetCalories) + Math.abs(currentProtein - targetProtein) + (
                    Math.abs(currentCarbs - targetCarbs) + Math.abs(currentFats - targetFats));
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
