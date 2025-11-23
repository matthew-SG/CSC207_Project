package entities;

public class Rating {
    private int ratingId;
    private int recipeId;
    private String userEmail;
    private int stars;
    private String comment;
    private String recipeName;
    private String recipeImageUrl;
    public Rating(int ratingId, int recipeId, String userEmail, int stars, String comment, String recipeName, String recipeImageUrl) {
        this.ratingId = ratingId;
        this.recipeId = recipeId;
        this.userEmail = userEmail;
        this.stars = stars;
        this.comment = comment;
        this.recipeName = recipeName;
        this.recipeImageUrl = recipeImageUrl;
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

    public String getRecipeName() {
        return recipeName;
    }
}