package data_access;

import entities.Rating;
import entities.Recipe;
import entities.User;
import use_case.community.CommunityDataAccessInterface;
import use_case.community.input_data.CommunityPublishInputData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DummyCommunityDataAccessObject implements CommunityDataAccessInterface {
    HashMap<Integer, Recipe> likedRecipes = new HashMap<>();
    ArrayList<Rating> ratings = new ArrayList<>();

    public DummyCommunityDataAccessObject() {
        likedRecipes.put(1, new Recipe(1, "pizza",
                "https://www.tasteofhome.com/wp-content/uploads/2018/01/Homemade-Pizza_EXPS_FT23_376_EC_120123_3.jpg",
                new ArrayList<>(), "american", new HashMap<>()));
        likedRecipes.put(2, new Recipe(2, "hamburger",
                "https://en.wikipedia.org/wiki/Cheeseburger#/media/File:Cheeseburger.jpg", new ArrayList<>(),
                "american", new HashMap<>()));
        ratings.add(new Rating(1, 1, "test@1.ca", 4, "yummy", "hamburger", "https://en.wikipedia.org/wiki/Cheeseburger#/media/File:Cheeseburger.jpg"));
        ratings.add(new Rating(2, 2, "test@1.ca", 4, "good","pizza", "https://www.tasteofhome.com/wp-content/uploads/2018/01/Homemade-Pizza_EXPS_FT23_376_EC_120123_3.jpg"));
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
                "test222@1.ca",
                data.getRating(),
                data.getComment(),
                data.getRecipeName(),
                data.getRecipeImageURL()
        ));
        return ratings;
    }
}
