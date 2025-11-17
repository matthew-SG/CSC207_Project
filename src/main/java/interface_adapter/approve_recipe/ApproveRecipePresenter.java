package interface_adapter.approve_recipe;

import interface_adapter.ViewManagerModel;
import use_case.approve_recipe.ApproveRecipeOutputBoundary;
import use_case.approve_recipe.ApproveRecipeOutputData;

/**
 * Presenter for the approve recipe use case.
 */
public class ApproveRecipePresenter implements ApproveRecipeOutputBoundary {
    private final ViewManagerModel viewManagerModel;
    private final ApproveRecipeViewModel approveRecipeViewModel;

    public ApproveRecipePresenter(ViewManagerModel viewManagerModel,
                                  ApproveRecipeViewModel approveRecipeViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.approveRecipeViewModel = approveRecipeViewModel;
    }

    @Override
    public void prepareRecipeView(ApproveRecipeOutputData outputData) {
        ApproveRecipeState state = approveRecipeViewModel.getState();
        state.setRecipeIds(outputData.getRecipeIds());
        state.setRecipeNames(outputData.getRecipeNames());
        state.setRecipeImages(outputData.getRecipeImages());
        state.setCurrentIndex(outputData.getCurrentIndex());
        state.setHasMore(outputData.hasMore());
        state.setErrorMessage(null);

        approveRecipeViewModel.setState(state);
        approveRecipeViewModel.firePropertyChange();

        viewManagerModel.getState().viewName = ApproveRecipeViewModel.viewName;
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareApproveSuccessView(ApproveRecipeOutputData outputData) {
        ApproveRecipeState state = approveRecipeViewModel.getState();
        state.setRecipeIds(outputData.getRecipeIds());
        state.setRecipeNames(outputData.getRecipeNames());
        state.setRecipeImages(outputData.getRecipeImages());
        state.setCurrentIndex(outputData.getCurrentIndex());
        state.setHasMore(outputData.hasMore());
        state.setErrorMessage(null);

        approveRecipeViewModel.setState(state);
        approveRecipeViewModel.firePropertyChange();
    }

    @Override
    public void prepareDeclineView(ApproveRecipeOutputData outputData) {
        ApproveRecipeState state = approveRecipeViewModel.getState();
        state.setRecipeIds(outputData.getRecipeIds());
        state.setRecipeNames(outputData.getRecipeNames());
        state.setRecipeImages(outputData.getRecipeImages());
        state.setCurrentIndex(outputData.getCurrentIndex());
        state.setHasMore(outputData.hasMore());
        state.setErrorMessage(null);

        approveRecipeViewModel.setState(state);
        approveRecipeViewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        ApproveRecipeState state = approveRecipeViewModel.getState();
        state.setErrorMessage(error);

        approveRecipeViewModel.setState(state);
        approveRecipeViewModel.firePropertyChange();
    }
}
