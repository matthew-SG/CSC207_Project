package use_case.likedRecipeList;

import entities.Recipe;

public interface LikedRecipeOutputBoundary {
    void prepareLikedRecipeView(Recipe recipe);
    void prepareHandsfree(Recipe recipe);
}
