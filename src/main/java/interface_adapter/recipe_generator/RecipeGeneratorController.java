package interface_adapter.recipe_generator;

import entities.DietaryRestriction;
import entities.Intolerance;
import entities.Cuisine;
import use_case.recipe_generator.GenerateRecipeInputData;
import use_case.recipe_generator.RecipeGeneratorInputBoundary;
import java.util.List;

public class RecipeGeneratorController {
    private final RecipeGeneratorInputBoundary recipeUseCaseInteractor;

    public RecipeGeneratorController(RecipeGeneratorInputBoundary recipeUseCaseInteractor) {
        this.recipeUseCaseInteractor = recipeUseCaseInteractor;
    }
    public void generateRecipe(
        DietaryRestriction dietaryRestriction, List<Intolerance> intolerances, Cuisine cuisine, String minCaloriesText, String maxCaloriesText, String minProteinText, String maxProteinText) {
        Integer minCalories = null;
        Integer maxCalories = null;
        Integer minProtein = null;
        Integer maxProtein = null;
        /**
         * catch handles if user inputs alphabetical values into the calories or protein field
         */
        try {
            if (minCaloriesText != null && !minCaloriesText.isEmpty()) {
                minCalories = Integer.parseInt(minCaloriesText);
            }
            if (maxCaloriesText != null && !maxCaloriesText.isEmpty()) {
                maxCalories = Integer.parseInt(maxCaloriesText);
            }
            if (minProteinText != null && !minProteinText.isEmpty()) {
                minProtein = Integer.parseInt(minProteinText);
            }
            if (maxProteinText != null && !maxProteinText.isEmpty()) {
                maxProtein = Integer.parseInt(maxProteinText);
            }
        } catch (NumberFormatException e) {
            return;
        }
        GenerateRecipeInputData inputData = new GenerateRecipeInputData(dietaryRestriction, intolerances, cuisine, minCalories, maxCalories, minProtein,  maxProtein);
        recipeUseCaseInteractor.generateRecipes(inputData);

    }
}
