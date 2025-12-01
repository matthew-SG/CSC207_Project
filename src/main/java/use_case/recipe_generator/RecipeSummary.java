package use_case.recipe_generator;

    // the presenter constructs RecipeSummary objects from the Recipe entity
    // the view model only sees RecipeSummary objects not the full Recipe entity
    // Clean Architecture: This is a boundary model sitting between the use case and UI layer
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



