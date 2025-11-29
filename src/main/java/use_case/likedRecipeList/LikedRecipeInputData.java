package use_case.likedRecipeList;

/**
 * The Input Data for the Liked Recipe List Use Case
 */

public class LikedRecipeInputData {
    private final int ID;
    private final String Recipe_name;

    public LikedRecipeInputData(int id, String recipeName) {
        ID = id;
        Recipe_name = recipeName;
    }

    int getID() {return ID;}
    String getRecipeName() {return Recipe_name;}
}
