package use_case.approve_recipe;

import java.util.List;

/**
 * Output data for the approve recipe use case.
 */
public class ApproveRecipeOutputData {
    private final List<Integer> recipeIds;
    private final List<String> recipeNames;
    private final List<String> recipeImages;
    private final int currentIndex;
    private final boolean hasMore;

    public ApproveRecipeOutputData(List<Integer> recipeIds, List<String> recipeNames,
                                   List<String> recipeImages, int currentIndex, boolean hasMore) {
        this.recipeIds = recipeIds;
        this.recipeNames = recipeNames;
        this.recipeImages = recipeImages;
        this.currentIndex = currentIndex;
        this.hasMore = hasMore;
    }

    public List<Integer> getRecipeIds() {
        return recipeIds;
    }

    public List<String> getRecipeNames() {
        return recipeNames;
    }

    public List<String> getRecipeImages() {
        return recipeImages;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public boolean hasMore() {
        return hasMore;
    }
}
