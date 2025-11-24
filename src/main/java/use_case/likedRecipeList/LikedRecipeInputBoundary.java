package use_case.likedRecipeList;

import entities.Recipe;

public interface LikedRecipeInputBoundary {

    void addLikedRecipe(Recipe recipe);

    void deleteLikedRecipe(Recipe recipe);

    void handsfree(Recipe recipe);
}
