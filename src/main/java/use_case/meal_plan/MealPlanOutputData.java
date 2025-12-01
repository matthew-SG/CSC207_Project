package use_case.meal_plan;

import java.util.List;
import java.util.Map;

/**
 * Output Data for the Meal Plan Use Case.
 */
public class MealPlanOutputData {

    private final String[] recipeNames;
    private final String[] recipeImages;
    private final List<List<String[]>> recipeIngredients;
    private final List<Map<String, Double>> recipeNutritionalValues;

    public MealPlanOutputData(String[] recipeNames, String[] recipeImages, List<List<String[]>> recipeIngredients,
                              List<Map<String, Double>> recipeNutritionalValues) {
        this.recipeNames = recipeNames;
        this.recipeImages = recipeImages;
        this.recipeIngredients = recipeIngredients;
        this.recipeNutritionalValues = recipeNutritionalValues;
    }

    public String[] getRecipeNames() {
        return recipeNames;
    }

    public String[] getRecipeImages() {
        return recipeImages;
    }

    public List<List<String[]>> getIngredients() {
        return recipeIngredients;
    }

    public List<Map<String, Double>> getNutritionalValues() {
        return recipeNutritionalValues;
    }

}
