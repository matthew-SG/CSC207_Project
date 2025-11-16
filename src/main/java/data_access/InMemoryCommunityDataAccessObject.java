package data_access;

import entities.Rating;
import entities.Recipe;
import entities.User;
import use_case.community.CommunityDataAccessInterface;
import use_case.community.input_data.CommunityPublishInputData;

import java.util.List;

public class InMemoryCommunityDataAccessObject implements CommunityDataAccessInterface {

    @Override
    public List<Recipe> getLikedRecipes(User user) {
        return List.of();
    }

    @Override
    public Recipe getSelectedRecipe(int recipeID) {
        return null;
    }

    @Override
    public List<Rating> getCurrentRatings() {
        return List.of();
    }

    @Override
    public List<Rating> publishReview(CommunityPublishInputData data) {
        return List.of();
    }
}
