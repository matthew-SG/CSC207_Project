package interface_adapter.likedRecipeList;

import java.util.List;
import java.util.Map;

public class LikedRecipeListState {

    private List<Integer> recipeIds;
    private List<String> recipeNames;

    private int selectedRecipe;
    private String selectedRecipeName;

    private List<List<String[]>> recipeIngredients;
    private List<Map<String, Double>> recipeNutrition;
    private List<String> recipeImages;

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

    public List<List<String[]>> getRecipeIngredients() {
        return recipeIngredients;
    }

    public void setRecipeIngredients(List<List<String[]>> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }

    public List<Map<String, Double>> getRecipeNutrition() {
        return recipeNutrition;
    }

    public void setRecipeNutrition(List<Map<String, Double>> recipeNutrition) {
        this.recipeNutrition = recipeNutrition;
    }

    public List<String> getRecipeImages() {
        return recipeImages;
    }

    public void setRecipeImages(List<String> recipeImages) {
        this.recipeImages = recipeImages;
    }
}
