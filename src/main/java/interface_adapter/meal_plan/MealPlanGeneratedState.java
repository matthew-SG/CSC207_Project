package interface_adapter.meal_plan;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The state for the MealPlanGenerated ViewModel
 */
public class MealPlanGeneratedState {
    private String[] recipeNames = new String[0];
    private String[] recipeImages = new String[0];
    private List<List<String[]>> recipeIngredients = new ArrayList<>();
    private List<Map<String, Double>> recipeNutritionalValues = new ArrayList<>();

    public String[] getRecipeNames() { return recipeNames; }

    public void setRecipeNames(String[] recipeNames) { this.recipeNames = recipeNames; }

    public String[] getRecipeImages() { return recipeImages; }

    public void setRecipeImages(String[] recipeImages) { this.recipeImages = recipeImages; }

    public List<List<String[]>> getRecipeIngredients() { return recipeIngredients; }

    public void setRecipeIngredients(List<List<String[]>> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }

    public List<Map<String, Double>> getRecipeNutritionalValues() { return recipeNutritionalValues; }

    public void setRecipeNutritionalValues(List<Map<String, Double>> recipeNutritionalValues) {
        this.recipeNutritionalValues = recipeNutritionalValues;
    }
}
