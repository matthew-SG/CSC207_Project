package use_case.recipe_generator;

import java.util.List;


 //the user will be provided either a list of recipeSummaries (List<RecipeSummary>) or a message (message) depending on if they entered valid filter options
 // Clean Architecture: This is an outbound Data Transfer Object from the interactor to the presenter. Keeps the interactor independent from swing and view model structure.

public class GenerateRecipeOutputData {
    private final List<RecipeSummary> recipes;
    private final String message; // message to handle the alternative flow, in case no recipe is found with current filter inputs, or api failures message is an empty string on successful searches

    public GenerateRecipeOutputData(List<RecipeSummary> recipes, String message) {
        this.recipes = recipes;
        this.message = message;
    }
     // the presenter reads this list and message and decides how to update the view model (ex error popup etc.)

    public List<RecipeSummary> getRecipes() {return recipes;}
    public String getMessage() {return message;}
}
