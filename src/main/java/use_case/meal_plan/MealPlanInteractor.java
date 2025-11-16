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
        final double calories = mealPlanInputData.getTargetCalories();
        final double protein = mealPlanInputData.getTargetProtein();
        final double carbs = mealPlanInputData.getTargetCarbs();
        final double fats = mealPlanInputData.getTargetFats();
        MealPlan mealPlan;
        String[] recipeNames = new String[3];
        String[] recipeImages = new String[3];
        List<List<Ingredient>> recipeIngredients = new ArrayList<>();
        List<Map<String, Double>> recipeNutritionalValues = new ArrayList<>();

        if (savedRecipes.size() < 3) {
            mealPlanPresenter.prepareFailView("At least 3 saved recipes must be saved for meal plan generation.");

        } else if (calories < 0 || protein < 0 || carbs < 0 || fats < 0) {
            mealPlanPresenter.prepareFailView("All input values must be positive.");

        } else {
            if (savedRecipes.size() == 3) {
                mealPlan = new MealPlan(savedRecipes, calories, protein, carbs, fats);

            } else {
                List<Recipe> mealPlanRecipes = computeBestFittingRecipes(savedRecipes, calories, protein, carbs, fats);
                mealPlan = new MealPlan(mealPlanRecipes, calories, protein, carbs, fats);

            }
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

        }
    }

    /**
     * Helper function that computes the best fitting recipe triplet for the designated meal plan
     * @param recipes list of possible recipes (with recipe size > 3)
     * @param targetCalories the target calories
     * @param targetProtein the target protein
     * @param targetCarbs the target carbs
     * @param targetFats the target fats
     * @return the best three recipes that match the meal plan target as close as possible
     */
    private List<Recipe> computeBestFittingRecipes(List<Recipe> recipes, double targetCalories, double targetProtein,
                                                   double targetCarbs, double targetFats) {
        assert recipes.size() > 3;

        List<List<Recipe>> recipeTriplets = createTripletCombinations(recipes);

        return computeLowestNutritionalError(recipeTriplets, targetCalories, targetProtein, targetCarbs, targetFats);

    }

    /**
     * Helper function that computes the lowest nutritional error of all the recipe triplets, where the nutritional
     *      error is determined by the absolute difference between the sum of the target nutrients and the sum of the
     *      associated nutrients of the recipe triplet
     * @param recipeTriplets list of recipe triplets (recipeTriplets.size() > 1)
     * @param targetCalories the target calories
     * @param targetProtein the target protein
     * @param targetCarbs the target carbs
     * @param targetFats the target fats
     * @return the recipe triplet with the lowest nutritional error
     */
    private static List<Recipe> computeLowestNutritionalError(List<List<Recipe>> recipeTriplets, double targetCalories,
                                                              double targetProtein, double targetCarbs, double targetFats) {
        assert recipeTriplets.size() > 1;
        double targetTotal =  targetCalories + targetProtein + targetCarbs + targetFats;
        double currentTotal;
        double currentError;
        double lowestError = -1;
        List<Recipe> bestFittingTriplet = new ArrayList<>();

        for (List<Recipe> recipeTriplet : recipeTriplets) {
            assert recipeTriplet.size() == 3;
            currentTotal = 0;

            for (Recipe recipe : recipeTriplet) {
                Map<String, Double> recipeNutritionalValues = recipe.getNutritionalValues();
                double calories = recipeNutritionalValues.get("calories");
                double protein = recipeNutritionalValues.get("protein");
                double carbs = recipeNutritionalValues.get("carbs");
                double fats = recipeNutritionalValues.get("fats");
                currentTotal += calories + protein + carbs + fats;

            }
            currentError = Math.abs(targetTotal - currentTotal);
            if (currentError == 0) {
                return recipeTriplet;

            } else if (lowestError == -1 || currentError < lowestError) {
                lowestError = currentError;
                bestFittingTriplet = recipeTriplet;

            }

        }
        return bestFittingTriplet;

    }

    /**
     * Helper function that creates the n choose 3 combinations of recipe triplets out of the overall list of recipes
     *      recipe, where n = recipe.size() > 3.
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
