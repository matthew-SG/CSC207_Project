package use_case.likedRecipeList;

import API.FindInstructionsSpoonacular;
import entities.InstructionStep;
import entities.BasicRecipe;
import java.util.ArrayList;
import java.util.List;

public class LikedRecipeInteractor implements LikedRecipeInputBoundary {
    private LikedRecipeOutputBoundary presenter;
    private ArrayList<BasicRecipe> recipes = new ArrayList<>();
    private final FindInstructionsSpoonacular api;
    private final String API_KEY;

    public LikedRecipeInteractor(LikedRecipeOutputBoundary presenter, FindInstructionsSpoonacular api, String apiKey) {
        this.presenter = presenter;
        this.api = api;
        API_KEY = apiKey;
    }

    @Override
    public void addLikedRecipe(LikedRecipeInputData inputData) {
        BasicRecipe item = new BasicRecipe(inputData.getID(), inputData.getRecipeName());
        recipes.add(item);
    }

    @Override
    public void deleteLikedRecipe(LikedRecipeInputData inputData) {
        BasicRecipe item = new BasicRecipe(inputData.getID(), inputData.getRecipeName());
        recipes.remove(item);
    }

    @Override
    public List<InstructionStep> handsfree(LikedRecipeInputData inputData) {
        int ID = inputData.getID();
        return api.getAnalyzedInstructions(ID, API_KEY);
    }
}
