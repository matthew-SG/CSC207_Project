package use_case.recipe_generator;
import java.util.ArrayList;

import entities.Cuisine;
import entities.DietaryRestriction;
import entities.Intolerance;
import  entities.Recipe;
import java.util.List;


public class RecipeGeneratorInteractor implements RecipeGeneratorInputBoundary{
    private final RecipeDataAccessInterface userRecipeAccessObject;
    private final RecipeGeneratorOutputBoundary recipePresenter;

    public RecipeGeneratorInteractor(RecipeDataAccessInterface userRecipeAccessObject, RecipeGeneratorOutputBoundary recipePresenter) {
        this.userRecipeAccessObject = userRecipeAccessObject;
        this.recipePresenter = recipePresenter;
    }

    @Override
    public void generateRecipes(GenerateRecipeInputData inputData){

        DietaryRestriction dietRestriction = inputData.getDietaryRestriction();
        List<Intolerance> intolerances = inputData.getIntolerances();
        Cuisine cuisine = inputData.getCuisine();
        Integer minCalories = inputData.getMinCalories();
        Integer maxCalories = inputData.getMaxCalories();
        Integer minProtein = inputData.getMinProtein();
        Integer maxProtein = inputData.getMaxProtein();

        List<Recipe> recipes;

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
        } catch (Exception e) {
            List<RecipeSummary> emptySummaries = new ArrayList<>();
            String errorMessage = "Could not load recipes right now. Please try again later.";

            GenerateRecipeOutputData outputData =
                    new GenerateRecipeOutputData(emptySummaries, errorMessage);
            recipePresenter.prepareView(outputData);
            return;
        }

        if (recipes == null || recipes.isEmpty()) {
            List<RecipeSummary> emptySummaries = new ArrayList<>();
            String message = "No recipes found, please try different filter options";

            GenerateRecipeOutputData outputData =
                    new GenerateRecipeOutputData(emptySummaries, message);
            recipePresenter.prepareView(outputData);
            return;
        }

        List<RecipeSummary> recipeSummaryList = new ArrayList<>();
        for (Recipe recipe : recipes) {
            RecipeSummary recipeX = new RecipeSummary(
                    recipe.getRecipeName(),
                    recipe.getRecipeId(),
                    recipe.getRecipeImage()
            );
            recipeSummaryList.add(recipeX);
        }

        GenerateRecipeOutputData outputData =
                new GenerateRecipeOutputData(recipeSummaryList, "");
        recipePresenter.prepareView(outputData);
    }

}


