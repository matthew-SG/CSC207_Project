package use_case.recipe_generator;

import java.util.ArrayList;
import java.util.List;

import entities.Cuisine;
import entities.DietaryRestriction;
import entities.Intolerance;
import entities.Recipe;
import use_case.approve_recipe.ApproveRecipeDataAccessInterface;

public class RecipeGeneratorInteractor implements RecipeGeneratorInputBoundary{
    private final RecipeDataAccessInterface userRecipeAccessObject;
    private final RecipeGeneratorOutputBoundary recipePresenter;
    private final ApproveRecipeDataAccessInterface approveRecipeDataAccess;

    public RecipeGeneratorInteractor(RecipeDataAccessInterface userRecipeAccessObject,
                                     RecipeGeneratorOutputBoundary recipePresenter,
                                     ApproveRecipeDataAccessInterface approveRecipeDataAccess) {
        this.userRecipeAccessObject = userRecipeAccessObject;
        this.recipePresenter = recipePresenter;
        this.approveRecipeDataAccess = approveRecipeDataAccess;
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
        List<Recipe> recipes = userRecipeAccessObject.getRecipes(dietRestriction, intolerances, cuisine, minCalories, maxCalories, minProtein, maxProtein);

        // Make recipes available for approval
        if (approveRecipeDataAccess != null) {
            approveRecipeDataAccess.setAvailableRecipes(recipes);
        }
        
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


