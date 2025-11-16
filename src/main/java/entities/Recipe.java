package entities;
import java.util.List;
import java.util.Map;

public class Recipe {
    private int recipeId;
    private String recipeName;
    private String recipeImage;
    private List<Ingredient> ingredients;
    private int calories;
    private Map<String, Map<Integer, String>> nutritionalValues;
    private String mealType;

    public int getRecipeId() { return recipeId; }

    public String getRecipeName() { return recipeName; }

    public String getRecipeImage() { return recipeImage; }

    public List<Ingredient> getIngredients() { return ingredients; }

    public int getCalories() { return calories; }

    public Map<String, Map<Integer, String>> getNutritionalValues() { return nutritionalValues; }

    public String getMealType() { return mealType; }
}