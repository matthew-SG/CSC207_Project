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
        CommunityState communityState = this.communityViewModel.getState();
        communityState.subviewName = CommunityViewModel.VIEWING;
        communityState.recipeIds = response.getRecipeIds();
        communityState.recipeNames = response.getRecipeNames();
        communityState.stars = response.getStars();
        communityState.comments = response.getComments();
        communityState.prompt = response.getPrompt();
        this.communityViewModel.firePropertyChange();
    }

    @Override
    public void prepareRecipeSelection(CommunityLikedRecipesOutputData response) {
        CommunityState communityState = this.communityViewModel.getState();
        communityState.subviewName = CommunityViewModel.SELECTING_RECIPE;
        communityState.recipeIds = response.getRecipeIds();
        communityState.recipeNames = response.getRecipeNames();
        communityState.recipeImages = response.getRecipeImages();
        this.communityViewModel.firePropertyChange();
    }

    @Override
    public void prepareCommentWriting(CommunitySelectedRecipeOutputData response) {
        CommunityState communityState = this.communityViewModel.getState();
        communityState.subviewName = CommunityViewModel.WRITING_REVIEW;
        communityState.seletedRecipe = response.getSelectedRecipeId();
        this.communityViewModel.firePropertyChange();
    }

    @Override
    public void preparePublishSucc(CommunityRatingsOutputData response) {
        CommunityState communityState = this.communityViewModel.getState();
        communityState.subviewName = CommunityViewModel.PUB_SUCC;
        communityState.recipeIds = response.getRecipeIds();
        communityState.recipeNames = response.getRecipeNames();
        communityState.stars = response.getStars();
        communityState.comments = response.getComments();
        communityState.prompt = response.getPrompt();
        this.communityViewModel.firePropertyChange();
    }
}
