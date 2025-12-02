package use_case.delete_meal_plan;

import data_access.InMemoryUserDataAccessObject;
import entities.MealPlan;
import entities.Recipe;
import org.junit.jupiter.api.Test;
import use_case.view_meal_plans.ViewMealPlansOutputBoundary;
import use_case.view_meal_plans.ViewMealPlansOutputData;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeleteMealPlanInteractorTests {

    /**
     * Tests the interactor when the user only has one meal plan saved
     */
    @Test
    void failureOnlyOneMealPlan() {
        // Create DAO and user to test one, apiKey doesn't matter
        InMemoryUserDataAccessObject userRepository = new InMemoryUserDataAccessObject("a");
        userRepository.signupUser("Matthew", "password");
        userRepository.login("Matthew", "password");

        // Create the input data (index doesn't matter for this case)
        DeleteMealPlanInputData inputData = new DeleteMealPlanInputData(0);

        // Create the meal plan for the tested user
        Recipe recipe1 = new Recipe(1234, "spaghetti", "idontexist.jpg", "italian");
        Recipe recipe2 = new Recipe(1324, "linguini", "idontexist.jpg", "italian");
        Recipe recipe3 = new Recipe(3124, "fettuccine", "idontexist.jpg", "italian");

        List<Recipe> recipes = new ArrayList<>();
        recipes.add(recipe1);
        recipes.add(recipe2);
        recipes.add(recipe3);

        MealPlan mealPlan = new MealPlan(recipes, 1, 1, 1, 1);

        userRepository.saveMealPlan(mealPlan);

        // Create the presenters to test the interactor
        DeleteMealPlanOutputBoundary presenter1 = new DeleteMealPlanOutputBoundary() {
            @Override
            public void prepareFailureView(String error) {
                assertEquals("Cannot delete only meal plan!", error);
            }
        };

        ViewMealPlansOutputBoundary presenter2 = new ViewMealPlansOutputBoundary() {
            @Override
            public void prepareSuccessView(ViewMealPlansOutputData viewMealPlansOutputData) {
                fail("Use case success is unexpected");
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected");
            }
        };

        DeleteMealPlanInputBoundary interactor = new DeleteMealPlanInteractor(userRepository, presenter1, presenter2);
        interactor.execute(inputData);
    }

    /**
     * Tests the interactor when the input index is out of the bounds of the user's meal plan list
     */
    @Test
    void failureIndexOutOfBounds() {
        // Create DAO and user to test one, api key doesn't matter
        InMemoryUserDataAccessObject userRepository = new InMemoryUserDataAccessObject("a");
        userRepository.signupUser("Matthew", "password");
        userRepository.login("Matthew", "password");

        // Create the input data (index is out of bounds, one for less than 0, and one for greater than the
        //      largest index)
        DeleteMealPlanInputData inputData1 = new DeleteMealPlanInputData(-1);
        DeleteMealPlanInputData inputData2 = new DeleteMealPlanInputData(2);

        // Create the meal plans for the tested user
        Recipe recipe1 = new Recipe(1234, "spaghetti", "idontexist.jpg", "italian");
        Recipe recipe2 = new Recipe(1324, "linguini", "idontexist.jpg", "italian");
        Recipe recipe3 = new Recipe(3124, "fettuccine", "idontexist.jpg", "italian");

        List<Recipe> recipes = new ArrayList<>();
        recipes.add(recipe1);
        recipes.add(recipe2);
        recipes.add(recipe3);

        MealPlan mealPlan1 = new MealPlan(recipes, 1, 1, 1, 1);
        MealPlan mealPlan2 = new MealPlan(recipes, 1, 1, 1, 1);

        userRepository.saveMealPlan(mealPlan1);
        userRepository.saveMealPlan(mealPlan2);

        // Create the presenters to test the interactor
        DeleteMealPlanOutputBoundary presenter1 = new DeleteMealPlanOutputBoundary() {
            @Override
            public void prepareFailureView(String error) {
                assertEquals("Error: Index out of bounds!", error);
            }
        };

        ViewMealPlansOutputBoundary presenter2 = new ViewMealPlansOutputBoundary() {
            @Override
            public void prepareSuccessView(ViewMealPlansOutputData viewMealPlansOutputData) {
                fail("Use case success is unexpected");
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected");
            }
        };

        DeleteMealPlanInputBoundary interactor = new DeleteMealPlanInteractor(userRepository, presenter1, presenter2);
        interactor.execute(inputData1);
        interactor.execute(inputData2);
    }

    /**
     * Tests the interactor when the index are in bounds, and they have more than one recipe
     */
    @Test
    void successTest() {
        // Create DAO and user to test one, apiKey doesn't matter
        InMemoryUserDataAccessObject userRepository = new InMemoryUserDataAccessObject("a");
        userRepository.signupUser("Matthew", "password");
        userRepository.login("Matthew", "password");

        // Create the input data (index doesn't matter for this case)
        DeleteMealPlanInputData inputData = new DeleteMealPlanInputData(0);

        // Create the meal plan for the tested user
        Recipe recipe1 = new Recipe(1234, "spaghetti", "idontexist.jpg", "italian");
        Recipe recipe2 = new Recipe(1324, "linguini", "idontexist.jpg", "italian");
        Recipe recipe3 = new Recipe(3124, "fettuccine", "idontexist.jpg", "italian");
        Recipe recipe4 = new Recipe(5678, "tortellini", "iDOexist.jpg", "italian");

        List<Recipe> recipes1 = new ArrayList<>();
        recipes1.add(recipe1);
        recipes1.add(recipe2);
        recipes1.add(recipe3);

        List<Recipe> recipes2 = new ArrayList<>();
        recipes2.add(recipe4);
        recipes2.add(recipe3);
        recipes2.add(recipe2);

        MealPlan mealPlan1 = new MealPlan(recipes1, 1, 1, 1, 1);
        MealPlan mealPlan2 = new MealPlan(recipes2, 2, 2, 2, 2);

        userRepository.saveMealPlan(mealPlan1);
        userRepository.saveMealPlan(mealPlan2);

        // Create expected values
        List<String> expectedNames = new ArrayList<>();
        expectedNames.add("tortellini");

        List<Double> expectedCalories = new ArrayList<>();
        expectedCalories.add(2.0);

        List<Double> expectedProteins = new ArrayList<>();
        expectedProteins.add(2.0);

        List<Double> expectedFats = new ArrayList<>();
        expectedFats.add(2.0);

        List<Double> expectedCarbs = new ArrayList<>();
        expectedCarbs.add(2.0);

        // Create the presenters to test the interactor
        DeleteMealPlanOutputBoundary presenter1 = new DeleteMealPlanOutputBoundary() {
            @Override
            public void prepareFailureView(String error) {
                fail("Use case failure is unexpected");
            }
        };

        ViewMealPlansOutputBoundary presenter2 = new ViewMealPlansOutputBoundary() {
            @Override
            public void prepareSuccessView(ViewMealPlansOutputData viewMealPlansOutputData) {
                assertIterableEquals(expectedNames, viewMealPlansOutputData.getFirstRecipeNames());
                assertIterableEquals(expectedCalories, viewMealPlansOutputData.getTargetCalories());
                assertIterableEquals(expectedProteins, viewMealPlansOutputData.getTargetProtein());
                assertIterableEquals(expectedFats, viewMealPlansOutputData.getTargetFats());
                assertIterableEquals(expectedCarbs, viewMealPlansOutputData.getTargetCarbs());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected");
            }
        };

        DeleteMealPlanInputBoundary interactor = new DeleteMealPlanInteractor(userRepository, presenter1, presenter2);
        interactor.execute(inputData);
    }
}
