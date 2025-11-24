package use_case.community.output_data;

import entities.Rating;

import java.util.ArrayList;
import java.util.List;

public class CommunityRatingsOutputData {
    private final List<Integer> recipeIds;
    private final List<String> recipeNames;
    private final List<String> recipeImageUrls;
    private final List<Integer> stars;

    private final List<String> comments;
    public static String PROMPT = "Welcome to Community!~~ Please view and leave reviews!";
    protected String prompt;

    public CommunityRatingsOutputData(List<Rating> currentRatings) {
        List<Integer> recipeIds = new ArrayList<Integer>();
        List<String> recipeNames = new ArrayList<String>();
        List<Integer> stars = new ArrayList<Integer>();
        List<String> comments = new ArrayList<String>();
        List<String> imageUrls = new ArrayList<>();

        for (Rating rating : currentRatings){
            recipeIds.add(rating.getRatingId());
            recipeNames.add(rating.getRecipeName());
            stars.add(rating.getStars());
            comments.add(rating.getComment());
            imageUrls.add(rating.getRecipeImageUrl());
        }
        this.recipeIds = recipeIds;
        this.recipeNames = recipeNames;
        this.stars = stars;
        this.comments = comments;
        this.recipeImageUrls = imageUrls;
        prompt = PROMPT;

    }

    public String getPrompt() {
        return prompt;
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
}
