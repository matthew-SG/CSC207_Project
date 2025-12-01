package use_case.community;

import entities.Rating;
import entities.User;
import use_case.community.input_data.CommunityLikeRecipeInputData;
import use_case.community.input_data.CommunityPoseSelectionInputData;
import use_case.community.input_data.CommunityPublishInputData;
import use_case.community.input_data.CommunityRecipeSelectionInputData;
import use_case.community.output_data.CommunityLikeRecipeOutputData;
import use_case.community.output_data.CommunityLikedRecipesOutputData;
import use_case.community.output_data.CommunityPublishSuccessOutputData;
import use_case.community.output_data.CommunityRatingsOutputData;
import use_case.community.output_data.CommunitySelectedRecipeOutputData;

public class CommunityMarketInteractor implements CommunityInputBoundary {
    private CommunityDataAccessInterface communityDAO;
    private CommunityUserRecipeDataAccessInterface communityLikeRecipeDAO;
    private CommunityOutputBoundary communityPresenter;

    public CommunityMarketInteractor(CommunityDataAccessInterface communityDAO,
                                     CommunityOutputBoundary communityPresenter, CommunityUserRecipeDataAccessInterface communityLikeRecipeDAO) {
        this.setCommunityDAO(communityDAO);
        this.setCommunityPresenter(communityPresenter);
        this.communityLikeRecipeDAO = communityLikeRecipeDAO;
    }

    @Override
    public void viewCommunity() {
        getCommunityPresenter().prepareViewRating(
                new CommunityRatingsOutputData(
                        getCommunityDAO().getCurrentRatings()
                )
        );
    }

    /**
     * Grab the recipe and add it to user's liked recipe and save.
     *
     * @param data
     */
    @Override
    public void likeRecipe(CommunityLikeRecipeInputData data) {
        boolean found = false;
        Rating queriedRating = null;
        for (Rating rating: this.communityDAO.getCurrentRatings()) {
            if (rating.getRatingId() == data.getRatingId()){
                found = true;
                queriedRating = rating;
                break;
            }
        }
        if (!found){
            this.communityPresenter.prepareFailView("Rating not found");
            return;
        }
        if (queriedRating.getDetailedRecipe() == null) {
            this.communityPresenter.prepareFailView("Detailed recipe unavailable for this rating yet.");
            return;
        }

        this.communityLikeRecipeDAO.saveRecipeToUser(data.getUsername(), queriedRating.getDetailedRecipe());
        this.communityPresenter.prepareAddSucc(
                new CommunityLikeRecipeOutputData(
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

        communityPresenter.prepareRecipeSelection(
                new CommunityLikedRecipesOutputData(
                        getCommunityDAO().getLikedRecipes(data.getUserName())
                )
        );
    }

    @Override
    public void selectRecipe(CommunityRecipeSelectionInputData data) {
        getCommunityPresenter().prepareCommentWriting(
                new CommunitySelectedRecipeOutputData(
                        getCommunityDAO().getSelectedRecipe(data.getRecipeID()).getRecipeId(),
                        data.getRecipeName(),
                        data.getRecipeImageUrl()
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
