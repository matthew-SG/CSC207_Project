package interface_adapter.approve_recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * State for the approve recipe view.
 */
public class ApproveRecipeState {
    private List<Integer> recipeIds = new ArrayList<>();
    private List<String> recipeNames = new ArrayList<>();
    private List<String> recipeImages = new ArrayList<>();
    private int currentIndex = 0;
    private boolean hasMore = false;
    private String errorMessage = null;

    public List<Integer> getRecipeIds() {
        return recipeIds;
    }

    public void setRecipeIds(List<Integer> recipeIds) {
        this.recipeIds = recipeIds;
    }

    public List<String> getRecipeNames() {
        return recipeNames;
    }

    public void setRecipeNames(List<String> recipeNames) {
        this.recipeNames = recipeNames;
    }

    public List<String> getRecipeImages() {
        return recipeImages;
    }

    public void setRecipeImages(List<String> recipeImages) {
        this.recipeImages = recipeImages;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public boolean hasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
