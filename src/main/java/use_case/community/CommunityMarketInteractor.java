package use_case.community;

import entities.User;
import use_case.community.input_data.CommunityPoseSelectionInputData;
import use_case.community.input_data.CommunityPublishInputData;
import use_case.community.input_data.CommunityRecipeSelectionInputData;
import use_case.community.output_data.CommunityLikedRecipesOutputData;
import use_case.community.output_data.CommunityPublishSuccessOutputData;
import use_case.community.output_data.CommunityRatingsOutputData;
import use_case.community.output_data.CommunitySelectedRecipeOutputData;

public class CommunityMarketInteractor implements CommunityInputBoundary {
    private CommunityDataAccessInterface communityDAO;
    private CommunityOutputBoundary communityPresenter;

    public CommunityMarketInteractor(CommunityDataAccessInterface communityDAO,
                                     CommunityOutputBoundary communityPresenter) {
        this.setCommunityDAO(communityDAO);
        this.setCommunityPresenter(communityPresenter);
    }



    @Override
    public void viewCommunity() {
        getCommunityPresenter().prepareViewRating(
                new CommunityRatingsOutputData(
                        getCommunityDAO().getCurrentRatings()
                )
        );
    }

    @Override
    public void viewToPost(CommunityPoseSelectionInputData data) {
        if (!data.isLoggedIn()) {
            getCommunityPresenter().prepareFailView("Log in to write reviews");
            return;
        }

        User user = new User(data.getUserName(), "dummy", null);
        communityPresenter.prepareRecipeSelection(
                new CommunityLikedRecipesOutputData(
                        getCommunityDAO().getLikedRecipes(user)
                )
        );
    }

    @Override
    public void selectRecipe(CommunityRecipeSelectionInputData data) {
        getCommunityPresenter().prepareCommentWriting(
                new CommunitySelectedRecipeOutputData(
                        getCommunityDAO().getSelectedRecipe(data.getRecipeID()).getRecipeId()
                )
        );
    }

    @Override
    public void publish(CommunityPublishInputData data) {
        getCommunityPresenter().preparePublishSucc(
                new CommunityPublishSuccessOutputData(
                        getCommunityDAO().publishReview(data)
                )
        );
    }

    public CommunityDataAccessInterface getCommunityDAO() {
        return communityDAO;
    }

    public void setCommunityDAO(CommunityDataAccessInterface communityDAO) {
        this.communityDAO = communityDAO;
    }

    public CommunityOutputBoundary getCommunityPresenter() {
        return communityPresenter;
    }

    public void setCommunityPresenter(CommunityOutputBoundary communityPresenter) {
        this.communityPresenter = communityPresenter;
    }
}
