package interface_adapter.recipe_generator;

import entities.Intolerance;
import entities.Cuisine;
import entities.DietaryRestriction;
import use_case.recipe_generator.RecipeSummary;
import use_case.recipe_generator.RecipeSummary;
import java.util.List;

public class RecipeGeneratorState {
    //inputs
    private DietaryRestriction selectedDietaryRestriction;
    private List<Intolerance> selectedIntolerances;
    private Cuisine selectedCuisine;
    private String maxCalories;
    private String minProtein;
    //outputs
    private List<RecipeSummary> recipes;
    private String message;

    public DietaryRestriction getSelectedDietaryRestriction() {
        return selectedDietaryRestriction;
    }
    public List<Intolerance> getSelectedIntolerances() {
        return selectedIntolerances;
    }
    public Cuisine getSelectedCuisine() {
        return selectedCuisine;
    }
    public String getMaxCalories() {
        return maxCalories;
    }
    public String getMinProtein() {
        return minProtein;
    }
    public  List<RecipeSummary> getRecipes() {
        return recipes;
    }
    public String getMessage() {
        return message;
    }
    public void setSelectedDietaryRestriction(DietaryRestriction selectedDietaryRestriction) {
        this.selectedDietaryRestriction = selectedDietaryRestriction;
    }
    public void setSelectedIntolerances(List<Intolerance> selectedIntolerances) {
        this.selectedIntolerances = selectedIntolerances;
    }
    public void setSelectedCuisine(Cuisine selectedCuisine) {
        this.selectedCuisine = selectedCuisine;
    }
    public void setMaxCalories(String maxCalories) {
        this.maxCalories = maxCalories;
    }
    public void setMinProtein(String minProtein) {
        this.minProtein = minProtein;
    }
    public void setRecipes(List<RecipeSummary> recipes) {
        this.recipes = recipes;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
