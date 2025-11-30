package use_case.community.output_data;

import entities.Rating;

import java.util.List;

public class CommunityLikeRecipeOutputData extends CommunityRatingsOutputData{
    public CommunityLikeRecipeOutputData(List<Rating> ratings) {
        super(ratings);
        prompt = "Like recipe successfully!";

    }
}
