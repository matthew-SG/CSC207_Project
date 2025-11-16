package interface_adapter.community;

import java.util.List;

public class CommunityState {
    String subviewName;

    // presented in ratings and recipe selection view;
    // from: requested after user clicks pose
    List<Integer> recipeIds;
    List<String> recipeNames;
    List<Integer> stars;
    List<String> comments;
    List<String> recipeImages;
    String prompt;

    // used to publish recipe;
    // presented in review writing view;
    // from user recipe selection view
    int seletedRecipe;

    // used to publish recipe;
    // presented in view writing view
    // from view writing view
    String review;
    int star;

    public String getSubviewName() {
        return subviewName;
    }
}
