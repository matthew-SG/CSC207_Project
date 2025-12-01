package interface_adapter.likedRecipeList;

import use_case.likedRecipeList.LikedRecipeInputBoundary;
import use_case.likedRecipeList.LikedRecipeInputData;

import java.util.HashMap;

public class LikedRecipeListController {

    private final LikedRecipeInputBoundary interactor;

    public LikedRecipeListController(LikedRecipeInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void loadLikedRecipes() {
        interactor.loadLikedRecipes();
    }

    public void startHandsfree(int recipeId, String recipeName) {
        LikedRecipeInputData inputData = new LikedRecipeInputData(
                recipeId,
                recipeName,
                null,
                new HashMap<>()
        );
        interactor.handsfree(inputData);
    }

    public void deleteLikedRecipe(int recipeId, String recipeName) {
        LikedRecipeInputData inputData = new LikedRecipeInputData(
                recipeId,
                recipeName,
                null,
                new HashMap<>()
        );
        interactor.deleteLikedRecipe(inputData);
    }
}
