package use_case.search_by_ingr;

import entities.Ingredient;

import java.util.*;

/**
 * Input data for the Search-By-Ingredient use case.
 * Holds the ingredients and the allowed number of missing items.
 */
public class SearchByIngredientInputData {
    private final List<Ingredient> INGREDIENTS;
    private final int AMOUNTMISSING;

    /**
     * Creates a new input data object.
     *
     * @param ingredients the ingredients provided by the user
     * @param amountMissing the max number of missing ingredients allowed
     */
    public SearchByIngredientInputData(List<Ingredient> ingredients, int amountMissing) {
        this.INGREDIENTS = ingredients;
        this.AMOUNTMISSING = amountMissing;
    }

    /**
     * @return the list of user-provided ingredients
     */
    public List<Ingredient> getIngredients() {
        return INGREDIENTS;
    }

    /**
     * @return the maximum number of missing ingredients allowed
     */
    public int getAmountMissing() {
        return AMOUNTMISSING;
    }
}