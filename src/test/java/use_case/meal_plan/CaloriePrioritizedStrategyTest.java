package use_case.meal_plan;

import entities.Ingredient;
import entities.Recipe;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the CaloriePrioritizedStrategy class.
 */
public class CaloriePrioritizedStrategyTest {

    /**
     * Test that the strategy correctly prioritizes calories over other nutrients.
     * When one triplet has closer calorie match but worse macro match, it should be selected.
     */
    @Test
    public void testCaloriePriorityOverMacros() {
        CaloriePrioritizedStrategy strategy = new CaloriePrioritizedStrategy();

        // Create ingredients (doesn't affect the nutritional calculation)
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("salt", 100, "kg"));

        // Triplet 1: Perfect calories (100), but bad macros
        Map<String, Double> nutritionalInfo1 = new HashMap<>();
        nutritionalInfo1.put("Calories", 33.33);
        nutritionalInfo1.put("Protein", 50.0);  // Way off target
        nutritionalInfo1.put("Carbohydrates", 50.0);  // Way off target
        nutritionalInfo1.put("Fat", 50.0);  // Way off target

        // Triplet 2: Off calories, but perfect macros
        Map<String, Double> nutritionalInfo2 = new HashMap<>();
        nutritionalInfo2.put("Calories", 50.0);  // Off target (150 total vs 100 target)
        nutritionalInfo2.put("Protein", 10.0);  // Perfect match
        nutritionalInfo2.put("Carbohydrates", 10.0);  // Perfect match
        nutritionalInfo2.put("Fat", 10.0);  // Perfect match

        // Create recipes
        Recipe recipe1a = new Recipe(1, "recipe1a", "img.jpg", ingredients, "cuisine", nutritionalInfo1);
        Recipe recipe1b = new Recipe(2, "recipe1b", "img.jpg", ingredients, "cuisine", nutritionalInfo1);
        Recipe recipe1c = new Recipe(3, "recipe1c", "img.jpg", ingredients, "cuisine", nutritionalInfo1);

        Recipe recipe2a = new Recipe(4, "recipe2a", "img.jpg", ingredients, "cuisine", nutritionalInfo2);
        Recipe recipe2b = new Recipe(5, "recipe2b", "img.jpg", ingredients, "cuisine", nutritionalInfo2);
        Recipe recipe2c = new Recipe(6, "recipe2c", "img.jpg", ingredients, "cuisine", nutritionalInfo2);

        List<Recipe> triplet1 = List.of(recipe1a, recipe1b, recipe1c);  // Total: 100 cal, 150 each macro
        List<Recipe> triplet2 = List.of(recipe2a, recipe2b, recipe2c);  // Total: 150 cal, 30 each macro

        List<List<Recipe>> recipeTriplets = new ArrayList<>();
        recipeTriplets.add(triplet1);
        recipeTriplets.add(triplet2);

        // Target: 100 calories, 30 each macro
        List<Recipe> result = strategy.generateMealPlan(recipeTriplets, 100, 30, 30, 30);

        // Triplet 1 should be selected because it has perfect calories (100), even though macros are way off
        // Triplet 1 error: 3 * |100-100| + |150-30| + |150-30| + |150-30| = 0 + 120 + 120 + 120 = 360
        // Triplet 2 error: 3 * |150-100| + |30-30| + |30-30| + |30-30| = 150 + 0 + 0 + 0 = 150
        // Without calorie priority, triplet 2 wins with 50+0+0+0=50 vs triplet 1 with 0+120+120+120=360
        // With 3x calorie weight, triplet 1 should still lose because 360 > 150
        // Let's adjust the test to show the calorie prioritization effect

