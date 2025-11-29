package use_case.recipe_generator;
import entities.Recipe;
import entities.DietaryRestriction;
import entities.Intolerance;
import entities.Cuisine;

import java.util.List;

public interface RecipeDataAccessInterface {
    List<Recipe> getRecipes(DietaryRestriction dietaryRestriction,
                            List<Intolerance> intolerances,
                            Cuisine cuisine,
                            Integer minCalories,
                            Integer maxCalories,
                            Integer minProtein,
                            Integer maxProtein);
}
