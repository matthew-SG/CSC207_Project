package use_case.likedRecipeList;

import entities.InstructionStep;
import entities.Recipe;

import java.util.List;

public interface LikedRecipeInputBoundary {

    void addLikedRecipe(Recipe recipe);

    void deleteLikedRecipe(Recipe recipe);

    public List<InstructionStep> handsfree(Recipe recipe);
}
