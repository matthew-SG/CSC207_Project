package interface_adapter.recipe_generator;

import use_case.recipe_generator.RecipeSummary;
import java.util.List;

// Clean Architecture: this state class holds the screen data for the Recipe Generator
// so the presenter and view can communicate without depending on each other directly.


// this class acts as a simple data container for the fields the view will need
public class RecipeGeneratorState {

    //outputs what the presenter actually sends back
    private List<RecipeSummary> recipes;
    private String message;

    public  List<RecipeSummary> getRecipes() {
        return recipes;
    }
    public String getMessage() {
        return message;
    }

    public void setRecipes(List<RecipeSummary> recipes) {
        this.recipes = recipes;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
