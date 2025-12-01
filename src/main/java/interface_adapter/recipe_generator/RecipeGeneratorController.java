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

        // no need to worry about invalid inputs (ie alphabetical inputs as that is already processed in RecipeGeneratorView
        // convert from strings to Integers (the type GenerateRecipeInputData expects)
            if (minCaloriesText != null && !minCaloriesText.isEmpty()) {
                minCalories = Integer.parseInt(minCaloriesText); // only parse if the field actually has something in it
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

        // after parsing to identify invalid inputs create an input DTO that the interactor expects (a GenerateRecipeInputData object)
        GenerateRecipeInputData inputData = new GenerateRecipeInputData(dietaryRestriction, intolerances, cuisine, minCalories, maxCalories, minProtein,  maxProtein);
        recipeUseCaseInteractor.generateRecipes(inputData); // this now actually calls the interactor with the processed input data as needed (lets the interactor/presenter handle view model updates

    }
}
