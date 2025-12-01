package entities;

/**
 * Class representing the rating entity.
 */
public class Rating {
    private final int ratingId;
    private final int recipeId;
    private final String username;
    private final int stars;
    private final String comment;
    private final String recipeName;
    private final String recipeImageUrl;

    public Rating(int ratingId, int recipeId, String username, int stars, String comment, String recipeName,
                  String recipeImageUrl) {
        this.ratingId = ratingId;
        this.recipeId = recipeId;
        this.username = username;
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

    public String getRecipeImageUrl() {
        return recipeImageUrl;
    }

    public Recipe getDetailedRecipe() {
        return detailedRecipe;
    }

    public void setDetailedRecipe(Recipe detailedRecipe) {
        this.detailedRecipe = detailedRecipe;
    }

    public String getUsername() {
        return username;
    }
}
