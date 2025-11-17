package interface_adapter.community;

import java.util.List;

public class CommunityState {
    public String subviewName;

    // presented in ratings and recipe selection view;
    // from: requested after user clicks pose
    List<Integer> recipeIds;
    List<String> recipeNames;
    List<Integer> stars;
    List<String> comments;
    List<String> recipeImages;
    public String prompt;

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

    public List<Integer> getRecipeIds() {
        return recipeIds;
    }

    public List<String> getRecipeNames() {
        return recipeNames;
    }

    public List<Integer> getStars() {
        return stars;
    }

    public List<String> getComments() {
        return comments;
    }

    public List<String> getRecipeImages() {
        return recipeImages;
    }

    public String getPrompt() {
        return prompt;
    }

    public int getSeletedRecipe() {
        return seletedRecipe;
    }

    public String getReview() {
        return review;
    }

    public int getStar() {
        return star;
    }
}
