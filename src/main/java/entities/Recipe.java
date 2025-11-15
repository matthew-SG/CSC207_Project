package entities;
import java.util.ArrayList;
import java.util.Map;

public class Recipe {
    private int recipeId;
    private String recipeName;
    private String recipeImage;
    private ArrayList<Ingredient> ingredients;
    private Map<String, Double> nutritionalValues;
    private String mealType;

    public String getRecipeName() {
        return recipeName;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public String getRecipeImage() {
        return recipeImage;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public Map<String, Double> getNutritionalValues() {
        return nutritionalValues;
    }

    public String getMealType() {
        return mealType;
    }
}