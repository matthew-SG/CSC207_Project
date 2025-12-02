package use_case.load_meal_plan;

import data_access.InMemoryUserDataAccessObject;
import entities.Ingredient;
import entities.MealPlan;
import entities.Recipe;
import org.junit.jupiter.api.Test;
import use_case.meal_plan.MealPlanOutputData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LoadMealPlanInteractorTests {

    /**
     * Tests the interactor when it is called
     */
    @Test
    void testLoadMealPlanInteractor() {
        // Create DAO and user for the purposes of the test, api key doesn't matter
        InMemoryUserDataAccessObject userRepository = new InMemoryUserDataAccessObject("a");
        userRepository.signupUser("Matthew", "password");
        userRepository.login("Matthew", "password");

        // Create input data object for logging in
        LoadMealPlanInputData inputData = new LoadMealPlanInputData(1);

        // Create meal plans for the user to load
        // User needs meal plans to load, so we'll create two meal plans that differ by one recipe
        Recipe recipe1 = new Recipe(1234, "spaghetti", "idontexist.jpg", new ArrayList<>(),
                "italian", new HashMap<>());
        Recipe recipe2 = new Recipe(1324, "linguini", "idontexist.jpg", new ArrayList<>(),
                "italian", new HashMap<>());
        Recipe recipe3 = new Recipe(3124, "fettuccine", "idontexist.jpg", "italian");
        Recipe recipe4 = new Recipe(5678, "tortellini", "iDOexist.jpg", new ArrayList<>(),
                "italian", new HashMap<>());

        // Adding an ingredient and nutritional value
        Ingredient ingredient = new Ingredient("salt", 100, "kg");
        recipe1.getIngredients().add(ingredient);
        recipe2.getIngredients().add(ingredient);
        recipe4.getIngredients().add(ingredient);

        recipe1.getNutritionalValues().put("Protein", 1.0);
        recipe2.getNutritionalValues().put("Protein", 1.0);
        recipe4.getNutritionalValues().put("Protein", 1.0);


        // Creates the list of recipes for the meal plans
        List<Recipe> recipes1 = new ArrayList<>();
        recipes1.add(recipe1);
        recipes1.add(recipe2);
        recipes1.add(recipe3);

        List<Recipe> recipes2 = new ArrayList<>();
        recipes2.add(recipe4);
        recipes2.add(recipe1);
        recipes2.add(recipe2);

        // Create the meal plans
        MealPlan mealPlan1 = new MealPlan(recipes1, 100, 100, 100, 100);
        MealPlan mealPlan2 = new MealPlan(recipes2, 200, 100, 100, 100);

        // Save the meal plans to the user
        userRepository.saveMealPlan(mealPlan1);
        userRepository.saveMealPlan(mealPlan2);

        // Create expected outputs. In this case, ingredients and nutritional values wont affect the
        String[] expectedNames = {"tortellini", "spaghetti", "linguini"};
        String[] expectedImages = {"iDOexist.jpg", "idontexist.jpg", "idontexist.jpg"};

        List<List<String[]>> expectedIngredients = new ArrayList<>();
        List<String[]> recipeIngredients = new ArrayList<>();
        recipeIngredients.add(new String[]{"salt", "100.0", "kg"});
        expectedIngredients.add(recipeIngredients);
        expectedIngredients.add(recipeIngredients);
        expectedIngredients.add(recipeIngredients);

        List<Map<String, Double>> expectedNutritionalValues = new ArrayList<>();
        Map<String, Double> recipeNutritionalValues = new HashMap<>();
        recipeNutritionalValues.put("Protein", 1.0);
        expectedNutritionalValues.add(recipeNutritionalValues);
        expectedNutritionalValues.add(recipeNutritionalValues);
        expectedNutritionalValues.add(recipeNutritionalValues);

        // Presenter to test output of the interactor
        LoadMealPlanOutputBoundary presenter = new LoadMealPlanOutputBoundary() {
            @Override
            public void prepareSuccessView(MealPlanOutputData mealPlanOutputData) {
                assertArrayEquals(expectedNames, mealPlanOutputData.getRecipeNames());
                assertArrayEquals(expectedImages, mealPlanOutputData.getRecipeImages());
                assertEquals(expectedIngredients.size(), mealPlanOutputData.getIngredients().size());
                assertEquals(expectedNutritionalValues.size(), mealPlanOutputData.getNutritionalValues().size());
            }
        };

        LoadMealPlanInputBoundary interactor = new LoadMealPlanInteractor(userRepository, presenter);
        interactor.execute(inputData);
    }
}
