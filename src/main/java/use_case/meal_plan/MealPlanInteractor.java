package use_case.meal_plan;

import entities.Ingredient;
import entities.MealPlan;
import entities.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The Meal Plan Use Case Interactor
 */
public class MealPlanInteractor implements MealPlanInputBoundary{
    private final MealPlanUserDataAccessInterface userDataAccessObject;
    private final MealPlanOutputBoundary mealPlanPresenter;

    public MealPlanInteractor(MealPlanUserDataAccessInterface userDataAccessObject,
                              MealPlanOutputBoundary mealPlanPresenter) {
        this.userDataAccessObject = userDataAccessObject;
        this.mealPlanPresenter = mealPlanPresenter;
    }

    public void execute(MealPlanInputData mealPlanInputData) {
        final List<Recipe> savedRecipes = userDataAccessObject.getSavedRecipes();
        final int calories = mealPlanInputData.getTargetCalories();
        final int protein = mealPlanInputData.getTargetProtein();
        final int carbs = mealPlanInputData.getTargetCarbs();
        final int fats = mealPlanInputData.getTargetFats();
        String[] recipeNames = new String[3];
        String[] recipeImages = new String[3];
        List<List<Ingredient>> recipeIngredients = new ArrayList<>();
        List<Map<String, Double>> recipeNutritionalValues = new ArrayList<>();
        if (savedRecipes.size() < 3) {
            mealPlanPresenter.prepareFailView("At least 3 saved recipes must be saved for meal plan generation.");
        } else if (calories < 0 || protein < 0 || carbs < 0 || fats < 0) {
            mealPlanPresenter.prepareFailView("All input values must be positive.");
        } else if (savedRecipes.size() == 3) {
            MealPlan mealPlan = new MealPlan(savedRecipes, calories, protein, carbs, fats);
            userDataAccessObject.saveMealPlan(mealPlan);
            int i = 0;
            for (Recipe recipe : savedRecipes) {
                recipeNames[i] = recipe.getRecipeName();
                recipeImages[i] = recipe.getRecipeImage();
                recipeIngredients.add(recipe.getIngredients());
                recipeNutritionalValues.add(recipe.getNutritionalValues());
                i++;
            }

            MealPlanOutputData mealPlanOutputData = new MealPlanOutputData(recipeNames, recipeImages, recipeIngredients,
                    recipeNutritionalValues);
            mealPlanPresenter.prepareSuccessView(mealPlanOutputData);
        } else {

        }
    }

    /**
     * Helper function that computes the best fitting recipes for the designated meal plan
     * @param recipes the user's currently saved recipes (with recipes.size() > 3)
     * @return the three recipes that fit closest to the meal plan parameters
     */
    private List<Recipe> computeBestFittingRecipes(List<Recipe> recipes) {
        assert recipes.size() > 3;

        List<List<Recipe>> recipeTriplets = createTripletCombinations(recipes);

        int currentTotal = 0;
        int lowestTotal;
        List<Recipe> bestFittingRecipes = new ArrayList<>();

        for (List<Recipe> recipeTriplet : recipeTriplets) {
            for (Recipe recipe : recipeTriplet) {
                Map<String, Double> recipeNutritionalValues = recipe.getNutritionalValues();

            }
        }

    }

    /**
     * Helper function that creates the n choose 3 combinations of recipe triplets out of the overall list of recipes
     * recipe, where n = recipe.size() > 3.
     * @param recipes the overall/total list of individual recipes
     * @return the unique triplet combinations of recipes
     */
    private static List<List<Recipe>> createTripletCombinations(List<Recipe> recipes) {
        assert recipes.size() > 3;
        ArrayList<List<Recipe>> recipeTriplets = new ArrayList<>();
        ArrayList<Recipe> recipeTriplet = new ArrayList<>();
        int n = recipes.size();

        for (int i = 0; i < n - 2; i++) {
            recipeTriplet.add(recipes.get(i));

            for (int j = i + 1; j < n - 1; j++) {
                recipeTriplet.add(recipes.get(j));

                for (int k = j + 1; k < n; k++) {
                    recipeTriplet.add(recipes.get(k));
                    recipeTriplets.add(recipeTriplet);
                    recipeTriplet.remove(2);
                    
                }
                recipeTriplet.remove(1);
            }
            recipeTriplet.remove(0);
        }
        return recipeTriplets;
    }

}
