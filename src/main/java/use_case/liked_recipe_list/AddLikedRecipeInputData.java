package use_case.liked_recipe_list;

public class AddLikedRecipeInputData {
    private final String recipeId;

    public AddLikedRecipeInputData(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeId() {
        return recipeId;
    }
}

