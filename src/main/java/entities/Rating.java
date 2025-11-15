package entities;

public class Rating {
    private int ratingId;
    private int recipeId;
    private int userId;
    private int stars;
    private String comment;

    public String getComment() {
        return comment;
    }

    public int getRatingId() {
        return ratingId;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public int getStars() {
        return stars;
    }

    public int getUserId() {
        return userId;
    }

    // not final fileds (might be deleted)
    private String recipeName;
    public String getRecipeName() {
        return recipeName;
    }
}