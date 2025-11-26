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
        Integer maxCalories = inputData.getMaxCalories();
        Integer minProtein = inputData.getMinProtein();
        List<Recipe> recipes = userRecipeAccessObject.getRecipes(dietRestriction, intolerances, cuisine, maxCalories, minProtein);
        List<RecipeSummary>  recipeSummaryList = new ArrayList<>();
        String message;
        for (Recipe recipe : recipes) {
            RecipeSummary recipeX = new RecipeSummary(recipe.getRecipeName(), recipe.getRecipeId(), recipe.getRecipeImage());
            recipeSummaryList.add(recipeX);
        }
        if (recipeSummaryList.isEmpty()){
             message = "No recipes found, try changing your recipe filters";
        } else {
            message = "";
        }
        GenerateRecipeOutputData outputData = new GenerateRecipeOutputData(recipeSummaryList, message);
        recipePresenter.prepareView(outputData);
    }
}


