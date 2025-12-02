package use_case.liked_recipe_list;

import java.util.List;

import entities.InstructionStep;

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

    /**
     * Adds all ingredients (with amounts) for a liked recipe
     * to the current user's grocery list.
     */
    void addIngredientsToGrocery(LikedRecipeInputData inputData);
}