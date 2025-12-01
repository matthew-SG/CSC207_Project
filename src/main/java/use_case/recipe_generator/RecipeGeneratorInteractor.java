package use_case.recipe_generator;

import java.util.ArrayList;
import java.util.List;

import entities.Cuisine;
import entities.DietaryRestriction;
import entities.Intolerance;
import entities.Recipe;
import use_case.approve_recipe.ApproveRecipeDataAccessInterface;

/**
 * Interactor for the recipe generator use case.
 */
public class RecipeGeneratorInteractor implements RecipeGeneratorInputBoundary {
    // DAI for Recipe Data
    private final RecipeDataAccessInterface userRecipeAccessObject;
    // Output Boundary for sending outputs to presenter
    private final RecipeGeneratorOutputBoundary recipePresenter;
    // DAI for storing avaialbe recipes for approval
    private final ApproveRecipeDataAccessInterface approveRecipeDataAccess;

    public RecipeGeneratorInteractor(RecipeDataAccessInterface userRecipeAccessObject,
                                     RecipeGeneratorOutputBoundary recipePresenter,
                                     ApproveRecipeDataAccessInterface approveRecipeDataAccess) {
        this.userRecipeAccessObject = userRecipeAccessObject;
        this.recipePresenter = recipePresenter;
        this.approveRecipeDataAccess = approveRecipeDataAccess;
    }

    @Override
    public void generateRecipes(GenerateRecipeInputData inputData) {

        final DietaryRestriction dietRestriction = inputData.getDietaryRestriction();
        final List<Intolerance> intolerances = inputData.getIntolerances();
        final Cuisine cuisine = inputData.getCuisine();
        final Integer minCalories = inputData.getMinCalories();
        final Integer maxCalories = inputData.getMaxCalories();
        final Integer minProtein = inputData.getMinProtein();
        final Integer maxProtein = inputData.getMaxProtein();

        final List<Recipe> recipes;

        // Handle any failure thrown by the DAO (timeouts, bad JSON, etc.)
        try {
            recipes = userRecipeAccessObject.getRecipes(
                    dietRestriction,
                    intolerances,
                    cuisine,
                    minCalories,
                    maxCalories,
                    minProtein,
                    maxProtein
            );
        }
        catch (Exception ex) {
            // Conversion of technical error → user-friendly UI message
            final List<RecipeSummary> emptySummaries = new ArrayList<>();
            final String errorMessage = "Could not load recipes right now. Please try again later.";
            final GenerateRecipeOutputData outputData = new GenerateRecipeOutputData(emptySummaries, errorMessage);
            recipePresenter.prepareView(outputData);
            return;
        }

        // Make recipes available for approval feature (only if that subsystem is present)
        if (approveRecipeDataAccess != null) {
            approveRecipeDataAccess.setAvailableRecipes(recipes);
        }

        // Case: DAO returned empty list (no matches for selected filters)
        if (recipes == null || recipes.isEmpty()) {
            final List<RecipeSummary> emptySummaries = new ArrayList<>();
            final String message = "No recipes found, please try different filter options";
            final GenerateRecipeOutputData outputData = new GenerateRecipeOutputData(emptySummaries, message);
            recipePresenter.prepareView(outputData);
            return;
        }

        // Convert Recipe entities into RecipeSummary objects (minimal UI model)
        final List<RecipeSummary> recipeSummaryList = new ArrayList<>();
        for (Recipe recipe : recipes) {
            final RecipeSummary recipeX = new RecipeSummary(
                    recipe.getRecipeName(),
                    recipe.getRecipeId(),
                    recipe.getRecipeImage()
            );
            recipeSummaryList.add(recipeX);
        }

        // Success: return list with no error message
        final GenerateRecipeOutputData outputData =
                new GenerateRecipeOutputData(recipeSummaryList, "");
        recipePresenter.prepareView(outputData);
    }
}
