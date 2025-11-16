package data_access;

import entities.Rating;
import entities.Recipe;
import entities.User;
import use_case.community.CommunityDataAccessInterface;
import use_case.community.input_data.CommunityPublishInputData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class DummyCommunityDataAccessObject implements CommunityDataAccessInterface {
    HashMap<Integer, Recipe> likedRecipes = new HashMap<>();
    ArrayList<Rating> ratings = new ArrayList<>();

    public DummyCommunityDataAccessObject() {
        likedRecipes.put(1, new Recipe(1, "pizza", "", "american"));
        likedRecipes.put(2, new Recipe(2, "hamburger", "", "american"));
        ratings.add(new Rating(1, 1, 1, 4, "yummy"));
        ratings.add(new Rating(2, 2, 2, 4, "good"));
    }
    @Override
    public List<Recipe> getLikedRecipes(User user) {
        return new ArrayList<>(likedRecipes.values());
    }

    @Override
    public Recipe getSelectedRecipe(int recipeID) {
        return likedRecipes.get(recipeID);
    }

    @Override
    public List<Rating> getCurrentRatings() {
        return ratings;
    }

    @Override
    public List<Rating> publishReview(CommunityPublishInputData data) {
        ratings.add(new Rating(
                ratings.size() + 1,
                data.getRecipeID(),
                3,
                data.getRating(),
                data.getComment()
        ));
        return ratings;
    }
}
