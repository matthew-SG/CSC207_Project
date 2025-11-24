package data_access;

import entities.Cuisine;
import entities.DietaryRestriction;
import entities.Intolerance;
import entities.Recipe;
import use_case.recipe_generator.RecipeDataAccessInterface;
import entities.Ingredient;


import java.util.ArrayList;
import java.util.List;

public class DummyRecipeDataAccessObject implements RecipeDataAccessInterface {
    public DummyRecipeDataAccessObject() {
        //TODO:
    }

    @Override
    public List<Recipe> getRecipes(DietaryRestriction dietaryRestriction,
                                   List<Intolerance> intolerances,
                                   Cuisine cuisine,
                                   Integer maxCalories,
                                   Integer minProtein) {

        List<Recipe> recipes = new ArrayList<>();

        // --- Recipe 1: Veggie Tacos ---
        Recipe veggieTacos = new Recipe(
                1,
                "Veggie Tacos",
                "https://example.com/tacos.jpg",
                "DINNER"
        );
        veggieTacos.getIngredients().add(new Ingredient("Tortilla", 2, "pieces"));
        veggieTacos.getIngredients().add(new Ingredient("Black beans", 100, "g"));
        veggieTacos.getIngredients().add(new Ingredient("Cheddar cheese", 30, "g"));

        veggieTacos.getNutritionalValues().put("calories", 450.0);
        veggieTacos.getNutritionalValues().put("protein", 18.0);

        recipes.add(veggieTacos);

        // --- Recipe 2: Chicken Stir Fry ---
        Recipe chickenStirFry = new Recipe(
                2,
                "Chicken Stir Fry",
                "https://example.com/stirfry.jpg",
                "DINNER"
        );
        chickenStirFry.getIngredients().add(new Ingredient("Chicken breast", 150, "g"));
        chickenStirFry.getIngredients().add(new Ingredient("Mixed vegetables", 120, "g"));
        chickenStirFry.getIngredients().add(new Ingredient("Soy sauce", 2, "tbsp"));

        chickenStirFry.getNutritionalValues().put("calories", 520.0);
        chickenStirFry.getNutritionalValues().put("protein", 35.0);

        recipes.add(chickenStirFry);

        // --- Recipe 3: Oatmeal Bowl ---
        Recipe oatmealBowl = new Recipe(
                3,
                "Oatmeal Breakfast Bowl",
                "https://example.com/oatmeal.jpg",
                "BREAKFAST"
        );
        oatmealBowl.getIngredients().add(new Ingredient("Oats", 60, "g"));
        oatmealBowl.getIngredients().add(new Ingredient("Milk", 200, "ml"));
        oatmealBowl.getIngredients().add(new Ingredient("Banana", 1, "piece"));

        oatmealBowl.getNutritionalValues().put("calories", 380.0);
        oatmealBowl.getNutritionalValues().put("protein", 15.0);

        recipes.add(oatmealBowl);

        // ---------- FILTERING BY CALORIES + PROTEIN ----------
        List<Recipe> filtered = new ArrayList<>();

        for (Recipe recipe : recipes) {
            boolean matches = true;

            // calories filter: keep <= maxCalories
            Double cals = recipe.getNutritionalValues().get("calories");
            if (maxCalories != null && cals != null && cals > maxCalories) {
                matches = false;
            }

            // protein filter: keep >= minProtein
            Double prot = recipe.getNutritionalValues().get("protein");
            if (minProtein != null && prot != null && prot < minProtein) {
                matches = false;
            }

            if (matches) {
                filtered.add(recipe);
            }
        }

        return filtered;
    }
}



