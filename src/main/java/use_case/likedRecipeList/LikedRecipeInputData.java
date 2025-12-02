package use_case.likedRecipeList;

import java.util.Map;

/**
 * The Input Data for the Liked Recipe List Use Case.
 */

public class LikedRecipeInputData {
    private final int id;
    private final String recipeName;
    private final String image;
    private Map<String, Double> nutrition;

    public LikedRecipeInputData(int id, String recipeName, String image, Map<String, Double> nutrition) {
        this.id = id;
        this.recipeName = recipeName;
        this.image = image;
        this.nutrition = nutrition;
    }

    int getId() {

        return id;
    }

    String getRecipeName() {

        return recipeName;
    }

    String getImage() {
        return image;
    }

    Map<String, Double> getNutrition() {
        return nutrition;
    }
}
