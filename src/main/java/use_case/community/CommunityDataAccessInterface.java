package use_case.community;

import entities.Rating;
import entities.Recipe;
import entities.User;
import use_case.community.input_data.CommunityPublishInputData;

import java.util.List;

/*
 * DataAccessInterface for community
 */
public interface CommunityDataAccessInterface {
    List<Recipe> getLikedRecipes(String username);
    Recipe getSelectedRecipe(int recipeID);

    List<Rating> getCurrentRatings();

    List<Rating> publishReview(CommunityPublishInputData data);
}
