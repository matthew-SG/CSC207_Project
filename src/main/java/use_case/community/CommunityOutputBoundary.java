package use_case.community;

import use_case.community.output_data.CommunityLikedRecipesOutputData;
import use_case.community.output_data.CommunityRatingsOutputData;
import use_case.community.output_data.CommunitySelectedRecipeOutputData;

public interface CommunityOutputBoundary {
    public void prepareFailView(String error);

    public void prepareViewRating(CommunityRatingsOutputData response);

    public void prepareRecipeSelection(CommunityLikedRecipesOutputData response);

    public void prepareCommentWriting(CommunitySelectedRecipeOutputData response);

    public void preparePublishSucc(CommunityRatingsOutputData response);

}
