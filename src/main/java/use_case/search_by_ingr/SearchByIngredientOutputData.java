package use_case.search_by_ingr;
import entities.Ingredient;
import entities.Recipe;

import java.util.List;

public class SearchByIngredientOutputData {
    private final List<Recipe> RECIPES;
    private final String MSG;
    public SearchByIngredientOutputData(List<Recipe> recipes, String msg) {
        RECIPES = recipes;
        this.MSG = msg;
    }
    public List<Recipe> getRecipes() {
        return RECIPES;
    }
    public String getMsg() {
        return MSG;
    }
}
