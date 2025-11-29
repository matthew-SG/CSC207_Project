package use_case.approve_recipe;

import entities.Recipe;
import entities.User;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ApproveRecipeInteractorTest {

    // Stub DAO for testing
    private class StubApproveRecipeDataAccess implements ApproveRecipeDataAccessInterface {
        private List<Recipe> availableRecipes = new ArrayList<>();
        private Map<String, User> users = new HashMap<>();

        public StubApproveRecipeDataAccess() {
            User user = new User("testUser", "password", new ArrayList<>(), new ArrayList<>(), null);
            users.put("testUser", user);
        }

        public void setAvailableRecipes(List<Recipe> recipes) {
            this.availableRecipes = recipes;
        }

        @Override
        public List<Recipe> getAvailableRecipes() {
            return availableRecipes;
        }

        @Override
        public Recipe getRecipeById(int recipeId) {
            for (Recipe r : availableRecipes) {
                if (r.getRecipeId() == recipeId) return r;
            }
            return null;
        }

        @Override
        public User getUser(String username) {
            return users.get(username);
        }

        @Override
        public void saveRecipeToUser(String username, Recipe recipe) {
            User user = users.get(username);
            if (user != null) {
                user.getSavedRecipes().add(recipe);
            }
        }
    }

    @Test
    void testLoadRecipesSuccess() {
        StubApproveRecipeDataAccess dataAccess = new StubApproveRecipeDataAccess();
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(new Recipe(1, "Test Recipe", "img", new ArrayList<>(), "Dinner", new HashMap<>()));
        dataAccess.setAvailableRecipes(recipes);

        ApproveRecipeOutputBoundary presenter = new ApproveRecipeOutputBoundary() {
            @Override
            public void prepareRecipeView(ApproveRecipeOutputData outputData) {
                assertEquals(1, outputData.getRecipeIds().size());
                assertEquals("Test Recipe", outputData.getRecipeNames().get(0));
                assertFalse(outputData.hasMore()); // Only 1 recipe
            }

            @Override
            public void prepareFailView(String error) {
                fail("Should not fail");
            }

            @Override
            public void prepareApproveSuccessView(ApproveRecipeOutputData outputData) {
                fail("Should not go to success view immediately");
            }

            @Override
            public void prepareDeclineView(ApproveRecipeOutputData outputData) {
                fail("Should not decline immediately");
            }
        };

        ApproveRecipeInteractor interactor = new ApproveRecipeInteractor(dataAccess, presenter);
        interactor.loadRecipes();
    }

    @Test
    void testApproveRecipe() {
        StubApproveRecipeDataAccess dataAccess = new StubApproveRecipeDataAccess();
        List<Recipe> recipes = new ArrayList<>();
        Recipe recipe = new Recipe(1, "Test Recipe", "img", new ArrayList<>(), "Dinner", new HashMap<>());
        recipes.add(recipe);
        dataAccess.setAvailableRecipes(recipes);

        ApproveRecipeOutputBoundary presenter = new ApproveRecipeOutputBoundary() {
            @Override
            public void prepareRecipeView(ApproveRecipeOutputData outputData) {
                // This might be called after approval if there are more recipes, or not if finished
            }

            @Override
            public void prepareFailView(String error) {
                fail("Should not fail: " + error);
            }

            @Override
            public void prepareApproveSuccessView(ApproveRecipeOutputData outputData) {
                // Should be called when no more recipes
                assertTrue(outputData.getRecipeIds().isEmpty());
            }

            @Override
            public void prepareDeclineView(ApproveRecipeOutputData outputData) {
                // Not called in approve test
            }
        };

        ApproveRecipeInteractor interactor = new ApproveRecipeInteractor(dataAccess, presenter);
        interactor.loadRecipes(); // Load first

        ApproveRecipeInputData inputData = new ApproveRecipeInputData(1, "testUser");
        interactor.approveRecipe(inputData);

        // Verify recipe saved to user
        User user = dataAccess.getUser("testUser");
        assertEquals(1, user.getSavedRecipes().size());
        assertEquals("Test Recipe", user.getSavedRecipes().get(0).getRecipeName());
    }

    @Test
    void testDeclineRecipe() {
        StubApproveRecipeDataAccess dataAccess = new StubApproveRecipeDataAccess();
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(new Recipe(1, "Test Recipe", "img", new ArrayList<>(), "Dinner", new HashMap<>()));
        dataAccess.setAvailableRecipes(recipes);

        ApproveRecipeOutputBoundary presenter = new ApproveRecipeOutputBoundary() {
            @Override
            public void prepareRecipeView(ApproveRecipeOutputData outputData) {
            }

            @Override
            public void prepareFailView(String error) {
                fail("Should not fail");
            }

            @Override
            public void prepareApproveSuccessView(ApproveRecipeOutputData outputData) {
            }

            @Override
            public void prepareDeclineView(ApproveRecipeOutputData outputData) {
                // Should be called
            }
        };

        ApproveRecipeInteractor interactor = new ApproveRecipeInteractor(dataAccess, presenter);
        interactor.loadRecipes();

        DeclineRecipeInputData inputData = new DeclineRecipeInputData(1, "testUser");
        interactor.declineRecipe(inputData);

        // Verify recipe NOT saved to user
        User user = dataAccess.getUser("testUser");
        assertEquals(0, user.getSavedRecipes().size());
    }
}

