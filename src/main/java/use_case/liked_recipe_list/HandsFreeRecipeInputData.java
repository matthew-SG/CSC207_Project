package use_case.liked_recipe_list;

public class HandsFreeRecipeInputData {
    private final String recipeId;

    public HandsFreeRecipeInputData(String recipeId) {
        this.recipeId = recipeId;
    }
    public String getRecipeId() {
        return recipeId;
    }
}
