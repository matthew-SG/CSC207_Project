package interface_adapter.search_by_ingr;

import entities.Ingredient;
import use_case.search_by_ingr.*;

import java.util.List;

public class SearchByIngredientController {
    private final SearchByIngredientInputBoundary interactor;

    public SearchByIngredientController(SearchByIngredientInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void search(List<Ingredient> ingredients, int amountMissing) {
        SearchByIngredientInputData inputData = new SearchByIngredientInputData(ingredients, amountMissing);
        interactor.execute(inputData);
    }
}
