package use_case.likedRecipeList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import entities.Ingredient;
import entities.InstructionStep;
import entities.Recipe;
import entities.RecipeInstructions;
import entities.User;
import use_case.step_by_step.StepByStepInputData;

/**
 * Interactor for the Liked Recipe use case.
 * Handles adding, deleting, and retrieving liked recipes, as well as
 * fetching recipe instructions and adding ingredients to the grocery list.
 */
public class LikedRecipeInteractor implements LikedRecipeInputBoundary {
    private final LikedRecipeDataAccessInterface dataAccess;
    private final LikedRecipeOutputBoundary presenter;

    /**
     * Constructs a new LikedRecipeInteractor.
     * @param dataAccess the data access interface for recipe and user data
     * @param presenter the output boundary for presenting results to the view
     */
    public LikedRecipeInteractor(LikedRecipeDataAccessInterface dataAccess,
                                 LikedRecipeOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    /**
     * Adds a recipe to the current user's liked recipes list.
     * Validates that the recipe exists and is not already saved before adding.
     * @param inputData contains the recipe ID and name to add
     */
    @Override
    public void addLikedRecipe(LikedRecipeInputData inputData) {
        try {
            final String username = dataAccess.getCurrentUsername();

            // Fetch the complete recipe details by ID
            final Recipe recipe = dataAccess.getRecipeById(inputData.getId());

            if (recipe == null) {
                presenter.prepareFailView("Recipe not found with ID: " + inputData.getId());
                return;
            }

            // Retrieve the user from the data store
            final User user = dataAccess.getUser(username);
            if (user == null) {
                presenter.prepareFailView("User not found: " + username);
                return;
            }

            // Check if the recipe is already in the user's saved recipes
            final boolean alreadySaved = user.getSavedRecipes().stream()
                    .anyMatch(recipe1 -> recipe1.getRecipeId() == recipe.getRecipeId());

            if (!alreadySaved) {
                // Add recipe to user's in-memory list
                user.getSavedRecipes().add(recipe);
                // Persist the change to storage
                dataAccess.saveLikedRecipe(username, recipe);
            }

            // Refresh the liked recipes view
            loadLikedRecipes();

        }
        catch (Exception ex) {
            presenter.prepareFailView("Error adding recipe: " + ex.getMessage());
        }
    }

    /**
     * Removes a recipe from the current user's liked recipes list.
     * @param inputData contains the recipe ID to delete
     */
    @Override
    public void deleteLikedRecipe(LikedRecipeInputData inputData) {
        try {
            final String username = dataAccess.getCurrentUsername();

            // Retrieve the user from the data store
            final User user = dataAccess.getUser(username);
            if (user == null) {
                presenter.prepareFailView("User not found: " + username);
                return;
            }

            // Remove the recipe from the user's in-memory list
            user.getSavedRecipes().removeIf(recipe -> recipe.getRecipeId() == inputData.getId());

            // Persist the deletion to storage
            dataAccess.deleteLikedRecipe(username, inputData.getId());

            // Refresh the liked recipes view
            loadLikedRecipes();

        }
        catch (Exception ex) {
            presenter.prepareFailView("Error deleting recipe: " + ex.getMessage());
        }
    }

    /**
     * Loads and displays all liked recipes for the current user.
     * Extracts recipe details including IDs, names, images, nutrition, and ingredients.
     * Steps are fetched on-demand via the handsfree method.
     */
    @Override
    public void loadLikedRecipes() {
        try {
            final String username = dataAccess.getCurrentUsername();
            final List<Recipe> recipes = dataAccess.getLikedRecipes(username);

            // Initialize arrays and lists for output data
            final int[] recipeIds = new int[recipes.size()];
            final String[] recipeNames = new String[recipes.size()];
            final String[] recipeImages = new String[recipes.size()];
            final List<Map<String, Double>> recipeNutrition = new ArrayList<>();
            final List<List<String[]>> recipeIngredients = new ArrayList<>();
            final List<List<String[]>> recipeSteps = new ArrayList<>();

            // Extract data from each recipe
            for (int i = 0; i < recipes.size(); i++) {
                final Recipe recipe = recipes.get(i);

                // Extract recipe ID
                recipeIds[i] = recipe.getRecipeId();

                // Extract recipe name
                recipeNames[i] = recipe.getRecipeName();

                // Extract image URL
                recipeImages[i] = recipe.getRecipeImage();

                // Extract nutritional information
                recipeNutrition.add(recipe.getNutritionalValues());

                // Convert ingredients to string array format
                final List<String[]> ingredientsList = new ArrayList<>();
                for (Ingredient ingredient : recipe.getIngredients()) {
                    final String[] ingredientData = new String[3];
                    ingredientData[0] = ingredient.getName();
                    ingredientData[1] = String.valueOf(ingredient.getQuantity());
                    ingredientData[2] = ingredient.getUnit();
                    ingredientsList.add(ingredientData);
                }
                recipeIngredients.add(ingredientsList);

                // Steps are loaded on-demand, so add empty placeholder
                recipeSteps.add(new ArrayList<>());
            }

            // Create output data and send to presenter
            final LikedRecipeOutputData outputData = new LikedRecipeOutputData(
                    recipeIds,
                    recipeNames,
                    recipeImages,
                    recipeNutrition,
                    recipeIngredients,
                    recipeSteps
            );
            presenter.prepareLikedRecipeView(outputData);

        }
        catch (Exception ex) {
            presenter.prepareFailView("Error loading recipes: " + ex.getMessage());
        }
    }

    /**
     * Fetches step-by-step instructions for a recipe and prepares the hands-free view.
     * Instructions are retrieved from the Spoonacular API via the data access layer.
     * @param inputData contains the recipe ID to fetch instructions for
     * @return the list of instruction steps, or an empty list if an error occurs
     */
    @Override
    public List<InstructionStep> handsfree(LikedRecipeInputData inputData) {
        try {
            final int recipeId = inputData.getId();

            // Fetch instructions from the API through the data access layer
            final List<InstructionStep> instructions = dataAccess.getAnalyzedInstructions(recipeId);

            // Wrap instructions in an entity object
            final RecipeInstructions recipeInstructions = new RecipeInstructions(instructions);

            // Prepare the step-by-step view starting at the first step (index 0)
            final StepByStepInputData stepByStepInputData = new StepByStepInputData(recipeInstructions, 0);
            presenter.prepareHandsfree(stepByStepInputData);

            return instructions;
        }
        catch (Exception ex) {
            presenter.prepareFailView("Error loading instructions: " + ex.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Adds all ingredients from a liked recipe to the user's grocery list.
     * Ingredients are merged intelligently (combining quantities for duplicate items).
     * @param inputData contains the recipe ID whose ingredients should be added
     */
    @Override
    public void addIngredientsToGrocery(LikedRecipeInputData inputData) {
        try {
            final String username = dataAccess.getCurrentUsername();

            // Find the target recipe in the user's liked recipes
            final List<Recipe> liked = dataAccess.getLikedRecipes(username);
            Recipe target = null;
            for (Recipe r : liked) {
                if (r.getRecipeId() == inputData.getId()) {
                    target = r;
                    break;
                }
            }

            // Validate that the recipe was found
            if (target == null) {
                presenter.prepareFailView("Recipe not found with ID: " + inputData.getId());
                return;
            }

            // Extract ingredients from the recipe
            final List<Ingredient> ingredients = target.getIngredients();
            if (ingredients == null || ingredients.isEmpty()) {
                presenter.prepareFailView("This recipe has no ingredients to add.");
                return;
            }

            // Add ingredients to the grocery list (merging duplicates)
            dataAccess.addIngredientsToGroceryList(username, ingredients);

            // Refresh the liked recipes view
            loadLikedRecipes();

        }
        catch (Exception ex) {
            presenter.prepareFailView("Error adding ingredients to grocery list: " + ex.getMessage());
        }
    }
}
