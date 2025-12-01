package use_case.recipe_generator;

import java.util.ArrayList;
import java.util.List;

import entities.Cuisine;
import entities.DietaryRestriction;
import entities.Intolerance;
import entities.Recipe;
import use_case.approve_recipe.ApproveRecipeDataAccessInterface;

public class RecipeGeneratorInteractor implements RecipeGeneratorInputBoundary{
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

        DietaryRestriction dietRestriction = inputData.getDietaryRestriction();
        List<Intolerance> intolerances = inputData.getIntolerances();
        Cuisine cuisine = inputData.getCuisine();
        Integer minCalories = inputData.getMinCalories();
        Integer maxCalories = inputData.getMaxCalories();
        Integer minProtein = inputData.getMinProtein();
        Integer maxProtein = inputData.getMaxProtein();

        List<Recipe> recipes;

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
        } catch (Exception ex) {
            // Conversion of technical error → user-friendly UI message
            List<RecipeSummary> emptySummaries = new ArrayList<>();
            String errorMessage = "Could not load recipes right now. Please try again later.";
            GenerateRecipeOutputData outputData = new GenerateRecipeOutputData(emptySummaries, errorMessage);
            recipePresenter.prepareView(outputData);
            return;
        }

        // Make recipes available for approval feature (only if that subsystem is present)
        if (approveRecipeDAO != null) {
            approveRecipeDAO.setAvailableRecipes(recipes);
        }

        // Case: DAO returned empty list (no matches for selected filters)
        if (recipes == null || recipes.isEmpty()) {
            List<RecipeSummary> emptySummaries = new ArrayList<>();
            String message = "No recipes found, please try different filter options";
            GenerateRecipeOutputData outputData = new GenerateRecipeOutputData(emptySummaries, message);
            recipePresenter.prepareView(outputData);
            return;
        }

        // Convert Recipe entities into RecipeSummary objects (minimal UI model)
        List<RecipeSummary> recipeSummaryList = new ArrayList<>();
        for (Recipe recipe : recipes) {
            RecipeSummary recipeX = new RecipeSummary(
                    recipe.getRecipeName(),
                    recipe.getRecipeId(),
                    recipe.getRecipeImage()
            );
            recipeSummaryList.add(recipeX);
        }

        // Success: return list with no error message
        GenerateRecipeOutputData outputData =
                new GenerateRecipeOutputData(recipeSummaryList, "");
        recipePresenter.prepareView(outputData);
    }
}
