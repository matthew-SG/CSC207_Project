package use_case.approve_recipe;

/**
 * Input data for declining a recipe.
 */
public class DeclineRecipeInputData {
    private final int recipeId;
    private final String username;

    public DeclineRecipeInputData(int recipeId, String username) {
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
