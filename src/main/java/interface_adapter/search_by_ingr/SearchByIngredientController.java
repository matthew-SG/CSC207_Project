package interface_adapter.search_by_ingr;
import entities.Ingredient;
import use_case.search_by_ingr.*;
import java.util.*;
public class SearchByIngredientController {
    private final SearchByIngredientInputBoundary interactor;
    public SearchByIngredientController(SearchByIngredientInputBoundary interactor) {
        this.interactor = interactor;
    }
    public SearchByIngredientOutputData  search(List<Ingredient> ingredients) {
        SearchByIngredientInputData inputData=new SearchByIngredientInputData(ingredients);
        return interactor.execute(inputData);
    }
}
