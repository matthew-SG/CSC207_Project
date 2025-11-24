package use_case.community.output_data;

public class CommunitySelectedRecipeOutputData {
    private final int selectedRecipeId;
    private final String selectedRecipeName;
    private final String selectedRecipeImageUrl;
    
    public CommunitySelectedRecipeOutputData(int selectedRecipeId, String selectedRecipeName, String selectedRecipeImageUrl) {
        this.selectedRecipeId = selectedRecipeId;
        this.selectedRecipeName = selectedRecipeName;
        this.selectedRecipeImageUrl = selectedRecipeImageUrl;
    }

    public int getSelectedRecipeId() {
        return selectedRecipeId;
    }

    public String getSelectedRecipeName() {
        return selectedRecipeName;
    }

    public String getSelectedRecipeImageUrl() {
        return selectedRecipeImageUrl;
    }
}
