package use_case.community.output_data;

import entities.Rating;

import java.util.ArrayList;
import java.util.List;

public class CommunityRatingsOutputData {
    private final List<Integer> ratingIds;
    private final List<Integer> recipeIds;
    private final List<String> recipeNames;
    private final List<String> recipeImageUrls;
    private final List<Integer> stars;

    private final List<String> comments;
    private final List<String> usernames;
    public static String PROMPT = "Welcome to Community!~~ Please view and leave reviews!";
    protected String prompt;

    public CommunityRatingsOutputData(List<Rating> currentRatings) {
        List<Integer> ratingIds = new ArrayList<>();
        List<Integer> recipeIds = new ArrayList<Integer>();
        List<String> recipeNames = new ArrayList<String>();
        List<Integer> stars = new ArrayList<Integer>();
        List<String> comments = new ArrayList<String>();
        List<String> imageUrls = new ArrayList<>();
        List<String> usernames = new ArrayList<>();

        for (Rating rating : currentRatings){
            ratingIds.add(rating.getRatingId());
            recipeIds.add(rating.getRecipeId());
            recipeNames.add(rating.getRecipeName());
            stars.add(rating.getStars());
            comments.add(rating.getComment());
            imageUrls.add(rating.getRecipeImageUrl());
            usernames.add(rating.getUsername());
        }
        this.ratingIds = ratingIds;
        this.recipeIds = recipeIds;
        this.recipeNames = recipeNames;
        this.stars = stars;
        this.comments = comments;
        this.recipeImageUrls = imageUrls;
        this.usernames = usernames;
        prompt = PROMPT;

    }

    public String getPrompt() {
        return prompt;
    }

    public List<Integer> getRatingIds() {
        return ratingIds;
    }

    public List<Integer> getRecipeIds() {
        return recipeIds;
    }

    public List<Integer> getStars() {
        return stars;
    }

    public List<String> getComments() {
        return comments;
    }

    public List<String> getRecipeNames() {
        return recipeNames;
    }

    public List<String> getRecipeImageUrls() {
        return recipeImageUrls;
    }

    public List<String> getUsernames() {
        return usernames;
    }
}
