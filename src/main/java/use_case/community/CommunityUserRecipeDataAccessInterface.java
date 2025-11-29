package use_case.community;

import entities.Recipe;

import java.util.List;
import java.util.Optional;

/**
 * Abstraction for retrieving persisted liked recipes and contextual user information
 * needed by the Community use case. Implemented by concrete data access objects
 * (e.g., {@code FileDataAccessObject}) so higher-level components depend only on
 * a narrow boundary.
 */
public interface CommunityUserRecipeDataAccessInterface {

    /**
     * Returns defensive copies of the liked recipes for the provided username.
     *
     * @param username username whose liked recipes should be fetched
     * @return list of liked recipes (empty if none or user missing)
     */
    List<Recipe> getLikedRecipesForUser(String username);

    /**
     * Retrieves a liked recipe by id for the currently authenticated user.
     *
     * @param recipeId identifier of the recipe
     * @return optional defensive copy of the recipe, if present
     */
    Optional<Recipe> getCurrentUserLikedRecipe(int recipeId);

    /**
     * @return username of the currently authenticated/signed-in user, or null if none
     */
    String getCurrentUsername();
}
