package use_case.recipe_generator;

import java.util.List;

/**
 * the user will be provided either a list of recipeSummaries or a message depending on if they entered valid filter options
 */

public class GenerateRecipeOutputData {
    private final List<RecipeSummary> recipes;
    private final String message; // message to handle the alternative flow, in case no recipe is found with current filter inputs

    public GenerateRecipeOutputData(List<RecipeSummary> recipes, String message) {
        this.recipes = recipes;
        this.message = message;
    }
    public List<RecipeSummary> getRecipes() {
        return recipes;
    }
    public String getMessage() {
        return message;
    }
}
