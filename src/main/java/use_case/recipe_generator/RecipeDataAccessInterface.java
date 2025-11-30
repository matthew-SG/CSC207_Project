package use_case.recipe_generator;
import entities.Recipe;
import entities.DietaryRestriction;
import entities.Intolerance;
import entities.Cuisine;

import java.util.List;
 // how the use case communicates to data
 //Clean Architecture: The use case only depends on this interface, seperated from specific databases or API.
 // this separation is what allows unit tests to be written with fake DAOs
public interface RecipeDataAccessInterface {
    List<Recipe> getRecipes(DietaryRestriction dietaryRestriction,
                            List<Intolerance> intolerances,
                            Cuisine cuisine,
                            Integer minCalories,
                            Integer maxCalories,
                            Integer minProtein,
                            Integer maxProtein);
}
