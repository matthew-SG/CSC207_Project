package use_case.load_meal_plan;

import entities.Ingredient;
import entities.MealPlan;
import entities.Recipe;
import use_case.meal_plan.MealPlanOutputData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Interactor for the Load Meal Plan Use Case
 */
public class LoadMealPlanInteractor implements LoadMealPlanInputBoundary {
    private final LoadMealPlanDataAccessInterface dataAccessObject;
    private final LoadMealPlanOutputBoundary loadMealPlanPresenter;

    public LoadMealPlanInteractor(LoadMealPlanDataAccessInterface dataAccessObject,
                                  LoadMealPlanOutputBoundary loadMealPlanPresenter) {
        this.dataAccessObject = dataAccessObject;
        this.loadMealPlanPresenter = loadMealPlanPresenter;
    }

    public void execute(LoadMealPlanInputData loadMealPlanInputData) {
        final int index = loadMealPlanInputData.getIndex();
        final List<MealPlan> mealPlans = dataAccessObject.getMealPlans();
        String[] recipeNames = new String[3];
        String[] recipeImages = new String[3];
        List<List<String[]>> recipeIngredients = new ArrayList<>();
        List<Map<String, Double>> recipeNutritionalValues = new ArrayList<>();

        final MealPlan mealPlan = mealPlans.get(index);
        int i = 0;
        for (Recipe recipe : mealPlan.getRecipes()) {
            recipeNames[i] = recipe.getRecipeName();
            recipeImages[i] = recipe.getRecipeImage();
            recipeIngredients.add(toOrderedString(recipe.getIngredients()));
            recipeNutritionalValues.add(recipe.getNutritionalValues());
            i++;

        }
        MealPlanOutputData mealPlanOutputData = new MealPlanOutputData(recipeNames, recipeImages, recipeIngredients,
                recipeNutritionalValues);
        loadMealPlanPresenter.prepareSuccessView(mealPlanOutputData);
    }

    /**
     * Converts a list of ingredients into a list of string arrays
     * @param ingredients the list of ingredients to be converted
     * @return the list string array representation of the ingredients
     */
    private static List<String[]> toOrderedString(List<Ingredient> ingredients) {
        List<String[]> result = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            String[] ingredientEntry = new String[3];
            ingredientEntry[0] = ingredient.getName();
            ingredientEntry[1] = ingredient.getQuantity() + "";
            ingredientEntry[2] = ingredient.getUnit();
            result.add(ingredientEntry);
        }
        return result;
    }
}
