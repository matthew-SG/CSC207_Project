package use_case.approve_recipe;

/**
 * Input data for approving a recipe.
 */
public class ApproveRecipeInputData {
    private final int recipeId;
    private final String username;

    public ApproveRecipeInputData(int recipeId, String username) {
        this.recipeId = recipeId;
        this.username = username;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public String getUsername() {
        return username;
    }
}
