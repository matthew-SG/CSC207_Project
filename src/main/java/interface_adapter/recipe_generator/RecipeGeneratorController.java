package interface_adapter.recipe_generator;

import entities.DietaryRestriction;
import entities.Intolerance;
import entities.Cuisine;
import use_case.recipe_generator.GenerateRecipeInputData;
import use_case.recipe_generator.RecipeGeneratorInputBoundary;
import java.util.List;


// the controller is responsible for type conversion from String UI values to Integers
// Clean Architecture: this controller is an interface adapter that converts UI input into a GenerateRecipeInputData request and calls the RecipeGeneratorInputBoundary use case.

public class RecipeGeneratorController {
    private final RecipeGeneratorInputBoundary recipeUseCaseInteractor;

    public RecipeGeneratorController(RecipeGeneratorInputBoundary recipeUseCaseInteractor) {
        this.recipeUseCaseInteractor = recipeUseCaseInteractor;
    }
    // when the user clicks generate recipe on the UI this is the method that the view calls
    // its job is to convert raw UI values into a proper Input object for the interactor
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
            if (minCaloriesText != null && !minCaloriesText.trim().isEmpty()) {
                minCalories = Integer.parseInt(minCaloriesText.trim());
            }
            if (maxCaloriesText != null && !maxCaloriesText.trim().isEmpty()) {
                maxCalories = Integer.parseInt(maxCaloriesText.trim());
            }
            if (minProteinText != null && !minProteinText.trim().isEmpty()) {
                minProtein = Integer.parseInt(minProteinText.trim());
            }
            if (maxProteinText != null && !maxProteinText.trim().isEmpty()) {
                maxProtein = Integer.parseInt(maxProteinText.trim());
            }
        } catch (NumberFormatException e) {
            // Ideally notify user, but for now just return to avoid crash
            System.out.println("Invalid number format: " + e.getMessage());
            return;
        }
        GenerateRecipeInputData inputData = new GenerateRecipeInputData(dietaryRestriction, intolerances, cuisine, minCalories, maxCalories, minProtein,  maxProtein);
        recipeUseCaseInteractor.generateRecipes(inputData); // this now actually calls the interactor with the processed input data as needed (lets the interactor/presenter handle view model updates

    }
}
