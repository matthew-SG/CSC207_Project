package use_case.likedRecipeList;

/**
 * The Input Data for the Liked Recipe List Use Case
 */

public class LikedRecipeInputData {
    private final String ID;
    private final String Recipe_name;

    public LikedRecipeInputData(String id, String recipeName) {
        ID = id;
        Recipe_name = recipeName;
    }

    String getID() {return ID;}
    String getRecipeName() {return Recipe_name;}
}
