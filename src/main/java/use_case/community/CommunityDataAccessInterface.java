package use_case.community;

import entities.Rating;
import entities.User;
import entities.Recipe;
import use_case.community.input_data.CommunityPublishInputData;

import java.util.List;

public interface CommunityDataAccessInterface {
    public List<Recipe> getLikedRecipes(User user);
    public Recipe getSelectedRecipe(int recipeID);

    public List<Rating> getCurrentRatings();

    public List<Rating> publishReview(CommunityPublishInputData data);
}
