package use_case.meal_plan;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import entities.Ingredient;
import entities.Recipe;

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

    /**
     * Builder for a meal plan output data object.
     */
    public static class Builder {
        private static final int MEAL_PLAN_SIZE = 3;
        private String[] recipeNames = new String[MEAL_PLAN_SIZE];
        private String[] recipeImages = new String[MEAL_PLAN_SIZE];
        private List<List<String[]>> recipeIngredients = new ArrayList<>();
        private List<Map<String, Double>> recipeNutritionalValues = new ArrayList<>();

        /**
         * Sets the list of recipe names for the meal plan output.
         * @param mealPlanRecipes the recipes of the input meal plan
         * @return this builder
         */
        public Builder buildRecipeNames(List<Recipe> mealPlanRecipes) {
            int i = 0;
            for (Recipe recipe : mealPlanRecipes) {
                recipeNames[i] = recipe.getRecipeName();
                i++;
            }
            return this;
        }

        /**
         * Sets the list of images for the meal plan output.
         * @param mealPlanRecipes the list of recipes in the meal plan
         * @return this builder
         */
        public Builder buildRecipeImages(List<Recipe> mealPlanRecipes) {
            int i = 0;
            for (Recipe recipe : mealPlanRecipes) {
                recipeImages[i] = recipe.getRecipeImage();
                i++;
            }
            return this;
        }

        /**
         * Sets the list of ingredients for each recipe for the meal plan output.
         * @param mealPlanRecipes the list of recipes in the meal plan
         * @return this builder
         */
        public Builder buildRecipeIngredients(List<Recipe> mealPlanRecipes) {
            for (Recipe recipe : mealPlanRecipes) {
                recipeIngredients.add(toOrderedString(recipe.getIngredients()));
            }
            return this;
        }

        /**
         * Sets the nutritional values for the meal plan output.
         * @param mealPlanRecipes the list of recipes in the meal plan
         * @return this builder
         */
        public Builder buildRecipeNutritionalValues(List<Recipe> mealPlanRecipes) {
            for (Recipe recipe : mealPlanRecipes) {
                recipeNutritionalValues.add(recipe.getNutritionalValues());
            }
            return this;
        }

        /**
         * Builds and returns the meal plan output data.
         * @return the meal plan output data
         */
        public MealPlanOutputData build() {
            return new MealPlanOutputData(recipeNames, recipeImages, recipeIngredients, recipeNutritionalValues);
        }

        /**
         * Converts a list of ingredients into a list of string arrays.
         * @param ingredients the list of ingredients to be converted
         * @return the list string array representation of the ingredients
         */
        private static List<String[]> toOrderedString(List<Ingredient> ingredients) {
            final List<String[]> result = new ArrayList<>();
            for (Ingredient ingredient : ingredients) {
                final String[] ingredientEntry = new String[MEAL_PLAN_SIZE];
                ingredientEntry[0] = ingredient.getName();
                ingredientEntry[1] = ingredient.getQuantity() + "";
                ingredientEntry[2] = ingredient.getUnit();
                result.add(ingredientEntry);
            }
            return result;
        }
    }

}
