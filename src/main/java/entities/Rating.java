package entities;

public class Rating {
    private int ratingId;
    private int recipeId;
    private int userId;
    private int stars;
    private String comment;

    public Rating(int ratingId, int recipeId, int userId, int stars, String comment) {
        this.ratingId = ratingId;
        this.recipeId = recipeId;
        this.userId = userId;
        this.stars = stars;
        this.comment = comment;
    }

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