package interface_adapter.liked_recipe_list;

import java.util.HashMap;

import use_case.liked_recipe_list.LikedRecipeInputBoundary;
import use_case.liked_recipe_list.LikedRecipeInputData;

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

    public void addIngredientsToGrocery(int recipeId, String recipeName) {
        LikedRecipeInputData data =
                new LikedRecipeInputData(recipeId, recipeName, null, null);
        interactor.addIngredientsToGrocery(data);
    }
}
