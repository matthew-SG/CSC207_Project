package use_case.view_meal_plans;

import data_access.InMemoryUserDataAccessObject;
import entities.MealPlan;
import entities.Recipe;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ViewMealPlansInteractorTests {

    /**
     * Tests the interactor when the user has no meal plans
     */
    @Test
    void failureTest() {
        InMemoryUserDataAccessObject userRepository = new InMemoryUserDataAccessObject();

        // Need a user to view meal plans for
        userRepository.signupUser("Matthew", "password");
        userRepository.login("Matthew", "password");

        // Now we will create a presenter to see if the interactor prepares the fail view
        ViewMealPlansOutputBoundary presenter = new ViewMealPlansOutputBoundary() {
            @Override
            public void prepareSuccessView(ViewMealPlansOutputData viewMealPlansOutputData) {
                fail("Use case success is unexpected");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("You currently have no meal plans saved!", error);
            }
        };

        ViewMealPlansInputBoundary interactor = new ViewMealPlansInteractor(userRepository, presenter);
        interactor.execute();
    }

    /**
     * Tests the interactor when the user does have meal plans
     */
    @Test
    void successTest() {
        // Create the DAO and the user to test with
        InMemoryUserDataAccessObject userRepository = new InMemoryUserDataAccessObject();
        userRepository.signupUser("Matthew", "password");
        userRepository.login("Matthew", "password");

        // User needs meal plans to display, so we'll create two meal plans that are the exact same for the user
        Recipe recipe1 = new Recipe(1234, "spaghetti", "idontexist.jpg", "italian");
        Recipe recipe2 = new Recipe(1324, "linguini", "idontexist.jpg", "italian");
        Recipe recipe3 = new Recipe(3124, "fettuccine", "idontexist.jpg", "italian");
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(recipe1);
        recipes.add(recipe2);
        recipes.add(recipe3);

        MealPlan mealPlan1 = new MealPlan(recipes, 100, 100, 100, 100);
        MealPlan mealPlan2 = new MealPlan(recipes, 100, 100, 100, 100);

        // Save the meal plans to the user
        userRepository.saveMealPlan(mealPlan1);
        userRepository.saveMealPlan(mealPlan2);

        // Create expected outputs
        List<String> expectedNames = new ArrayList<>();
        expectedNames.add("spaghetti");
        expectedNames.add("spaghetti");

        List<Double> expectedCalories = new ArrayList<>();
        expectedCalories.add(100.0);
        expectedCalories.add(100.0);
        List<Double> expectedProtein = new ArrayList<>();
        expectedProtein.add(100.0);
        expectedProtein.add(100.0);
        List<Double> expectedCarbs = new ArrayList<>();
        expectedCarbs.add(100.0);
        expectedCarbs.add(100.0);
        List<Double> expectedFats = new ArrayList<>();
        expectedFats.add(100.0);
        expectedFats.add(100.0);

        // Create the presenter to test the interactor
        ViewMealPlansOutputBoundary presenter = new ViewMealPlansOutputBoundary() {
            @Override
            public void prepareSuccessView(ViewMealPlansOutputData viewMealPlansOutputData) {
                assertIterableEquals(viewMealPlansOutputData.getFirstRecipeNames(),  expectedNames);
                assertIterableEquals(viewMealPlansOutputData.getTargetCalories(), expectedCalories);
                assertIterableEquals(viewMealPlansOutputData.getTargetProtein(), expectedProtein);
                assertIterableEquals(viewMealPlansOutputData.getTargetFats(), expectedFats);
                assertIterableEquals(viewMealPlansOutputData.getTargetCarbs(), expectedCarbs);
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected");
            }
        };

        ViewMealPlansInputBoundary interactor = new ViewMealPlansInteractor(userRepository, presenter);
        interactor.execute();

    }
}
