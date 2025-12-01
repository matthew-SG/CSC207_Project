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
    private List<Recipe> currentRecipes;
    private int currentIndex;

    public ApproveRecipeInteractor(ApproveRecipeDataAccessInterface dataAccess,
                                   ApproveRecipeOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
        this.currentRecipes = new ArrayList<>();
        this.currentIndex = 0;
    }

    @Override
    public void loadRecipes() {
        try {
            currentRecipes = dataAccess.getAvailableRecipes();
            currentIndex = 0;

            if (currentRecipes.isEmpty()) {
                presenter.prepareFailView("No recipes available.");
                return;
            }

            showCurrentRecipe();
        } catch (Exception e) {
            presenter.prepareFailView("Error loading recipes: " + e.getMessage());
        }
    }

    private void showCurrentRecipe() {
        if (currentIndex >= currentRecipes.size()) {
            // No more recipes
            presenter.prepareApproveSuccessView(new ApproveRecipeOutputData(
                    new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 0, false
            ));
            return;
        }

        List<Integer> recipeIds = new ArrayList<>();
        List<String> recipeNames = new ArrayList<>();
        List<String> recipeImages = new ArrayList<>();

        for (int i = currentIndex; i < currentRecipes.size(); i++) {
            Recipe recipe = currentRecipes.get(i);
            recipeIds.add(recipe.getRecipeId());
            recipeNames.add(recipe.getRecipeName());
            recipeImages.add(recipe.getRecipeImage());
        }

        ApproveRecipeOutputData outputData = new ApproveRecipeOutputData(
                recipeIds, recipeNames, recipeImages, 0, currentRecipes.size() - currentIndex > 1
        );

        presenter.prepareRecipeView(outputData);
    }

    @Override
    public void approveRecipe(ApproveRecipeInputData inputData) {
        try {
            if (currentIndex >= currentRecipes.size()) {
                presenter.prepareFailView("No recipe to approve.");
                return;
            }

            Recipe recipe = currentRecipes.get(currentIndex);

            // Add recipe to user's saved recipes
            dataAccess.saveRecipeToUser(inputData.getUsername(), recipe);

            // Move to next recipe
            currentIndex++;
            showCurrentRecipe();
        } catch (Exception e) {
            presenter.prepareFailView("Error approving recipe: " + e.getMessage());
        }
    }

    @Override
    public void declineRecipe(DeclineRecipeInputData inputData) {
        try {
            if (currentIndex >= currentRecipes.size()) {
                presenter.prepareFailView("No recipe to decline.");
                return;
            }

            Recipe recipe = currentRecipes.get(currentIndex);

            // Remove from pending approval list
            dataAccess.removeFromPendingApproval(recipe.getRecipeId());

            // Move to next recipe
            currentIndex++;
            showCurrentRecipe();
        } catch (Exception e) {
            presenter.prepareFailView("Error declining recipe: " + e.getMessage());
        }
    }
}
