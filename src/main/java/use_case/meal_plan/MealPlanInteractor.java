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
        final String inputCalories = mealPlanInputData.getTargetCalories();
        final String inputProtein = mealPlanInputData.getTargetProtein();
        final String inputCarbs = mealPlanInputData.getTargetCarbs();
        final String inputFats = mealPlanInputData.getTargetFats();
        MealPlan mealPlan;
        String[] recipeNames = new String[3];
        String[] recipeImages = new String[3];
        List<List<String[]>> recipeIngredients = new ArrayList<>();
        List<Map<String, Double>> recipeNutritionalValues = new ArrayList<>();

        if (savedRecipes.size() < 3) {
            mealPlanPresenter.prepareFailView("At least 3 saved recipes must be saved for meal plan generation.",
                    null);
        } else if (!isDouble(inputCalories) || !isDouble(inputProtein) || !isDouble(inputCarbs) || !isDouble(inputFats)) {
            mealPlanPresenter.prepareFailView(null, "All input values must be numerical.");
        } else {
            double targetCalories = Double.parseDouble(inputCalories);
            double targetProtein = Double.parseDouble(inputProtein);
            double targetCarbs = Double.parseDouble(inputCarbs);
            double targetFats = Double.parseDouble(inputFats);
            if (targetCalories < 0 || targetProtein < 0 || targetCarbs < 0 || targetFats < 0) {
                mealPlanPresenter.prepareFailView(null, "All input values must be non-negative.");
            } else {
                List<Recipe> mealPlanRecipes = computeBestFittingRecipes(savedRecipes, targetCalories, targetProtein,
                        targetCarbs, targetFats);
                mealPlan = new MealPlan(mealPlanRecipes, targetCalories, targetProtein, targetCarbs, targetFats);

                userDataAccessObject.saveMealPlan(mealPlan);
                int i = 0;

                for (Recipe recipe : savedRecipes) {
                    recipeNames[i] = recipe.getRecipeName();
                    recipeImages[i] = recipe.getRecipeImage();
                    recipeIngredients.add(toOrderedString(recipe.getIngredients()));
                    recipeNutritionalValues.add(recipe.getNutritionalValues());
                    i++;

                }
                MealPlanOutputData mealPlanOutputData = new MealPlanOutputData(recipeNames, recipeImages, recipeIngredients,
                        recipeNutritionalValues);
                mealPlanPresenter.prepareSuccessView(mealPlanOutputData);
            }

        }
    }

    /**
     * Helper function that checks if a String is a double.
     * @param str the String to be checked
     * @return whether the String can be represented as a double
     */
    private static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException _) {
            return false;
        }
    }

    /**
     * Converts a list of ingredients into a list of string arrays
     * @param ingredients the list of ingredients to be converted
     * @return the list string array representation of the ingredients
     */
    private static List<String[]> toOrderedString(List<Ingredient> ingredients) {
        List<String[]> list = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            String[] ingredientEntry = new String[3];
            ingredientEntry[0] = ingredient.getName();
            ingredientEntry[1] = ingredient.getQuantity() + "";
            ingredientEntry[2] = ingredient.getUnit();
            list.add(ingredientEntry);
        }
        return list;
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
    private static List<Recipe> computeBestFittingRecipes(List<Recipe> recipes, double targetCalories,
                                                          double targetProtein, double targetCarbs, double targetFats) {
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
        double currentCalories;
        double currentProtein;
        double currentCarbs;
        double currentFats;
        double currentError;
        double lowestError = -1;
        List<Recipe> bestFittingTriplet = new ArrayList<>();

        for (List<Recipe> recipeTriplet : recipeTriplets) {
            assert recipeTriplet.size() == 3;
            currentCalories = 0;
            currentProtein = 0;
            currentCarbs = 0;
            currentFats = 0;

            for (Recipe recipe : recipeTriplet) {
                Map<String, Double> recipeNutritionalValues = recipe.getNutritionalValues();
                currentCalories += recipeNutritionalValues.get("calories");
                currentProtein += recipeNutritionalValues.get("protein");
                currentCarbs += recipeNutritionalValues.get("carbs");
                currentFats += recipeNutritionalValues.get("fats");
            }
            currentError = Math.abs(currentCalories - targetCalories) + Math.abs(currentProtein - targetProtein) + (
                    Math.abs(currentCarbs - targetCarbs) + Math.abs(currentFats - targetFats));
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
