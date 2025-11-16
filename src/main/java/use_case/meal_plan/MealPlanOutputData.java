package use_case.meal_plan;

import entities.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Output Data for the Meal Plan Use Case
 */
public class MealPlanOutputData {

    private final String[] recipeNames;
    private final String[] recipeImages;
    private final List<List<Ingredient>> recipeIngredients;
    private final List<Map<String, Map<Integer, String>>> recipeNutritionalValues;

    public MealPlanOutputData(String[] recipeNames, String[] recipeImages, List<List<Ingredient>> recipeIngredients,
                              List<Map<String, Map<Integer, String>>> recipeNutritionalValues) {
        this.recipeNames = recipeNames;
        this.recipeImages = recipeImages;
        this.recipeIngredients = recipeIngredients;
        this.recipeNutritionalValues = recipeNutritionalValues;
    }

    public String[] getRecipeName() { return recipeNames; }

    public String[] getRecipeImage() { return  recipeImages; }

    public List<List<Ingredient>> getIngredients() { return  recipeIngredients; }

    public List<Map<String, Map<Integer, String>>> getNutritionalValues() { return  recipeNutritionalValues; }

}
