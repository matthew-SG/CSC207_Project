package use_case.community.input_data;


public class CommunityRecipeSelectionInputData {
    private final int recipeID;
    private final String recipeName;
    private final String recipeImageUrl;
    public CommunityRecipeSelectionInputData(int recipeID, String recipeName, String recipeImageUrl){
        this.recipeID = recipeID;
        this.recipeName = recipeName;
        this.recipeImageUrl = recipeImageUrl;
    }

    public int getRecipeID() {
        return recipeID;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public String getRecipeImageUrl() {
        return recipeImageUrl;
    }
}