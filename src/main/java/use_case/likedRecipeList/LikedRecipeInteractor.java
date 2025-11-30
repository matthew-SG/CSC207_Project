package use_case.likedRecipeList;

import java.util.ArrayList;
import java.util.List;

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

            dataAccess.saveLikedRecipe(username, recipe);

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
            String[] recipeNames = new String[recipes.size()];
            List<List<String[]>> recipeIngredients = new ArrayList<>();
            List<List<String[]>> recipeSteps = new ArrayList<>();

            for (int i = 0; i < recipes.size(); i++) {
                Recipe recipe = recipes.get(i);

                // Extract recipe name
                recipeNames[i] = recipe.getRecipeName();

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
                    recipeNames,
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