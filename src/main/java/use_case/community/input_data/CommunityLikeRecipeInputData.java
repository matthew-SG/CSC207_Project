package use_case.community.input_data;

public class CommunityLikeRecipeInputData {
    private final String username;
    private final String ratingId;

    public CommunityLikeRecipeInputData(String username, String ratingId) {
        this.username = username;
        this.ratingId = ratingId;
    }

    public String getUsername() {
        return username;
    }

    public String getRatingId() {
        return ratingId;
    }
}
