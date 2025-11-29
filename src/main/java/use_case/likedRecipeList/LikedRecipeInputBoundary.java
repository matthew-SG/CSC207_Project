package use_case.likedRecipeList;

import entities.InstructionStep;
import entities.Recipe;

import java.util.List;

public interface LikedRecipeInputBoundary {

    void addLikedRecipe(LikedRecipeInputData inputData);

    void deleteLikedRecipe(LikedRecipeInputData inputData);

    List<InstructionStep> handsfree(LikedRecipeInputData inputData);
}
