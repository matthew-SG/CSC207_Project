package interface_adapter.search_by_ingr;

import java.util.List;

import entities.Ingredient;
import use_case.search_by_ingr.*;

/**
 * Controller for the Search-By-Ingredient feature.
 * Takes user input from the view and forwards it to the interactor.
 */
public class SearchByIngredientController {
    private final SearchByIngredientInputBoundary interactor;

    /**
     * Creates a controller that uses the given interactor.
     *
     * @param interactor the input boundary for executing the use case
     */
    public SearchByIngredientController(SearchByIngredientInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Sends the ingredients and allowed missing count to the interactor.
     *
     * @param ingredients a list of ingredients entered by the user
     * @param amountMissing the max number of missing ingredients allowed
     */
    public void search(List<Ingredient> ingredients, int amountMissing) {
        SearchByIngredientInputData inputData = new SearchByIngredientInputData(ingredients, amountMissing);
        interactor.execute(inputData);
    }
}
