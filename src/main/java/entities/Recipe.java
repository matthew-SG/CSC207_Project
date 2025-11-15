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
}