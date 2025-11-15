package entities;
import java.util.ArrayList;

public class Recipe {
    private int recipeId;
    private String recipeName;
    private String recipeImage;
    private ArrayList<Ingredient> ingredients;
    private Map<String, double> nutritionalValues;
    private String mealType;
}