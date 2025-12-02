package use_case.search_by_ingr;

import java.util.List;

import org.json.JSONObject;

import entities.Ingredient;

/**
 * Gateway interface for accessing recipe data from an external source.
 * Implementations are responsible for calling the API and returning results.
 */
public interface SearchByIngredientGateway {

    /**
     * Searches for recipes using the given list of ingredients.
     *
     * @param ingredients the ingredients to search with
     * @return a JSONObject containing search results, or null if the call fails
     */
    JSONObject searchByIngredients(List<Ingredient> ingredients);
}