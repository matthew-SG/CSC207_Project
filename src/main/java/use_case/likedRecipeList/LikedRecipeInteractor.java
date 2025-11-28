package use_case.likedRecipeList;

import API.FindInstructionsSpoonacular;
import API.SearchByIngredientSpoonacular;
import entities.InstructionStep;
import entities.Recipe;
import java.util.ArrayList;
import java.util.List;

public class LikedRecipeInteractor implements LikedRecipeInputBoundary {
    private LikedRecipeOutputBoundary outputBoundary;
    private ArrayList<Recipe> recipes;
    private final FindInstructionsSpoonacular api;
    private final String API_KEY;

    public LikedRecipeInteractor(LikedRecipeOutputBoundary outputBoundary, FindInstructionsSpoonacular api, String apiKey) {
        this.outputBoundary = outputBoundary;
        this.api = api;
        API_KEY = apiKey;
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
    public List<InstructionStep> handsfree(Recipe recipe) {
        int ID = recipe.getRecipeId();
        return api.getAnalyzedInstructions(ID, API_KEY);
    }
}
