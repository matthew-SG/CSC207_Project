package use_case.liked_recipe_list;

public interface LikedRecipeInputBoundary {

    void executeAddLikedRecipe(AddLikedRecipeInputData addLikedRecipeInputData);

    void executeDeleteLikedRecipe(DeleteLikedRecipeInputData deleteLikedRecipeInputData);

    void executeHandsfree(HandsFreeRecipeInputData handsFreeRecipeInputData);
}
