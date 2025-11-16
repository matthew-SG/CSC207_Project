package use_case.meal_plan;

import entities.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Output Data for the Meal Plan Use Case
 */
public class MealPlanOutputData {

    private String recipeName;
    private String recipeImage;
    private List<Ingredient> ingredients;
    private Map<String, Double> nutritionalValues;

    public MealPlanOutputData(String recipe, String recipeImage, List<Ingredient> ingredients, Map<String, Double> nutritionalValues) {
        this.recipeName = recipe;
        this.recipeImage = recipeImage;
        this.ingredients = ingredients;
        this.nutritionalValues = nutritionalValues;
    }

    public String getRecipeName() { return recipeName; }

    public String getRecipeImage() { return  recipeImage; }

    public List<Ingredient> getIngredients() { return  ingredients; }

    public Map<String, Double> getNutritionalValues() { return  nutritionalValues; }

}
