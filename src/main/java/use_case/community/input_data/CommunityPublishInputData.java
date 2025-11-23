package use_case.community.input_data;

public class CommunityPublishInputData {
    private final int rating;
    private final String comment;
    private final int recipeID;
    private final String userName;
    private final String recipeName;
    private final String recipeImageURL;
    public CommunityPublishInputData(String userName, int recipeID, int rating,
                                     String comment, String recipeName, String recipeImageURL) {
        this.userName = userName;
        this.recipeID = recipeID;
        this.rating = rating;
        this.comment = comment;
        this.recipeName = recipeName;
        this.recipeImageURL = recipeImageURL;
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

    public String getRecipeName() {
        return recipeName;
    }

    public String getRecipeImageURL() {
        return recipeImageURL;
    }
}