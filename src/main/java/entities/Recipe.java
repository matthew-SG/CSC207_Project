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
    private Map<String, Double> nutritionalValues;
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

    public void addIngredient(Ingredient ingredient) { ingredients.add(ingredient); }

    public Map<String, Double> getNutritionalValues() { return nutritionalValues; }

    public void addNutritionalValue(String name, double value) { nutritionalValues.put(name, value); }

    public String getMealType() { return mealType; }

    public String getSteps() { return steps; }
    public void setIngredients(List<Ingredient> ingredients) { this.ingredients = ingredients; }
    public void setSteps(String steps) { this.steps = steps; }
    @Override
    public String toString() {
        return recipeName;
    }
}
