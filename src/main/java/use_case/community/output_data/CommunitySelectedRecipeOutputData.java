package use_case.community.output_data;

import entities.Recipe;

public class CommunitySelectedRecipeOutputData {
    private final int selectedRecipeId;


    public CommunitySelectedRecipeOutputData(int selectedRecipeId) {
        this.selectedRecipeId = selectedRecipeId;
    }

    public int getSelectedRecipeId() {
        return selectedRecipeId;
    }
}
