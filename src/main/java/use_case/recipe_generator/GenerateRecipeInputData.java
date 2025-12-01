package use_case.recipe_generator;

import entities.Intolerance; // import Intolerance, Cuisine, dietary Restrictions enums to
import entities.DietaryRestriction;
import entities.Cuisine;
import java.util.List;
// the controller converts validated UI values into a GenerateRecipeDataInputData
// model of what use case 1 needs as an input
// set filters ie dietary restrictions, intolerances etc.
// Clean Architecture: The UI builds an instance of this and hands it over to the interactor, prevents use case from seeing raw Swing fields
public class GenerateRecipeInputData {
    private final DietaryRestriction dietaryRestriction; // one enum value
    private final List<Intolerance> intolerances; // list of enum values
    private final Cuisine cuisine;
    private final Integer minCalories;
    private final Integer maxCalories;
    private final Integer minProtein; //Integer to allow for null entries
    private final Integer maxProtein;

    public GenerateRecipeInputData( DietaryRestriction dietaryRestriction, List<Intolerance> intolerances, Cuisine cuisine, Integer minCalories, Integer maxCalories, Integer minProtein, Integer maxProtein) {
        this.dietaryRestriction = dietaryRestriction;
        this.intolerances = intolerances;
        this.cuisine = cuisine;
        this.minCalories = minCalories;
        this.maxCalories = maxCalories;
        this.minProtein = minProtein;
        this.maxProtein = maxProtein;
    }
    public DietaryRestriction getDietaryRestriction() {
        return dietaryRestriction;
    }
    public List<Intolerance> getIntolerances() {
        return intolerances;
    }
    public Cuisine getCuisine() {
        return cuisine;
    }
    public Integer getMaxCalories() {
        return maxCalories;
    }
    public Integer getMinProtein() {
        return minProtein;
    }
    public Integer getMaxProtein() {
        return maxProtein;
    }
    public Integer getMinCalories() {
        return minCalories;
    }
}

