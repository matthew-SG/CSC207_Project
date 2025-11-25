package use_case.likedRecipeList;

import entities.Recipe;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LikedRecipeOutputData {
    private final String[] recipeNames;
    private final List<List<String[]>> recipeIngredients;
    private final List<List<String[]>> recipeSteps;

    public LikedRecipeOutputData(String[] recipeNames, List<List<String[]>> ingredients, List<List<String[]>> steps) {
        this.recipeNames = recipeNames;
        this.recipeIngredients = ingredients;
        this.recipeSteps = steps;
    }

    public String[] getRecipeNames() {
        return recipeNames;
    }

    public List<List<String[]>> getRecipeIngredients() {
        return recipeIngredients;
    }

    public List<List<String[]>> getRecipeSteps() {
        return recipeSteps;
    }
}
