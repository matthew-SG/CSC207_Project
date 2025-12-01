package use_case.community.input_data;

public class CommunityLikeRecipeInputData {
    private final String username;
    private final int ratingId;

    public CommunityLikeRecipeInputData(String username, int ratingId) {
        this.username = username;
        this.ratingId = ratingId;
    }

    public String getUsername() {
        return username;
    }

    public int getRatingId() {
        return ratingId;
    }
}
