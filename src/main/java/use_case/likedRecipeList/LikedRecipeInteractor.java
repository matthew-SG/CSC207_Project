package use_case.likedRecipeList;

import entities.Recipe;
import java.util.ArrayList;
import java.util.List;

public class LikedRecipeInteractor implements LikedRecipeInputBoundary {
    private LikedRecipeOutputBoundary outputBoundary;
    private ArrayList<Recipe> recipes;

    public LikedRecipeInteractor(LikedRecipeOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void addLikedRecipe(Recipe recipe) {
        recipes.add(recipe);
    }

    @Override
    public void deleteLikedRecipe(Recipe recipe) {
        recipes.remove(recipe);
    }

    @Override
    public void handsfree(Recipe recipe) {
        int ID = recipe.getRecipeId();
        // Need to create the API Call
    }
}
