package use_case.likedRecipeList;

public class DeleteLikedRecipeInputData {
    private final String RecipeId;

    public DeleteLikedRecipeInputData(String recipeId) {
        this.RecipeId = recipeId;
    }

    public String getRecipeId() {

        return RecipeId;
    }

}
