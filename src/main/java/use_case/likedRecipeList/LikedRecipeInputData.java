package use_case.likedRecipeList;

/**
 * The Input Data for the Liked Recipe List Use Case
 */

public class LikedRecipeInputData {
    private final int id;
    private final String recipe_name;

    public LikedRecipeInputData(int id, String recipeName) {
        this.id = id;
        recipe_name = recipeName;
    }

    int getId() {
        return id;
    }

    String getRecipeName() {
        return recipe_name;
    }
}
