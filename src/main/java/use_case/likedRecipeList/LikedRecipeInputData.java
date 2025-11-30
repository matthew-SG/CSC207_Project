package use_case.likedRecipeList;

/**
 * The Input Data for the Liked Recipe List Use Case
 */

public class LikedRecipeInputData {
    private final int id;
    private final String recipeName;

    public LikedRecipeInputData(int id, String recipeName) {
        this.id = id;
        this.recipeName = recipeName;
    }

    int getId() {
        return id;
    }

    String getRecipeName() {
        return recipeName;
    }
}
