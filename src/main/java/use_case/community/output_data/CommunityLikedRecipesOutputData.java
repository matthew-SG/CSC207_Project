package use_case.community.output_data;


import entities.Recipe;

import java.util.ArrayList;
import java.util.List;

public class CommunityLikedRecipesOutputData {
    private final List<Integer> recipeIds;
    private final List<String> recipeNames;
    private final List<String> recipeImages;

    public CommunityLikedRecipesOutputData(List<Recipe> likedRecipes) {
        List<Integer> recipeIds = new ArrayList<>();
        List<String> recipeNames = new ArrayList<>();
        List<String> recipeImages = new ArrayList<>();

        for (Recipe recipe : likedRecipes) {
            recipeIds.add(recipe.getRecipeId());
            recipeNames.add(recipe.getRecipeName());
            recipeImages.add(recipe.getRecipeImage());
        }

        this.recipeIds = recipeIds;
        this.recipeNames = recipeNames;
        this.recipeImages = recipeImages;
    }

    public List<String> getRecipeNames() {
        return recipeNames;
    }

    public List<Integer> getRecipeIds() {
        return recipeIds;
    }

    public List<String> getRecipeImages() {
        return recipeImages;
    }
}
