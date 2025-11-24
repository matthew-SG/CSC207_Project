package use_case.search_by_ingr;

import API.SearchByIngredientSpoonacular;
import entities.Ingredient;
import entities.Recipe;

import java.util.ArrayList;
import java.util.List;

public class SearchByIngredientInteractor implements SearchByIngredientInputBoundary{
    private final SearchByIngredientSpoonacular api;
    public SearchByIngredientInteractor(SearchByIngredientSpoonacular api) {
        this.api = api;
    }

    @Override
    public SearchByIngredientOutputData execute(SearchByIngredientInputData inputData) {
        List<Ingredient> ingredients = inputData.getIngredients();
        if(ingredients.isEmpty()){return new SearchByIngredientOutputData(List.of(),"Enter at least one ingredient");}
        ArrayList<String> ingrNames = new ArrayList<>();
        for (Ingredient ingr : ingredients)
            ingrNames.add(ingr.getName());
        List<Recipe> recipes = api.searchByIngredientSpoonacular(ingrNames);
        String msg;
        if(recipes.isEmpty()) msg="no perfect match found";
        else msg="found";
        return new SearchByIngredientOutputData(recipes,msg);
    }
}
