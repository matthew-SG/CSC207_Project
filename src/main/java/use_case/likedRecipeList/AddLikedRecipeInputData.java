package use_case.likedRecipeList;

public class AddLikedRecipeInputData {
    private final String RecipeId;

    public AddLikedRecipeInputData(String recipeId) {
        this.RecipeId = recipeId;
    }

    public String getRecipeId() {
        return RecipeId;
    }
}

