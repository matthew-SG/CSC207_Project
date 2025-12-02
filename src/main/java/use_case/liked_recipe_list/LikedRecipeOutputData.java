package use_case.liked_recipe_list;

import java.util.List;
import java.util.Map;

public class LikedRecipeOutputData {

    private final int[] recipeIds;
    private final String[] recipeNames;
    private final String[] recipeImages;
    private final List<Map<String, Double>> recipeNutrition;
    private final List<List<String[]>> recipeIngredients;
    private final List<List<String[]>> recipeSteps;

    public LikedRecipeOutputData(int[] recipeIds,
                                 String[] recipeNames,
                                 String[] recipeImages,
                                 List<Map<String, Double>> recipeNutrition,
                                 List<List<String[]>> ingredients,
                                 List<List<String[]>> steps) {
        this.recipeIds = recipeIds;
        this.recipeNames = recipeNames;
        this.recipeImages = recipeImages;
        this.recipeNutrition = recipeNutrition;
        this.recipeIngredients = ingredients;
        this.recipeSteps = steps;
    }

    public int[] getRecipeIds() {
        return recipeIds;
    }

    public String[] getRecipeNames() {
        return recipeNames;
    }

    public String[] getRecipeImages() {
        return recipeImages;
    }

    public List<Map<String, Double>> getRecipeNutrition() {
        return recipeNutrition;
    }

    public List<List<String[]>> getRecipeIngredients() {
        return recipeIngredients;
    }

    public List<List<String[]>> getRecipeSteps() {
        return recipeSteps;
    }
}
