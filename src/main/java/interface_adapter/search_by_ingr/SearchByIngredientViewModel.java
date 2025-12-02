package interface_adapter.search_by_ingr;

import interface_adapter.ViewModel;

/**
 * View model for the Search-By-Ingredient feature.
 * Holds and manages the state used by the corresponding view.
 */
public class SearchByIngredientViewModel extends ViewModel<SearchByIngredientState> {
    public static final String viewName = "Search By Ingredient";

    /**
     * Creates a new view model with an empty initial state.
     */
    public SearchByIngredientViewModel() {
        super(viewName);
        setState(new SearchByIngredientState());
    }
}