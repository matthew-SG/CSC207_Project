package interface_adapter.community;

import java.util.List;

public class CommunityState {
    // TODO: Sync view with state
    private String subviewName;

    // presented in ratings and recipe selection view;
    // from: requested after user clicks pose
    private List<Integer> recipeIds;
    private List<Integer> ratingIds;
    private List<String> recipeNames;
    private List<Integer> stars;
    private List<String> comments;
    private List<String> recipeImages;
    private List<String> usernames;
    private String prompt;

    // used to publish recipe;
    // presented in review writing view;
    // from user recipe selection view
    private int seletedRecipe;
    private String seletedRecipeName;
    private String seletedRecipeImageUrl;

    // used to publish recipe;
    // presented in view writing view
    // from view writing view
    private String review;
    private int star;

    public String getSubviewName() {
        return subviewName;
    }

    public List<Integer> getRecipeIds() {
        return recipeIds;
    }

    public List<Integer> getRatingIds() {
        return ratingIds;
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

    public List<String> getUsernames() {
        return usernames;
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

    public void setSubviewName(String subviewName) {
        this.subviewName = subviewName;
    }

    public void setRecipeIds(List<Integer> recipeIds) {
        this.recipeIds = recipeIds;
    }

    public void setRatingIds(List<Integer> ratingIds) {
        this.ratingIds = ratingIds;
    }

    public void setRecipeNames(List<String> recipeNames) {
        this.recipeNames = recipeNames;
    }

    public void setStars(List<Integer> stars) {
        this.stars = stars;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public void setRecipeImages(List<String> recipeImages) {
        this.recipeImages = recipeImages;
    }

    public void setUsernames(List<String> usernames) {
        this.usernames = usernames;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public void setSeletedRecipe(int seletedRecipe) {
        this.seletedRecipe = seletedRecipe;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getSeletedRecipeName() {
        return seletedRecipeName;
    }

    public void setSeletedRecipeName(String seletedRecipeName) {
        this.seletedRecipeName = seletedRecipeName;
    }

    public String getSeletedRecipeImageUrl() {
        return seletedRecipeImageUrl;
    }

    public void setSeletedRecipeImageUrl(String seletedRecipeImageUrl) {
        this.seletedRecipeImageUrl = seletedRecipeImageUrl;
    }
}
