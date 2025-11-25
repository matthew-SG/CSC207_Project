package use_case.likedRecipeList;

import entities.Recipe;

public interface LikedRecipeInputBoundary {

    void executeAddLikedRecipe(AddLikedRecipeInputData addLikedRecipeInputData);

    void executeDeleteLikedRecipe(DeleteLikedRecipeInputData deleteLikedRecipeInputData);

    void executeHandsfree(HandsFreeRecipeInputData handsFreeRecipeInputData);
}
