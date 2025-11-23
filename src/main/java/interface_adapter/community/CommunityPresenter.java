package interface_adapter.community;

import interface_adapter.ViewManagerModel;
import use_case.community.CommunityOutputBoundary;
import use_case.community.output_data.CommunityLikedRecipesOutputData;
import use_case.community.output_data.CommunityRatingsOutputData;
import use_case.community.output_data.CommunitySelectedRecipeOutputData;

public class CommunityPresenter implements CommunityOutputBoundary {
    private final ViewManagerModel viewManagerModel;
    private final CommunityViewModel communityViewModel;

    public CommunityPresenter(ViewManagerModel viewManagerModel, CommunityViewModel communityViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.communityViewModel = communityViewModel;
    }

    /*
    *  Change no state, just inform the user
    * */
    @Override
    public void prepareFailView(String error) {
        this.viewManagerModel.showsErrorMessage(error);
    }

    @Override
    public void prepareViewRating(CommunityRatingsOutputData response) {
        prepareView(response);
    }

    @Override
    public void prepareRecipeSelection(CommunityLikedRecipesOutputData response) {
        CommunityState communityState = this.communityViewModel.getState();
        communityState.setSubviewName(CommunityViewModel.SELECTING_RECIPE);
        communityState.setRecipeIds(response.getRecipeIds());
        communityState.setRecipeNames(response.getRecipeNames());
        communityState.setRecipeImages(response.getRecipeImages());
        this.communityViewModel.firePropertyChange();
    }

    @Override
    public void prepareCommentWriting(CommunitySelectedRecipeOutputData response) {
        CommunityState communityState = this.communityViewModel.getState();
        communityState.setSubviewName(CommunityViewModel.WRITING_REVIEW);
        communityState.setSeletedRecipe(response.getSelectedRecipeId());
        this.communityViewModel.firePropertyChange();
    }

    @Override
    public void preparePublishSucc(CommunityRatingsOutputData response) {
        prepareView(response);
        CommunityState communityState = this.communityViewModel.getState();
        communityState.setSubviewName(CommunityViewModel.PUB_SUCC);
    }

    private void prepareView(CommunityRatingsOutputData response) {
        CommunityState communityState = this.communityViewModel.getState();
        communityState.setSubviewName(CommunityViewModel.VIEWING);
        communityState.setRecipeIds(response.getRecipeIds());
        communityState.setRecipeNames(response.getRecipeNames());
        communityState.setStars(response.getStars());
        communityState.setComments(response.getComments());
        communityState.setPrompt(response.getPrompt());
        this.communityViewModel.firePropertyChange();
    }
}
