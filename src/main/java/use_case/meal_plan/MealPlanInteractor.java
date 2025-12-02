package use_case.meal_plan;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import entities.Ingredient;
import entities.MealPlan;
import entities.Recipe;

/**
 * The Meal Plan Use Case Interactor.
 */
public class MealPlanInteractor implements MealPlanInputBoundary {
    private static final int MEAL_PLAN_SIZE = 3;
    private final MealPlanUserDataAccessInterface userDataAccessObject;
    private final MealPlanOutputBoundary mealPlanPresenter;
    private MealPlanGeneratorStrategy generationStrategy;

    public MealPlanInteractor(MealPlanUserDataAccessInterface userDataAccessObject,
                              MealPlanOutputBoundary mealPlanPresenter) {
        this.userDataAccessObject = userDataAccessObject;
        this.mealPlanPresenter = mealPlanPresenter;
    }

    /**
     * Executes the meal plan generator use case.
     * @param mealPlanInputData the input data
     */
    public void execute(MealPlanInputData mealPlanInputData) {
        final List<Recipe> savedRecipes = userDataAccessObject.getSavedRecipes();
        final String inputCalories = mealPlanInputData.getTargetCalories();
        final String inputProtein = mealPlanInputData.getTargetProtein();
        final String inputCarbs = mealPlanInputData.getTargetCarbs();
        final String inputFats = mealPlanInputData.getTargetFats();
        final MealPlan mealPlan;
        final String[] recipeNames = new String[MEAL_PLAN_SIZE];
        final String[] recipeImages = new String[MEAL_PLAN_SIZE];
        final List<List<String[]>> recipeIngredients = new ArrayList<>();
        final List<Map<String, Double>> recipeNutritionalValues = new ArrayList<>();

        if (savedRecipes.size() < MEAL_PLAN_SIZE) {
            mealPlanPresenter.prepareFailView(
                    "At least 3 saved recipes must be saved for meal plan generation!", null);
        }
        else if (!isDouble(inputCalories) || !isDouble(inputProtein) || !isDouble(inputCarbs) || !isDouble(inputFats)) {
            mealPlanPresenter.prepareFailView(null, "All input values must be numerical!");
        }
        else {
            final double targetCalories = Double.parseDouble(inputCalories);
            final double targetProtein = Double.parseDouble(inputProtein);
            final double targetCarbs = Double.parseDouble(inputCarbs);
            final double targetFats = Double.parseDouble(inputFats);
            if (targetCalories < 0 || targetProtein < 0 || targetCarbs < 0 || targetFats < 0) {
                mealPlanPresenter.prepareFailView(null, "All input values must be non-negative!");
            }
            else {
                // Sets the generation strategy for the meal plan
                generationStrategy = new LowestNutritionalErrorStrategy();
                // Computes the recipes according to the selected strategy
                final List<Recipe> mealPlanRecipes = computeBestFittingRecipes(savedRecipes, targetCalories,
                        targetProtein, targetCarbs, targetFats);
                mealPlan = new MealPlan(mealPlanRecipes, targetCalories, targetProtein, targetCarbs, targetFats);

                userDataAccessObject.saveMealPlan(mealPlan);
                buildMealPlanOutput(mealPlanRecipes, recipeNames, recipeImages, recipeIngredients,
                        recipeNutritionalValues);
                final MealPlanOutputData mealPlanOutputData = new MealPlanOutputData(recipeNames, recipeImages,
                        recipeIngredients, recipeNutritionalValues);
                mealPlanPresenter.prepareSuccessView(mealPlanOutputData);
            }

        }
    }

    /**
     * Helper function that builds the parameters for output data.
     * @param mealPlanRecipes the recipes to be displayed
     * @param recipeNames the names of recipes to be displayed
     * @param recipeImages the image path of the recipes to be displayed
     * @param recipeIngredients the ingredients of the recipes to be displayed
     * @param recipeNutritionalValues the nutritional values of the recipes to be displayed
     */
    private static void buildMealPlanOutput(List<Recipe> mealPlanRecipes, String[] recipeNames, String[] recipeImages,
                                            List<List<String[]>> recipeIngredients,
                                            List<Map<String, Double>> recipeNutritionalValues) {
        int i = 0;

        for (Recipe recipe : mealPlanRecipes) {
            recipeNames[i] = recipe.getRecipeName();
            recipeImages[i] = recipe.getRecipeImage();
            recipeIngredients.add(toOrderedString(recipe.getIngredients()));
            recipeNutritionalValues.add(recipe.getNutritionalValues());
            i++;

        }
    }

    /**
     * Helper function that checks if a String is a double.
     * @param str the String to be checked
     * @return whether the String can be represented as a double
     */
    private static boolean isDouble(String str) {
        boolean result;
        try {
            Double.parseDouble(str);
            result = true;
        }
        catch (NumberFormatException ex) {
            result = false;
        }
        return result;
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

    /**
     * Helper function that computes the best fitting recipe triplet for the designated meal plan.
     * @param recipes list of possible recipes (with recipe size > MEAL_PLAN_SIZE)
     * @param targetCalories the target calories
     * @param targetProtein the target protein
     * @param targetCarbs the target carbs
     * @param targetFats the target fats
     * @return the best three recipes that match the meal plan target as close as possible
     */
    private List<Recipe> computeBestFittingRecipes(List<Recipe> recipes, double targetCalories,
                                                          double targetProtein, double targetCarbs, double targetFats) {
        // Gathers all unique combinations of possible recipe triplets
        final List<List<Recipe>> recipeTriplets = createTripletCombinations(recipes);

        // Uses the strategy to generate which recipes go into the meal plan
        return generationStrategy.generateMealPlan(recipeTriplets, targetCalories, targetProtein, targetCarbs,
                targetFats);

    }

    /**
     * Helper function that creates the n choose MEAL_PLAN_SIZE combinations of recipe triplets out of the overall
     *      list of recipes, where n = recipe.size() > MEAL_PLAN_SIZE.
     * @param recipes the overall/total list of individual recipes
     * @return the unique triplet combinations of recipes
     */
    private static List<List<Recipe>> createTripletCombinations(List<Recipe> recipes) {
        final ArrayList<List<Recipe>> result = new ArrayList<>();
        final ArrayList<Recipe> recipeTriplet = new ArrayList<>();
        final int n = recipes.size();

        for (int i = 0; i < n - 2; i++) {
            recipeTriplet.add(recipes.get(i));

            for (int j = i + 1; j < n - 1; j++) {
                recipeTriplet.add(recipes.get(j));

                for (int k = j + 1; k < n; k++) {
                    recipeTriplet.add(recipes.get(k));
                    final List<Recipe> recipeTripletCopy = new ArrayList<>(recipeTriplet);
                    result.add(recipeTripletCopy);
                    recipeTriplet.remove(2);
                    
                }
                recipeTriplet.remove(1);
            }
            recipeTriplet.remove(0);
        }
        return result;
    }

}
