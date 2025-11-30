package interface_adapter.likedRecipeList;

import java.util.List;

public class LikedRecipeListState {

    private List<Integer> recipeIds;
    private List<String> recipeNames;

    private int selectedRecipe;
    private String selectedRecipeName;

    public List<Integer> getRecipeIds() {
        return recipeIds;
    }

    public List<String> getRecipeNames() {
        return recipeNames;
    }

    public void setRecipeIds(List<Integer> recipeIds) {
        this.recipeIds = recipeIds;
    }

    public void setRecipeNames(List<String> recipeNames) {
        this.recipeNames = recipeNames;
    }

    public int getSelectedRecipe() {
        return selectedRecipe;
    }

    public String getSelectedRecipeName() {
        return selectedRecipeName;
    }

    public void setSelectedRecipe(int recipeId) {
        this.selectedRecipe = recipeId;
    }

    public void setSelectedRecipeName(String recipeName) {
        this.selectedRecipeName = recipeName;
    }
}
