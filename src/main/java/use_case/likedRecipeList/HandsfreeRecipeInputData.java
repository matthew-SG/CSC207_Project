package use_case.likedRecipeList;

public class HandsfreeRecipeInputData {
    private final String RecipeId;

    public HandsfreeRecipeInputData(String recipeId) {
        this.RecipeId = recipeId;
    }
    public String getRecipeId() {
        return RecipeId;
    }
}
