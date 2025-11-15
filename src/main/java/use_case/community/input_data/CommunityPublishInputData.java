package use_case.community.input_data;

public class CommunityPublishInputData {
    private final int rating;
    private final String comment;
    private final int recipeID;
    private final String userName;
    public CommunityPublishInputData(String userName, int recipeID, int rating, String comment){
        this.userName = userName;
        this.recipeID = recipeID;
        this.rating = rating;
        this.comment = comment;
    }

    public int getRecipeID() {
        return recipeID;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public String getUserName() {
        return userName;
    }
}