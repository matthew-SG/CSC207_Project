package use_case.recipe_generator;


public class RecipeSummary {
    private final String recipeName;
    private final int recipeId;
    private final String recipeImage;

    public RecipeSummary(String recipeName, int recipeId, String recipeImage) {
        this.recipeName = recipeName;
        this.recipeId = recipeId;
        this.recipeImage = recipeImage;
    }
    public String getRecipeName() {
        return recipeName;
    }
    public int getRecipeId() {
        return recipeId;
    }
    public String getRecipeImage() {
        return recipeImage;
    }
}



