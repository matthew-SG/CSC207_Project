package use_case.likedRecipeList;

import entities.InstructionStep;
import entities.Recipe;

import java.util.List;

public interface LikedRecipeInputBoundary {

    /**
     * Adds a recipe to the user's liked recipes.
     * @param inputData the input data containing recipe information
     */
    void addLikedRecipe(LikedRecipeInputData inputData);

    /**
     * Deletes a recipe from the user's liked recipes.
     * @param inputData the input data containing recipe ID
     */
    void deleteLikedRecipe(LikedRecipeInputData inputData);

    /**
     * Loads all liked recipes for the current user.
     */
    void loadLikedRecipes();

    /**
     * Gets hands-free instructions for a recipe.
     * @param inputData the input data containing recipe ID
     * @return list of instruction steps
     */
    List<InstructionStep> handsfree(LikedRecipeInputData inputData);
}