package entities;
import java.util.List;
import java.util.Map;

public class Recipe {
    private int recipeId;
    private String recipeName;
    private String recipeImage;
    private List<Ingredient> ingredients;
    private Map<String, Double> nutritionalValues;
    private String mealType;
    private String steps;

    public int getRecipeId() { return recipeId; }

    public String getRecipeName() { return recipeName; }

    public String getRecipeImage() { return recipeImage; }

    public List<Ingredient> getIngredients() { return ingredients; }

    public Map<String, Double> getNutritionalValues() { return nutritionalValues; }

    public String getMealType() { return mealType; }

    public String getSteps() { return steps; }
}