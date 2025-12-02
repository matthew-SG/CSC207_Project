package interface_adapter.search_by_ingr;

import entities.Ingredient;
import entities.Recipe;

import java.util.*;

/**
 * State object for the Search-By-Ingredient view.
 * Stores ingredients, results, and status/error messages.
 */
public class SearchByIngredientState {
    private List<Ingredient> ingredients = new ArrayList<>();
    private List<Recipe> recipes = new ArrayList<>();
    private String statusMessage;
    private String errorMessage;

    /**
     * Creates an empty state.
     */
    public SearchByIngredientState() {}

    /**
     * @return the list of user-entered ingredients
     */
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    /**
     * Sets the current list of ingredients.
     *
     * @param ingredients the ingredient list
     */
    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * @return the list of recipes returned by the search
     */
    public List<Recipe> getRecipes() {
        return recipes;
    }

    /**
     * Sets the list of recipes found.
     *
     * @param recipes the recipes
     */
    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    /**
     * @return the status message (success/info)
     */
    public String getStatusMessage() {
        return statusMessage;
    }

    /**
     * Sets a status message.
     *
     * @param statusMessage the message to set
     */
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    /**
     * @return the error message, or null if none
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets an error message.
     *
     * @param errorMessage the error text
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
