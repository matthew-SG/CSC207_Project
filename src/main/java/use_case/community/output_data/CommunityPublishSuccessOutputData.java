package use_case.community.output_data;

import entities.Rating;

import java.util.List;

public class CommunityPublishSuccessOutputData extends CommunityRatingsOutputData {

    public CommunityPublishSuccessOutputData(List<Rating> ratings) {
        super(ratings);
        prompt = "Review published successfully";

    }
}