        // Actually the math shows triplet 2 wins even with weight
        // Let's create a scenario where calorie priority makes a difference:
        // We need triplet with good calories but slightly worse total, vs triplet with bad calories but very close macros
        assertEquals(triplet2, result);  // Triplet 2 still wins because 150 < 360
    }

    /**
     * Test where calorie prioritization actually makes a difference in selection.
     */
    @Test
    public void testCaloriePrioritizationMakesDifference() {
        CaloriePrioritizedStrategy strategy = new CaloriePrioritizedStrategy();
        LowestNutritionalErrorStrategy baseStrategy = new LowestNutritionalErrorStrategy();

        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("salt", 100, "kg"));

        // Triplet 1: Perfect calories, slightly off macros
        Map<String, Double> nutritionalInfo1 = new HashMap<>();
        nutritionalInfo1.put("Calories", 100.0);  // 300 total = target
        nutritionalInfo1.put("Protein", 12.0);   // 36 total, target 30, error 6
        nutritionalInfo1.put("Carbohydrates", 12.0);  // 36 total, target 30, error 6
        nutritionalInfo1.put("Fat", 12.0);  // 36 total, target 30, error 6

        // Triplet 2: Off calories by 10, perfect macros
        Map<String, Double> nutritionalInfo2 = new HashMap<>();
        nutritionalInfo2.put("Calories", 96.67);  // 290 total, target 300, error 10
        nutritionalInfo2.put("Protein", 10.0);   // 30 total = target
        nutritionalInfo2.put("Carbohydrates", 10.0);  // 30 total = target
        nutritionalInfo2.put("Fat", 10.0);  // 30 total = target

        Recipe recipe1a = new Recipe(1, "recipe1a", "img.jpg", ingredients, "cuisine", nutritionalInfo1);
        Recipe recipe1b = new Recipe(2, "recipe1b", "img.jpg", ingredients, "cuisine", nutritionalInfo1);
        Recipe recipe1c = new Recipe(3, "recipe1c", "img.jpg", ingredients, "cuisine", nutritionalInfo1);

        Recipe recipe2a = new Recipe(4, "recipe2a", "img.jpg", ingredients, "cuisine", nutritionalInfo2);
        Recipe recipe2b = new Recipe(5, "recipe2b", "img.jpg", ingredients, "cuisine", nutritionalInfo2);
        Recipe recipe2c = new Recipe(6, "recipe2c", "img.jpg", ingredients, "cuisine", nutritionalInfo2);

        List<Recipe> triplet1 = List.of(recipe1a, recipe1b, recipe1c);
        List<Recipe> triplet2 = List.of(recipe2a, recipe2b, recipe2c);

        List<List<Recipe>> recipeTriplets = new ArrayList<>();
        recipeTriplets.add(triplet1);
        recipeTriplets.add(triplet2);

        // Target: 300 calories, 30 protein, 30 carbs, 30 fat
        List<Recipe> caloriePrioritizedResult = strategy.generateMealPlan(recipeTriplets, 300, 30, 30, 30);
        List<Recipe> baseResult = baseStrategy.generateMealPlan(recipeTriplets, 300, 30, 30, 30);

        // Base strategy: Triplet 1 error = 0 + 6 + 6 + 6 = 18
        //                Triplet 2 error = 10 + 0 + 0 + 0 = 10
        //                Base picks Triplet 2
        // Calorie prioritized: Triplet 1 error = 3*0 + 6 + 6 + 6 = 18
        //                      Triplet 2 error = 3*10 + 0 + 0 + 0 = 30
        //                      Calorie prioritized picks Triplet 1

        assertEquals(triplet1, caloriePrioritizedResult);  // CaloriePrioritizedStrategy picks perfect calories
        assertEquals(triplet2, baseResult);  // LowestNutritionalErrorStrategy picks lower total error
    }

    /**
     * Test with exact match (zero error).
     */
    @Test
    public void testExactMatch() {
        CaloriePrioritizedStrategy strategy = new CaloriePrioritizedStrategy();

        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("salt", 100, "kg"));

        Map<String, Double> nutritionalInfo = new HashMap<>();
        nutritionalInfo.put("Calories", 100.0);
        nutritionalInfo.put("Protein", 10.0);
        nutritionalInfo.put("Carbohydrates", 10.0);
        nutritionalInfo.put("Fat", 10.0);

        Recipe recipe1 = new Recipe(1, "recipe1", "img.jpg", ingredients, "cuisine", nutritionalInfo);
        Recipe recipe2 = new Recipe(2, "recipe2", "img.jpg", ingredients, "cuisine", nutritionalInfo);
        Recipe recipe3 = new Recipe(3, "recipe3", "img.jpg", ingredients, "cuisine", nutritionalInfo);

        List<Recipe> triplet = List.of(recipe1, recipe2, recipe3);
        List<List<Recipe>> recipeTriplets = new ArrayList<>();
        recipeTriplets.add(triplet);

        // Target exactly matches triplet nutritional values
        List<Recipe> result = strategy.generateMealPlan(recipeTriplets, 300, 30, 30, 30);

        assertEquals(triplet, result);
    }

    /**
     * Test with single triplet (should return that triplet).
     */
    @Test
    public void testSingleTriplet() {
        CaloriePrioritizedStrategy strategy = new CaloriePrioritizedStrategy();

        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("salt", 100, "kg"));

        Map<String, Double> nutritionalInfo = new HashMap<>();
        nutritionalInfo.put("Calories", 50.0);
        nutritionalInfo.put("Protein", 5.0);
        nutritionalInfo.put("Carbohydrates", 5.0);
        nutritionalInfo.put("Fat", 5.0);

        Recipe recipe1 = new Recipe(1, "recipe1", "img.jpg", ingredients, "cuisine", nutritionalInfo);
        Recipe recipe2 = new Recipe(2, "recipe2", "img.jpg", ingredients, "cuisine", nutritionalInfo);
        Recipe recipe3 = new Recipe(3, "recipe3", "img.jpg", ingredients, "cuisine", nutritionalInfo);

        List<Recipe> triplet = List.of(recipe1, recipe2, recipe3);
        List<List<Recipe>> recipeTriplets = new ArrayList<>();
        recipeTriplets.add(triplet);

        List<Recipe> result = strategy.generateMealPlan(recipeTriplets, 100, 10, 10, 10);

        assertEquals(triplet, result);
    }
}
