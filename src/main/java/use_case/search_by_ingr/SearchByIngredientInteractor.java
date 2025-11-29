package use_case.search_by_ingr;

import API.SearchByIngredientSpoonacular;
import entities.Ingredient;
import entities.Recipe;

import java.util.ArrayList;
import java.util.List;

import use_case.approve_recipe.ApproveRecipeDataAccessInterface;

public class SearchByIngredientInteractor implements SearchByIngredientInputBoundary{
    private final SearchByIngredientSpoonacular api;
    private final ApproveRecipeDataAccessInterface approveRecipeDataAccessObject;

    public SearchByIngredientInteractor(SearchByIngredientSpoonacular api, 
                                        ApproveRecipeDataAccessInterface approveRecipeDataAccessObject) {
        this.api = api;
        this.approveRecipeDataAccessObject = approveRecipeDataAccessObject;
    }

    @Override
    public SearchByIngredientOutputData execute(SearchByIngredientInputData inputData) {
        List<Ingredient> ingredients = inputData.getIngredients();
        if(ingredients.isEmpty()){return new SearchByIngredientOutputData(List.of(),"Enter at least one ingredient");}
        ArrayList<String> ingrNames = new ArrayList<>();
        for (Ingredient ingr : ingredients)
            ingrNames.add(ingr.getName());
        List<Recipe> recipes = api.searchByIngredientSpoonacular(ingrNames);
        
        // Save recipes to the shared DAO for approval
        if (approveRecipeDataAccessObject instanceof data_access.FileDataAccessObject) {
            ((data_access.FileDataAccessObject) approveRecipeDataAccessObject).setAvailableRecipes(recipes);
        }

        String msg;
        if(recipes.isEmpty()) msg="no perfect match found";
        else msg="found";
        return new SearchByIngredientOutputData(recipes,msg);
    }
}
