package use_case.liked_recipe_list;

public class DeleteLikedRecipeInputData {
    private final String recipeId;

    public DeleteLikedRecipeInputData(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeId() {

        return recipeId;
    }

}
