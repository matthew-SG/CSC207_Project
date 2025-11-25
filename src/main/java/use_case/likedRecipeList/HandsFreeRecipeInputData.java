package use_case.likedRecipeList;

public class HandsFreeRecipeInputData {
    private final String RecipeId;

    public HandsFreeRecipeInputData(String recipeId) {
        this.RecipeId = recipeId;
    }
    public String getRecipeId() {
        return RecipeId;
    }
}
