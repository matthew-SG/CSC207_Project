package use_case.community.input_data;


public class CommunityRecipeSelectionInputData {
    private final int recipeID;
    public CommunityRecipeSelectionInputData(int recipeID){
        this.recipeID = recipeID;
    }

    public int getRecipeID() {
        return recipeID;
    }
}