package use_case.meal_plan;

import data_access.InMemoryUserDataAccessObject;
import entities.Ingredient;
import entities.Recipe;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class MealPlanInteractorTests {

    /**
     * Tests the interactor when the current user has less than three meal plans saved
     */
    @Test
    public void failureLessThanThreeLikedRecipes() {
        // Creates input data and temp DAO, api key doesn't matter
        MealPlanInputData inputData = new MealPlanInputData("1", "1", "1", "1");
        InMemoryUserDataAccessObject userRepository = new InMemoryUserDataAccessObject("a");

        // For the failure test, we need to add a user to the repository with less than three recipes
        //      In this case, we will add two, one with no recipes, and one with a singular recipe
        userRepository.signupUser("Matthew", "password");
        userRepository.signupUser("Wehttam", "drowssap");

        Recipe newRecipe = new Recipe(1234, "spaghetii", "idontexist.jpg", "italian");
        userRepository.getUsers().get("Wehttam").getSavedRecipes().add(newRecipe);

        // Now we create a presenter to test if the interactor outputs what we expect
        MealPlanOutputBoundary presenter = new MealPlanOutputBoundary() {
            @Override
            public void prepareSuccessView(MealPlanOutputData mealPlanOutputData) {
                fail("Use case success is unexpected");
            }

            @Override
            public void prepareFailView(String listError, String inputError) {
                assertEquals("At least 3 saved recipes must be saved for meal plan generation!", listError);
                assertNull(inputError);
            }
        };

        MealPlanInputBoundary interactor = new MealPlanInteractor(userRepository, presenter);
        // Sets current user to Matthew
        userRepository.login("Matthew", "password");
        interactor.execute(inputData);
        // Sets current user to Wehttam
        userRepository.login("Wehttam", "drowssap");
        interactor.execute(inputData);
    }

    /**
     * Tests the interactor when one of the input strings cannot be converted into doubles
     */
    @Test
    public void failureNonDoubleInputs() {
        // One input data object for each parameter being a non-double string
        MealPlanInputData inputData1 = new MealPlanInputData("a", "1", "2", "3");
        MealPlanInputData inputData2 = new MealPlanInputData("1", "a", "2", "3");
        MealPlanInputData inputData3 = new MealPlanInputData("1", "2", "a", "3");
        MealPlanInputData inputData4 = new MealPlanInputData("1", "2", "3", "a");
        // Creates temp DAO for testing, api key doesn't matter
        InMemoryUserDataAccessObject userRepository = new InMemoryUserDataAccessObject("a");

        // Will need a new user with (at least) 3 recipes to get to the next failure use case
        userRepository.signupUser("Matthew", "password");
        userRepository.login("Matthew", "password");

        Recipe recipe1 = new Recipe(1234, "spaghetii", "idontexist.jpg", "italian");
        Recipe recipe2 = new Recipe(1324, "linguini", "idontexist.jpg", "italian");
        Recipe recipe3 = new Recipe(3124, "fettuccine", "idontexist.jpg", "italian");

        List<Recipe> likedRecipes = userRepository.getSavedRecipes();
        likedRecipes.add(recipe1);
        likedRecipes.add(recipe2);
        likedRecipes.add(recipe3);

        // Now we create a presenter to see if the interactor outputs what we expect
        MealPlanOutputBoundary presenter = new MealPlanOutputBoundary() {
            @Override
            public void prepareSuccessView(MealPlanOutputData mealPlanOutputData) {
                fail("Use case success is unexpected");
            }

            @Override
            public void prepareFailView(String listError, String inputError) {
                assertNull(listError);
                assertEquals("All input values must be numerical!", inputError);
            }
        };

        MealPlanInputBoundary interactor = new MealPlanInteractor(userRepository, presenter);
        interactor.execute(inputData1);
        interactor.execute(inputData2);
        interactor.execute(inputData3);
        interactor.execute(inputData4);
    }

    /**
     * Tests the interactor when on of the input fields are negative
     */
    @Test
    public void failureNegativeInputs() {
        // One input data object for each parameter being a negative value
        MealPlanInputData inputData1 = new MealPlanInputData("-1", "1", "2", "3");
        MealPlanInputData inputData2 = new MealPlanInputData("1", "-1", "2", "3");
        MealPlanInputData inputData3 = new MealPlanInputData("1", "2", "-1", "3");
        MealPlanInputData inputData4 = new MealPlanInputData("1", "2", "3", "-1");
        // Creates temp DAO for testing, api key doesn't matter
        InMemoryUserDataAccessObject userRepository = new InMemoryUserDataAccessObject("a");

        // Will need a new user with (at least) 3 recipes to get to the next failure use case
        userRepository.signupUser("Matthew", "password");
        userRepository.login("Matthew", "password");

        Recipe recipe1 = new Recipe(1234, "spaghetii", "idontexist.jpg", "italian");
        Recipe recipe2 = new Recipe(1324, "linguini", "idontexist.jpg", "italian");
        Recipe recipe3 = new Recipe(3124, "fettuccine", "idontexist.jpg", "italian");

        List<Recipe> likedRecipes = userRepository.getSavedRecipes();
        likedRecipes.add(recipe1);
        likedRecipes.add(recipe2);
        likedRecipes.add(recipe3);

        // Now we create a presenter to see if the interactor outputs what we expect
        MealPlanOutputBoundary presenter = new MealPlanOutputBoundary() {
            @Override
            public void prepareSuccessView(MealPlanOutputData mealPlanOutputData) {
                fail("Use case success is unexpected");
            }

            @Override
            public void prepareFailView(String listError, String inputError) {
                assertNull(listError);
                assertEquals("All input values must be non-negative!", inputError);
            }
        };

        MealPlanInputBoundary interactor = new MealPlanInteractor(userRepository, presenter);
        interactor.execute(inputData1);
        interactor.execute(inputData2);
        interactor.execute(inputData3);
        interactor.execute(inputData4);

    }

    /**
     * Test the interactor when the user has exactly three recipes
     */
    @Test
    public void successThreeRecipes() {
        // Values of non-negative input data do not matter as the returned meal plan should just be the three recipes
        //      in the user's liked recipes, in the same order
        MealPlanInputData mealPlanInputData = new MealPlanInputData("1", "1", "1", "1");
        // Creates temp DAO for testing, api key doesn't matter
        InMemoryUserDataAccessObject userRepository = new InMemoryUserDataAccessObject("a");

        // Creating a user to test the interactor on
        userRepository.signupUser("Matthew", "password");
        userRepository.login("Matthew", "password");

        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("salt", 100, "kg"));

        Map<String, Double> nutritionalInfo = new HashMap<>();
        nutritionalInfo.put("Calories", 1.0);
        nutritionalInfo.put("Carbohydrates", 1.0);
        nutritionalInfo.put("Fat", 1.0);
        nutritionalInfo.put("Protein", 1.0);

        // The nutritional info of the recipes doesnt matter as well because there are only 3/3 recipes to check.
        Recipe recipe1 = new Recipe(1234, "spaghetti", "idontexist.jpg", ingredients,
                "italian", nutritionalInfo);
        Recipe recipe2 = new Recipe(1324, "linguini", "idontexist.jpg", ingredients,
                "italian", nutritionalInfo);
        Recipe recipe3 = new Recipe(3124, "fettuccine", "idontexist.jpg", ingredients,
                "italian", nutritionalInfo);

        List<Recipe> likedRecipes = userRepository.getSavedRecipes();
        likedRecipes.add(recipe1);
        likedRecipes.add(recipe2);
        likedRecipes.add(recipe3);

        String[] expectedNames = {"spaghetti", "linguini", "fettuccine"};
        String[] expectedImages = {"idontexist.jpg", "idontexist.jpg", "idontexist.jpg"};

        List<List<String[]>> expectedIngredients = new ArrayList<>();
        List<String[]> nestedIngredients = new ArrayList<>();
        expectedIngredients.add(nestedIngredients);
        expectedIngredients.add(nestedIngredients);
        expectedIngredients.add(nestedIngredients);
        nestedIngredients.add(new String[]{"salt","100.0","kg"});

        // All the nutritional info is the same
        List<Map<String, Double>> expectedNutritionalInfo = new ArrayList<>();
        expectedNutritionalInfo.add(nutritionalInfo);
        expectedNutritionalInfo.add(nutritionalInfo);
        expectedNutritionalInfo.add(nutritionalInfo);

        // Presenter to see if use case works as expected (returns correct names and images,
        //      sizes of structures are correct)
        MealPlanOutputBoundary presenter = new MealPlanOutputBoundary() {
            @Override
            public void prepareSuccessView(MealPlanOutputData mealPlanOutputData) {
                assertEquals(mealPlanOutputData.getIngredients().size(), expectedIngredients.size());
                assertArrayEquals(mealPlanOutputData.getRecipeImages(), expectedImages);
                assertArrayEquals(mealPlanOutputData.getRecipeNames(), expectedNames);
                assertEquals(mealPlanOutputData.getNutritionalValues().size(), expectedNutritionalInfo.size());
            }

            @Override
            public void prepareFailView(String listError, String inputError) {
                fail("Use case failure is unexpected");
            }
        };

        MealPlanInputBoundary interactor = new MealPlanInteractor(userRepository, presenter);
        interactor.execute(mealPlanInputData);
    }

    /**
     * Test the interactor when the user has more than three recipes
     */
    @Test
    public void successMoreThanThreeRecipes() {
        // Values of non-negative input data do not matter as the returned meal plan should just be the three recipes
        //      in the user's liked recipes, in the same order
        MealPlanInputData mealPlanInputData = new MealPlanInputData("50", "50", "50", "50");
        // Creates temp DAO for testing, api key doesn't matter
        InMemoryUserDataAccessObject userRepository = new InMemoryUserDataAccessObject("a");

        // Creating a user to test the interactor on
        userRepository.signupUser("Matthew", "password");
        userRepository.login("Matthew", "password");

        // Ingredients have no impact on the success
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("salt", 100, "kg"));

        // Four different nutritional infos for four different recipes
        Map<String, Double> nutritionalInfo1 = new HashMap<>();
        nutritionalInfo1.put("Calories", 5.0);
        nutritionalInfo1.put("Carbohydrates", 5.0);
        nutritionalInfo1.put("Fat", 5.0);
        nutritionalInfo1.put("Protein", 5.0);

        Map<String, Double> nutritionalInfo2 = new HashMap<>();
        nutritionalInfo2.put("Calories", 15.0);
        nutritionalInfo2.put("Carbohydrates", 15.0);
        nutritionalInfo2.put("Fat", 15.0);
        nutritionalInfo2.put("Protein", 15.0);

        Map<String, Double> nutritionalInfo3 = new HashMap<>();
        nutritionalInfo3.put("Calories", 15.0);
        nutritionalInfo3.put("Carbohydrates", 15.0);
        nutritionalInfo3.put("Fat", 15.0);
        nutritionalInfo3.put("Protein", 15.0);

        Map<String, Double> nutritionalInfo4 = new HashMap<>();
        nutritionalInfo4.put("Calories", 20.0);
        nutritionalInfo4.put("Carbohydrates", 20.0);
        nutritionalInfo4.put("Fat", 20.0);
        nutritionalInfo4.put("Protein", 20.0);

        // 4 different recipes with 4 different nutritional compositions
        Recipe recipe1 = new Recipe(1234, "spaghetti", "idontexist.jpg", ingredients,
                "italian", nutritionalInfo1);
        Recipe recipe2 = new Recipe(1324, "linguini", "idontexist.jpg", ingredients,
                "italian", nutritionalInfo2);
        Recipe recipe3 = new Recipe(3124, "fettuccine", "idontexist.jpg", ingredients,
                "italian", nutritionalInfo3);
        Recipe recipe4 = new Recipe(5678, "tortellini", "iDOexist.jpg", ingredients,
                "italian", nutritionalInfo4);

        List<Recipe> likedRecipes = userRepository.getSavedRecipes();
        likedRecipes.add(recipe1);
        likedRecipes.add(recipe2);
        likedRecipes.add(recipe3);
        likedRecipes.add(recipe4);

        // The output recipes should be the last 3 as per the algorithm, in the order they appear in the user's
        //      liked recipes
        String[] expectedNames = {"linguini", "fettuccine", "tortellini"};
        String[] expectedImages = {"idontexist.jpg", "idontexist.jpg", "iDOexist.jpg"};

        List<List<String[]>> expectedIngredients = new ArrayList<>();
        List<String[]> nestedIngredients = new ArrayList<>();
        expectedIngredients.add(nestedIngredients);
        expectedIngredients.add(nestedIngredients);
        expectedIngredients.add(nestedIngredients);
        nestedIngredients.add(new String[]{"salt","100.0","kg"});

        List<Map<String, Double>> expectedNutritionalInfo = new ArrayList<>();
        expectedNutritionalInfo.add(nutritionalInfo2);
        expectedNutritionalInfo.add(nutritionalInfo3);
        expectedNutritionalInfo.add(nutritionalInfo4);

        // Presenter to see if use case works as expected (returns correct names and images,
        //      sizes of structures are correct)
        MealPlanOutputBoundary presenter = new MealPlanOutputBoundary() {
            @Override
            public void prepareSuccessView(MealPlanOutputData mealPlanOutputData) {
                assertEquals(mealPlanOutputData.getIngredients().size(), expectedIngredients.size());
                assertArrayEquals(mealPlanOutputData.getRecipeImages(), expectedImages);
                assertArrayEquals(mealPlanOutputData.getRecipeNames(), expectedNames);
                assertEquals(mealPlanOutputData.getNutritionalValues().size(), expectedNutritionalInfo.size());
            }

            @Override
            public void prepareFailView(String listError, String inputError) {
                fail("Use case failure is unexpected");
            }
        };

        MealPlanInputBoundary interactor = new MealPlanInteractor(userRepository, presenter);
        interactor.execute(mealPlanInputData);
    }

}
