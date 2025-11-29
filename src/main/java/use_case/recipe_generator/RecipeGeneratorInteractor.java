package use_case.recipe_generator;
import java.util.ArrayList;

import entities.Cuisine;
import entities.DietaryRestriction;
import entities.Intolerance;
import  entities.Recipe;
import java.util.List;


import use_case.approve_recipe.ApproveRecipeDataAccessInterface;

public class RecipeGeneratorInteractor implements RecipeGeneratorInputBoundary{
    private final RecipeDataAccessInterface userRecipeAccessObject;
    private final RecipeGeneratorOutputBoundary recipePresenter;
    private final ApproveRecipeDataAccessInterface approveRecipeDataAccessObject;

    public RecipeGeneratorInteractor(RecipeDataAccessInterface userRecipeAccessObject, 
                                     RecipeGeneratorOutputBoundary recipePresenter,
                                     ApproveRecipeDataAccessInterface approveRecipeDataAccessObject) {
        this.userRecipeAccessObject = userRecipeAccessObject;
        this.recipePresenter = recipePresenter;
        this.approveRecipeDataAccessObject = approveRecipeDataAccessObject;
    }

    @Override
    public void generateRecipes(GenerateRecipeInputData inputData){
        DietaryRestriction dietRestriction = inputData.getDietaryRestriction();
        List<Intolerance> intolerances = inputData.getIntolerances();
        Cuisine cuisine = inputData.getCuisine();
        Integer maxCalories = inputData.getMaxCalories();
        Integer minProtein = inputData.getMinProtein();
        List<Recipe> recipes = userRecipeAccessObject.getRecipes(dietRestriction, intolerances, cuisine, maxCalories, minProtein);
        
        // Save recipes to the shared DAO for approval
        if (approveRecipeDataAccessObject instanceof data_access.FileDataAccessObject) {
            ((data_access.FileDataAccessObject) approveRecipeDataAccessObject).setAvailableRecipes(recipes);
        } else if (approveRecipeDataAccessObject != null) {
            // Fallback if interface doesn't have setAvailableRecipes but usually we cast
            // If we want to enforce it in interface we should modify interface, but user asked to use FileDataAccessObject
            // For now, since we know the implementation, casting is acceptable or we could add method to interface.
            // Adding to interface is cleaner.
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


