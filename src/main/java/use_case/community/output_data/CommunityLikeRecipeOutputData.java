package use_case.community.output_data;

import entities.Rating;

import java.util.List;

public class CommunityLikeRecipeOutputData extends CommunityRatingsOutputData{
    public static String PROMPT = "Like recipe successfully!";
    public CommunityLikeRecipeOutputData(List<Rating> ratings) {
        super(ratings);
        prompt = PROMPT;

    }
}
