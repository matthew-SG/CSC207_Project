package interface_adapter.search_by_ingr;

import interface_adapter.ViewManagerModel;
import use_case.search_by_ingr.*;

/**
 * Presenter for the Search-By-Ingredient use case.
 * Updates the view model and view manager based on success or failure.
 */
public class SearchByIngredientPresenter implements SearchByIngredientOutputBoundary {
    private ViewManagerModel viewManagerModel;
    private SearchByIngredientViewModel searchByIngredientViewModel;

    /**
     * Creates a presenter with the given view manager and view model.
     *
     * @param viewManagerModel manages which view is shown
     * @param searchByIngredientView the view model for this feature
     */
    public SearchByIngredientPresenter(ViewManagerModel viewManagerModel,
                                       SearchByIngredientViewModel searchByIngredientView) {
        this.searchByIngredientViewModel = searchByIngredientView;
        this.viewManagerModel = viewManagerModel;
    }

    /**
     * Prepares the success view by updating the state with recipes and a status message.
     *
     * @param outputData the results returned from the interactor
     */
    @Override
    public void prepareSuccessView(SearchByIngredientOutputData outputData) {
        SearchByIngredientState state = new SearchByIngredientState();
        state.setErrorMessage(null);
        state.setRecipes(outputData.getRecipes());
        state.setStatusMessage(outputData.getMsg());
        searchByIngredientViewModel.setState(state);
        searchByIngredientViewModel.firePropertyChange();

        viewManagerModel.getState().viewName = SearchByIngredientViewModel.viewName;
        viewManagerModel.firePropertyChange();
    }

    /**
     * Shows an error message when the use case fails.
     *
     * @param error the error message
     */
    @Override
    public void prepareFailView(String error) {
        this.viewManagerModel.showsErrorMessage(error);
    }
}
