package interface_adapter.community;

import use_case.community.CommunityInputBoundary;
import use_case.community.input_data.CommunityLikeRecipeInputData;
import use_case.community.input_data.CommunityPoseSelectionInputData;
import use_case.community.input_data.CommunityPublishInputData;
import use_case.community.input_data.CommunityRecipeSelectionInputData;

public class CommunityController {
    private final CommunityInputBoundary communityInteractor;

    public CommunityController(CommunityInputBoundary communityInteractor) {
        this.communityInteractor = communityInteractor;
    }

    public void viewCommunity(){
        this.communityInteractor.viewCommunity();
    }

    public void publish(int rating, String comment, int recipeID, String userName,
                        String recipeName, String recipeImageUrl){
        this.communityInteractor.publish(
                new CommunityPublishInputData(userName, recipeID, rating, comment, recipeName, recipeImageUrl)
        );
    }

    public void viewToPost(String userName, boolean isLoggedIn){
        this.communityInteractor.viewToPost(
                new CommunityPoseSelectionInputData(
                        isLoggedIn,
                        userName
                )
        );
    }

    public void selectRecipe(int recipeID, String recipeName, String recipeImageUrl){
        this.communityInteractor.selectRecipe(
                new CommunityRecipeSelectionInputData(
                        recipeID,
                        recipeName,
                        recipeImageUrl
                )
        );
    }

    public void likeRecipe(int ratingId, String username) {
        this.communityInteractor.likeRecipe(
                new CommunityLikeRecipeInputData(username, ratingId)
        );
    }
}
