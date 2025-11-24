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
        DietaryRestriction dietaryRestriction, List<Intolerance> intolerances, Cuisine cuisine, String maxCaloriesText, String minProteinText) {
        Integer maxCalories = null;
        Integer minProtein = null;
        /**
         * catch handles if user inputs alphabetical values into the calories or protein field
         */
        try {
            if (maxCaloriesText != null && !maxCaloriesText.isEmpty()) {
                maxCalories = Integer.parseInt(maxCaloriesText);
            }
            if (minProteinText != null && !minProteinText.isEmpty()) {
                minProtein = Integer.parseInt(minProteinText);
            }
        } catch (NumberFormatException e) {
            return;
        }
        GenerateRecipeInputData inputData = new GenerateRecipeInputData(dietaryRestriction, intolerances, cuisine, maxCalories, minProtein);
        recipeUseCaseInteractor.generateRecipes(inputData);

    }
}
