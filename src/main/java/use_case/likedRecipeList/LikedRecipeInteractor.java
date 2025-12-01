package use_case.likedRecipeList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import entities.User;
import entities.InstructionStep;
import entities.Recipe;
import entities.Ingredient;
import entities.RecipeInstructions;
import use_case.step_by_step.StepByStepInputData;

/**
 * Interactor for the Liked Recipe use case.
 * Handles adding, deleting, and retrieving liked recipes.
 */
public class LikedRecipeInteractor implements LikedRecipeInputBoundary {
    private final LikedRecipeDataAccessInterface dataAccess;
    private final LikedRecipeOutputBoundary presenter;

    public LikedRecipeInteractor(LikedRecipeDataAccessInterface dataAccess,
                                 LikedRecipeOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void addLikedRecipe(LikedRecipeInputData inputData) {
        try {
            String username = dataAccess.getCurrentUsername();

            // Get the full recipe by ID
            Recipe recipe = dataAccess.getRecipeById(inputData.getId());

            if (recipe == null) {
                presenter.prepareFailView("Recipe not found with ID: " + inputData.getId());
                return;
            }

            // Get user from users map
            User user = dataAccess.getUser(username);
            if (user == null) {
                presenter.prepareFailView("User not found: " + username);
                return;
            }

            // Check if recipe already exists
            boolean alreadySaved = user.getSavedRecipes().stream()
                    .anyMatch(r -> r.getRecipeId() == recipe.getRecipeId());

            if (!alreadySaved) {
                user.getSavedRecipes().add(recipe);
                // Persist changes to JSON file
                dataAccess.saveLikedRecipe(username, recipe);
            }

            // Reload the liked recipes list
            loadLikedRecipes();

        } catch (Exception e) {
            presenter.prepareFailView("Error adding recipe: " + e.getMessage());
        }
    }

    @Override
    public void deleteLikedRecipe(LikedRecipeInputData inputData) {
        try {
            String username = dataAccess.getCurrentUsername();

            // Get user from users map
            User user = dataAccess.getUser(username);
            if (user == null) {
                presenter.prepareFailView("User not found: " + username);
                return;
            }

            // Remove recipe from user's saved recipes
            user.getSavedRecipes().removeIf(recipe -> recipe.getRecipeId() == inputData.getId());

            // Persist changes to JSON file
            dataAccess.deleteLikedRecipe(username, inputData.getId());

            // Reload the liked recipes list
            loadLikedRecipes();

        } catch (Exception e) {
            presenter.prepareFailView("Error deleting recipe: " + e.getMessage());
        }
    }

    @Override
    public void loadLikedRecipes() {
        try {
            String username = dataAccess.getCurrentUsername();
            List<Recipe> recipes = dataAccess.getLikedRecipes(username);

            // Convert recipes to the output data format
            int[] recipeIds = new int[recipes.size()];
            String[] recipeNames = new String[recipes.size()];
            String[] recipeImages = new String[recipes.size()];
            List<Map<String, Double>> recipeNutrition = new ArrayList<>();
            List<List<String[]>> recipeIngredients = new ArrayList<>();
            List<List<String[]>> recipeSteps = new ArrayList<>();

            for (int i = 0; i < recipes.size(); i++) {
                Recipe recipe = recipes.get(i);

                // extracts recipe ids
                recipeIds[i] = recipe.getRecipeId();

                // Extract recipe name
                recipeNames[i] = recipe.getRecipeName();

                // Extract image
                recipeImages[i] = recipe.getRecipeImage();

                // Extract nutrition
                recipeNutrition.add(recipe.getNutritionalValues());

                // Extract ingredients
                List<String[]> ingredientsList = new ArrayList<>();
                for (Ingredient ingredient : recipe.getIngredients()) {
                    String[] ingredientData = new String[3];
                    ingredientData[0] = ingredient.getName();
                    ingredientData[1] = String.valueOf(ingredient.getQuantity());
                    ingredientData[2] = ingredient.getUnit();
                    ingredientsList.add(ingredientData);
                }
                recipeIngredients.add(ingredientsList);

                // Steps are fetched on-demand via handsfree method
                // Add empty list as placeholder for list view
                recipeSteps.add(new ArrayList<>());
            }

            LikedRecipeOutputData outputData = new LikedRecipeOutputData(
                    recipeIds,
                    recipeNames,
                    recipeImages,
                    recipeNutrition,
                    recipeIngredients,
                    recipeSteps
            );
            presenter.prepareLikedRecipeView(outputData);

        } catch (Exception e) {
            presenter.prepareFailView("Error loading recipes: " + e.getMessage());
        }
    }

    @Override
    public List<InstructionStep> handsfree(LikedRecipeInputData inputData) {
        try {
            int recipeId = inputData.getId();

            // Fetch instructions from data access layer (which calls the API)
            List<InstructionStep> instructions = dataAccess.getAnalyzedInstructions(recipeId);

            // Create RecipeInstructions entity from the list of steps
            RecipeInstructions recipeInstructions = new RecipeInstructions(instructions);

            // Prepare hands-free view starting at step 0
            StepByStepInputData stepByStepInputData = new StepByStepInputData(recipeInstructions, 0);
            presenter.prepareHandsfree(stepByStepInputData);

            return instructions;
        } catch (Exception e) {
            presenter.prepareFailView("Error loading instructions: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}