package use_case.likedRecipeList;

import java.util.List;
import java.util.Map;

public class LikedRecipeOutputData {
    private final String[] recipeNames;
    private final String[] recipeImages;
    private final List<Map<String, Double>> recipeNutrition;
    private final List<List<String[]>> recipeIngredients;
    private final List<List<String[]>> recipeSteps;

    public LikedRecipeOutputData(String[] recipeNames,
                                 String[] recipeImages,
                                 List<Map<String, Double>> recipeNutrition,
                                 List<List<String[]>> ingredients,
                                 List<List<String[]>> steps) {
        this.recipeNames = recipeNames;
        this.recipeImages = recipeImages;
        this.recipeNutrition = recipeNutrition;
        this.recipeIngredients = ingredients;
        this.recipeSteps = steps;
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