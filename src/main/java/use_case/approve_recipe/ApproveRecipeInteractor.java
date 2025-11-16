package use_case.approve_recipe;

import entities.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Interactor for the approve recipe use case.
 * Handles approving and declining recipes, and adding approved recipes to user's saved list.
 */
public class ApproveRecipeInteractor implements ApproveRecipeInputBoundary {
    private final ApproveRecipeDataAccessInterface dataAccess;
    private final ApproveRecipeOutputBoundary presenter;

    public ApproveRecipeInteractor(ApproveRecipeDataAccessInterface dataAccess,
                                   ApproveRecipeOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void loadRecipes() {
        try {
            List<Recipe> recipes = dataAccess.getAvailableRecipes();

            if (recipes.isEmpty()) {
                presenter.prepareFailView("No recipes available.");
                return;
            }

            List<Integer> recipeIds = new ArrayList<>();
            List<String> recipeNames = new ArrayList<>();
            List<String> recipeImages = new ArrayList<>();

            for (Recipe recipe : recipes) {
                recipeIds.add(recipe.getRecipeId());
                recipeNames.add(recipe.getRecipeName());
                recipeImages.add(recipe.getRecipeImage());
            }

            ApproveRecipeOutputData outputData = new ApproveRecipeOutputData(
                    recipeIds, recipeNames, recipeImages, 0, recipes.size() > 1
            );

            presenter.prepareRecipeView(outputData);
        } catch (Exception e) {
            presenter.prepareFailView("Error loading recipes: " + e.getMessage());
        }
    }

    @Override
    public void approveRecipe(ApproveRecipeInputData inputData) {
        try {
            Recipe recipe = dataAccess.getRecipeById(inputData.getRecipeId());

            if (recipe == null) {
                presenter.prepareFailView("Recipe not found.");
                return;
            }

            // Add recipe to user's saved recipes
            dataAccess.saveRecipeToUser(inputData.getUsername(), recipe);

            // Load remaining recipes
            List<Recipe> remainingRecipes = dataAccess.getAvailableRecipes();
            List<Integer> recipeIds = new ArrayList<>();
            List<String> recipeNames = new ArrayList<>();
            List<String> recipeImages = new ArrayList<>();

            for (Recipe r : remainingRecipes) {
                if (r.getRecipeId() != inputData.getRecipeId()) {
                    recipeIds.add(r.getRecipeId());
                    recipeNames.add(r.getRecipeName());
                    recipeImages.add(r.getRecipeImage());
                }
            }

            ApproveRecipeOutputData outputData = new ApproveRecipeOutputData(
                    recipeIds, recipeNames, recipeImages, 0, !recipeIds.isEmpty()
            );

            presenter.prepareApproveSuccessView(outputData);
        } catch (Exception e) {
            presenter.prepareFailView("Error approving recipe: " + e.getMessage());
        }
    }

    @Override
    public void declineRecipe(DeclineRecipeInputData inputData) {
        try {
            // Load remaining recipes (excluding the declined one)
            List<Recipe> remainingRecipes = dataAccess.getAvailableRecipes();
            List<Integer> recipeIds = new ArrayList<>();
            List<String> recipeNames = new ArrayList<>();
            List<String> recipeImages = new ArrayList<>();

            for (Recipe r : remainingRecipes) {
                if (r.getRecipeId() != inputData.getRecipeId()) {
                    recipeIds.add(r.getRecipeId());
                    recipeNames.add(r.getRecipeName());
                    recipeImages.add(r.getRecipeImage());
                }
            }

            ApproveRecipeOutputData outputData = new ApproveRecipeOutputData(
                    recipeIds, recipeNames, recipeImages, 0, !recipeIds.isEmpty()
            );

            presenter.prepareDeclineView(outputData);
        } catch (Exception e) {
            presenter.prepareFailView("Error declining recipe: " + e.getMessage());
        }
    }
}
