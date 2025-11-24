package interface_adapter.search_by_ingr;

import interface_adapter.ViewModel;

public class SearchByIngredientViewModel extends ViewModel<SearchByIngredientState> {
    public static final String viewName = "Search By Ingredient";

    public SearchByIngredientViewModel() {
        super(viewName);
        setState(new SearchByIngredientState());
    }
}