package use_case.likedRecipeList;

import entities.Recipe;

public interface LikedRecipeInputBoundary {

    void executeAddLikedRecipe(addLikedRecipeInputData addLikedRecipeInputData);

    void executeDeleteLikedRecipe();

    void executeHandsfree(HandsFreeInputData handsFreeInputData);
}
