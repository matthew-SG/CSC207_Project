package interface_adapter.search_by_ingr;

import interface_adapter.ViewManagerModel;
import use_case.search_by_ingr.*;

public class SearchByIngredientPresenter implements SearchByIngredientOutputBoundary{
    private ViewManagerModel viewManagerModel;
    private SearchByIngredientViewModel searchByIngredientViewModel;
    public SearchByIngredientPresenter(ViewManagerModel viewManagerModel,SearchByIngredientViewModel searchByIngredientView) {
        this.searchByIngredientViewModel = searchByIngredientView;
        this.viewManagerModel = viewManagerModel;
    }
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

    @Override
    public void prepareFailView(String error) {
    this.viewManagerModel.showsErrorMessage(error);
    }
}
