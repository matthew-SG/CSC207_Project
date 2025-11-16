package entities;
import java.util.ArrayList;
import java.util.HashMap;
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
    private String steps;

    public Recipe(int recipeId, String recipeName, String recipeImage, String mealType) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.recipeImage = recipeImage;
        this.ingredients = new ArrayList<>();
        this.mealType = mealType;
        nutritionalValues = new HashMap<>();
    }

    public int getRecipeId() { return recipeId; }

    public String getRecipeName() { return recipeName; }

    public String getRecipeImage() { return recipeImage; }

    public List<Ingredient> getIngredients() { return ingredients; }

    public int getCalories() { return calories; }

    public Map<String, Map<Integer, String>> getNutritionalValues() { return nutritionalValues; }

    public String getMealType() { return mealType; }

    public String getSteps() { return steps; }
}
