package use_case.likedRecipeList;

import entities.Recipe;

import java.util.List;

public interface LikedRecipeDataAccessInterface {

    /**
     * Returns the saved recipes of the current user
     * @return the saved recipes of the user
     */
    public List<Recipe> getSavedRecipes();
}
