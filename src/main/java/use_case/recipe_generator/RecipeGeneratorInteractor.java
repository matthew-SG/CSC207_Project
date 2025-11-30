package use_case.recipe_generator;
import java.util.ArrayList;

import entities.Cuisine;
import entities.DietaryRestriction;
import entities.Intolerance;
import  entities.Recipe;
import java.util.List;

// only depends on the entities and data access interfaces, not Swing etc.
// implementation of the use case (business logic)
// Clean Architecture: This class implements the business logic such as which filters matter, when to show a message for API error or when no recipes are found etc.
// this only depends on interfaces such as RecipeDataAccessInterface for data and RecipeGeneratorOutputBoundary for output
// independent of json data or Spoonacular, essentially isolates data access interfaces from UI or infrastructure so other layers can change without touching core logic


public class RecipeGeneratorInteractor implements RecipeGeneratorInputBoundary{
    private final RecipeDataAccessInterface userRecipeAccessObject; // port to the DAO which communicates with API
    private final RecipeGeneratorOutputBoundary recipePresenter; // port to the UI layer (presenter that updates the view models)

    public RecipeGeneratorInteractor(RecipeDataAccessInterface userRecipeAccessObject, RecipeGeneratorOutputBoundary recipePresenter) {
        this.userRecipeAccessObject = userRecipeAccessObject;
        this.recipePresenter = recipePresenter;
    }
    // main use case method
    @Override
    public void generateRecipes(GenerateRecipeInputData inputData){
        // first we extract all the filters from input data from the input data the controller sends
        DietaryRestriction dietRestriction = inputData.getDietaryRestriction();
        List<Intolerance> intolerances = inputData.getIntolerances();
        Cuisine cuisine = inputData.getCuisine();
        Integer minCalories = inputData.getMinCalories();
        Integer maxCalories = inputData.getMaxCalories();
        Integer minProtein = inputData.getMinProtein();
        Integer maxProtein = inputData.getMaxProtein();

        List<Recipe> recipes;
        // try and catch basically any exception thrown by the DAO is caught here such as the API failure path
        // basically concrete class hits Spoonacular, parses the JSON and returns a List<Recipes>
        // the exception is caught here instead of the DAO so the interactor can convert the error into a user readable message
        try {
            // calls the DAO interface method with the filters we have
            recipes = userRecipeAccessObject.getRecipes(
                    dietRestriction,
                    intolerances,
                    cuisine,
                    minCalories,
                    maxCalories,
                    minProtein,
                    maxProtein
            );
        } catch (Exception _) {
            List<RecipeSummary> emptySummaries = new ArrayList<>(); // on errors there is no usable recipe list so you construct an empty list of summaries

            String errorMessage = "Could not load recipes right now. Please try again later."; // message for API Failures

            GenerateRecipeOutputData outputData = new GenerateRecipeOutputData(emptySummaries, errorMessage);
            recipePresenter.prepareView(outputData); // calls the presenter via RecipeGeneratorOutputBoundary so it can update the view model and call the .showsErrorMessage method
            return;
        }
        // handles case were no recipes were found given the selected filter inputs the user made
        if (recipes == null || recipes.isEmpty()) {
            List<RecipeSummary> emptySummaries = new ArrayList<>();
            String message = "No recipes found, please try different filter options";

            GenerateRecipeOutputData outputData =
                    new GenerateRecipeOutputData(emptySummaries, message);
            recipePresenter.prepareView(outputData);
            return;
        }
        // finally this is reached when no exception or recipes is a non-empty list (meaning there are actually recipes to show)
        List<RecipeSummary> recipeSummaryList = new ArrayList<>();
        // loop over each of the recipes returned by the DAO
        // extract the required recipe information such as id name etc
        for (Recipe recipe : recipes) {
            RecipeSummary recipeX = new RecipeSummary(
                    recipe.getRecipeName(),
                    recipe.getRecipeId(),
                    recipe.getRecipeImage()
            );
            recipeSummaryList.add(recipeX); // add each of these recipeSummaries to the summary list
        }
        // the recipes were successfully processed meaning there should be no error message displayed to the user
        GenerateRecipeOutputData outputData =
                new GenerateRecipeOutputData(recipeSummaryList, "");
        recipePresenter.prepareView(outputData); // we have to notify the presenter that the use case succeeded (updating the view model etc.)
    }

}


