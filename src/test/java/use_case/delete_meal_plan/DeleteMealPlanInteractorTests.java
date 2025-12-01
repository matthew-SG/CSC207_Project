package use_case.delete_meal_plan;

import data_access.InMemoryUserDataAccessObject;
import entities.MealPlan;
import entities.Recipe;
import org.junit.jupiter.api.Test;
import use_case.view_meal_plans.ViewMealPlansOutputBoundary;
import use_case.view_meal_plans.ViewMealPlansOutputData;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class DeleteMealPlanInteractorTests {

    /**
     * Tests the interactor when the user only has one meal plan saved
     */
    @Test
    void failureOnlyOneMealPlan() {
        // Create DAO and user to test one
        InMemoryUserDataAccessObject userRepository = new InMemoryUserDataAccessObject();
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
}
