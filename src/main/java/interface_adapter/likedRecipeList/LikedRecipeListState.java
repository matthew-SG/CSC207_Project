package interface_adapter.likedRecipeList;

import entities.Recipe;

import java.util.ArrayList;

import java.util.Collections;
import java.util.List;


public class LikedRecipeListState {

    private final List<Recipe> likedRecipes;

    public LikedRecipeListState(List<Recipe> likedRecipes) {
        this.likedRecipes = likedRecipes;
    }

    /** Returns an unmodifiable copy of the liked recipes list */
    public List<Recipe> getLikedRecipes() {
        return Collections.unmodifiableList(likedRecipes);
    }

    /** Adds a recipe to the liked list if it's not already there */
    public void likeRecipe(Recipe recipe) {
        if (!likedRecipes.contains(recipe)) {
            likedRecipes.add(recipe);
        }
    }

    /** Removes a recipe from the liked list */
    public void unlikeRecipe(Recipe recipe) {
        likedRecipes.remove(recipe);
    }

    /** Checks if a recipe is liked */
    public boolean isLiked(Recipe recipe) {
        return likedRecipes.contains(recipe);
    }

    /** Clears all liked recipes */
    public void clear() {
        likedRecipes.clear();
    }

}
