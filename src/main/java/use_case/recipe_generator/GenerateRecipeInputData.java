package use_case.recipe_generator;

import entities.Intolerance; // import Intolerance, Cuisine, dietary Restrictions enums to
import entities.DietaryRestriction;
import entities.Cuisine;
import java.util.List;
// model of what use case 1 needs as an input
// set filters ie dietary restrictions, intolerances etc.
public class GenerateRecipeInputData {
    private final DietaryRestriction dietaryRestriction;
    private final List<Intolerance> intolerances;
    private final Cuisine cuisine;
    private final Integer maxCalories;
    private final Integer minProtein; //Integer to allow for null entries

    public GenerateRecipeInputData( DietaryRestriction dietaryRestriction, List<Intolerance> intolerances, Cuisine cuisine, Integer maxCalories, Integer minProtein) {
        this.dietaryRestriction = dietaryRestriction;
        this.intolerances = intolerances;
        this.cuisine = cuisine;
        this.maxCalories = maxCalories;
        this.minProtein = minProtein;
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
}

