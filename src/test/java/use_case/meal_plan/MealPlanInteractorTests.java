package use_case.meal_plan;

import data_access.InMemoryUserDataAccessObject;
import entities.Recipe;
import org.junit.Test;

import java.util.List;

import static org.bson.assertions.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MealPlanInteractorTests {

    /**
     * Tests the interactor when the current user has less than three meal plans saved
     */
    @Test
    public void failureLessThanThreeLikedRecipes() {
        MealPlanInputData inputData = new MealPlanInputData("1", "1", "1", "1");
        InMemoryUserDataAccessObject userRepository = new InMemoryUserDataAccessObject();

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
        InMemoryUserDataAccessObject userRepository = new InMemoryUserDataAccessObject();

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

    }
}
