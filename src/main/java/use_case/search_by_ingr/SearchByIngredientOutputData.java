package use_case.search_by_ingr;

import entities.Recipe;
import java.util.List;

/**
 * Output data for the Search-By-Ingredient use case.
 * Holds the resulting recipes and a status message.
 */
public class SearchByIngredientOutputData {
    private final List<Recipe> RECIPES;
    private final String MSG;

    /**
     * Creates a new output data object.
     *
     * @param recipes the list of recipes found
     * @param msg a status or info message
     */
    public SearchByIngredientOutputData(List<Recipe> recipes, String msg) {
        RECIPES = recipes;
        this.MSG = msg;
    }

    /**
     * @return the list of resulting recipes
     */
    public List<Recipe> getRecipes() {
        return RECIPES;
    }

    /**
     * @return the status message
     */
    public String getMsg() {
        return MSG;
    }
}