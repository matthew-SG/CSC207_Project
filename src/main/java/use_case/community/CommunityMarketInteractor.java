package use_case.community;

import entities.Recipe;
import entities.User;
import use_case.community.input_data.CommunityPoseSelectionInputData;
import use_case.community.input_data.CommunityPublishInputData;
import use_case.community.input_data.CommunityRecipeSelectionInputData;
import use_case.community.output_data.CommunityLikedRecipesOutputData;
import use_case.community.output_data.CommunityPublishSuccessOutputData;
import use_case.community.output_data.CommunityRatingsOutputData;
import use_case.community.output_data.CommunitySelectedRecipeOutputData;

import java.util.List;

public class CommunityMarketInteractor implements CommunityInputBoundary {
    CommunityDataAccessInterface communityDAO;
    CommunityOutputBoundary communityPresenter;


    public CommunityMarketInteractor(CommunityDataAccessInterface communityDAO, CommunityOutputBoundary communityPresenter){
        this.communityDAO = communityDAO;
        this.communityPresenter = communityPresenter;
    }



    @Override
    public void viewCommunity(){
        communityPresenter.prepareViewRating(
                new CommunityRatingsOutputData(
                        communityDAO.getCurrentRatings()
                )
        );
    }

    @Override
    public void viewToPost(CommunityPoseSelectionInputData data) {
        if (!data.isLoggedIn()) {
            communityPresenter.prepareFailView("Log in to write reviews");
            return;
        }

        User user = new User(data.getUserName(), "dummy", null);
        communityPresenter.prepareRecipeSelection(
                new CommunityLikedRecipesOutputData(
                        communityDAO.getLikedRecipes(user)
                )
        );
    }

    @Override
    public void selectRecipe(CommunityRecipeSelectionInputData data) {
        communityPresenter.prepareCommentWriting(
                new CommunitySelectedRecipeOutputData(
                        communityDAO.getSelectedRecipe(data.getRecipeID()).getRecipeId()
                )
        );
    }

    @Override
    public void publish(CommunityPublishInputData data) {
        communityPresenter.preparePublishSucc(
                new CommunityPublishSuccessOutputData(
                        communityDAO.publishReview(data)
                )
        );
    }
}