package use_case.approve_recipe;

import entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ApproveRecipeInteractor
 */
class ApproveRecipeInteractorTest {

    private TestApproveRecipeDAO dao;
    private TestApproveRecipePresenter presenter;
    private ApproveRecipeInteractor interactor;

    /**
     * Test implementation of ApproveRecipeDataAccessInterface
     */
    private static class TestApproveRecipeDAO implements ApproveRecipeDataAccessInterface {
        private List<Recipe> availableRecipes = new ArrayList<>();
        private Map<String, User> users = new HashMap<>();

        public TestApproveRecipeDAO() {
            // Create test user
            User testUser = new User("testUser", "password", new ArrayList<>(), 
                    new ArrayList<>(), new GroceryList(new ArrayList<>()));
            users.put("testUser", testUser);
        }

        public void setAvailableRecipes(List<Recipe> recipes) {
            this.availableRecipes = new ArrayList<>(recipes);
        }

        @Override
        public List<Recipe> getAvailableRecipes() {
            return new ArrayList<>(availableRecipes);
        }

        @Override
        public Recipe getRecipeById(int recipeId) {
            for (Recipe recipe : availableRecipes) {
                if (recipe.getRecipeId() == recipeId) {
                    return recipe;
                }
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
                // Check if already saved
                boolean exists = user.getSavedRecipes().stream()
                        .anyMatch(r -> r.getRecipeId() == recipe.getRecipeId());
                if (!exists) {
                    user.getSavedRecipes().add(recipe);
                }
            }
        }
    }

    /**
     * Test implementation of ApproveRecipeOutputBoundary
     */
    private static class TestApproveRecipePresenter implements ApproveRecipeOutputBoundary {
        private ApproveRecipeOutputData lastRecipeViewData;
        private ApproveRecipeOutputData lastApproveSuccessData;
        private ApproveRecipeOutputData lastDeclineData;
        private String lastFailMessage;

        @Override
        public void prepareRecipeView(ApproveRecipeOutputData outputData) {
            this.lastRecipeViewData = outputData;
        }

        @Override
        public void prepareApproveSuccessView(ApproveRecipeOutputData outputData) {
            this.lastApproveSuccessData = outputData;
        }

        @Override
        public void prepareDeclineView(ApproveRecipeOutputData outputData) {
            this.lastDeclineData = outputData;
        }

        @Override
        public void prepareFailView(String error) {
            this.lastFailMessage = error;
        }
    }

    @BeforeEach
    void setUp() {
        dao = new TestApproveRecipeDAO();
        presenter = new TestApproveRecipePresenter();
        interactor = new ApproveRecipeInteractor(dao, presenter);
    }

    @Test
    void testLoadRecipesWithNoRecipesAvailable() {
        // No recipes set in DAO
        interactor.loadRecipes();

        // Should call prepareFailView
        assertNotNull(presenter.lastFailMessage);
        assertEquals("No recipes available.", presenter.lastFailMessage);
    }

    @Test
    void testLoadRecipesSuccess() {
        // Setup test recipes
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(new Recipe(1, "Pasta", "img1.jpg", "Dinner"));
        recipes.add(new Recipe(2, "Salad", "img2.jpg", "Lunch"));
        dao.setAvailableRecipes(recipes);

        interactor.loadRecipes();

        // Should call prepareRecipeView
        assertNotNull(presenter.lastRecipeViewData);
        assertEquals(2, presenter.lastRecipeViewData.getRecipeIds().size());
        assertEquals("Pasta", presenter.lastRecipeViewData.getRecipeNames().get(0));
        assertEquals("Salad", presenter.lastRecipeViewData.getRecipeNames().get(1));
        assertTrue(presenter.lastRecipeViewData.hasMore());
    }

    @Test
    void testApproveRecipeSuccess() {
        // Setup
        List<Recipe> recipes = new ArrayList<>();
        Recipe testRecipe = new Recipe(1, "Pasta", "img1.jpg", "Dinner");
        recipes.add(testRecipe);
        dao.setAvailableRecipes(recipes);

        // Load recipes first
        interactor.loadRecipes();

        // Approve the recipe
        ApproveRecipeInputData inputData = new ApproveRecipeInputData(1, "testUser");
        interactor.approveRecipe(inputData);

        // Verify recipe was saved to user
        User user = dao.getUser("testUser");
        assertEquals(1, user.getSavedRecipes().size());
        assertEquals("Pasta", user.getSavedRecipes().get(0).getRecipeName());

        // Should call prepareApproveSuccessView (no more recipes)
        assertNotNull(presenter.lastApproveSuccessData);
        assertEquals(0, presenter.lastApproveSuccessData.getRecipeIds().size());
        assertFalse(presenter.lastApproveSuccessData.hasMore());
    }

    @Test
    void testApproveMultipleRecipes() {
        // Setup
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(new Recipe(1, "Pasta", "img1.jpg", "Dinner"));
        recipes.add(new Recipe(2, "Salad", "img2.jpg", "Lunch"));
        recipes.add(new Recipe(3, "Pizza", "img3.jpg", "Dinner"));
        dao.setAvailableRecipes(recipes);

        interactor.loadRecipes();

        // Approve first recipe
        interactor.approveRecipe(new ApproveRecipeInputData(1, "testUser"));

        // Verify user has 1 recipe
        assertEquals(1, dao.getUser("testUser").getSavedRecipes().size());

        // Should show next recipe
        assertNotNull(presenter.lastRecipeViewData);
        assertEquals("Salad", presenter.lastRecipeViewData.getRecipeNames().get(0));

        // Approve second recipe
        interactor.approveRecipe(new ApproveRecipeInputData(2, "testUser"));

        // Verify user has 2 recipes
        assertEquals(2, dao.getUser("testUser").getSavedRecipes().size());
    }

    @Test
    void testDeclineRecipeSuccess() {
        // Setup
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(new Recipe(1, "Pasta", "img1.jpg", "Dinner"));
        recipes.add(new Recipe(2, "Salad", "img2.jpg", "Lunch"));
        dao.setAvailableRecipes(recipes);

        interactor.loadRecipes();

        // Decline first recipe
        DeclineRecipeInputData inputData = new DeclineRecipeInputData(1, "testUser");
        interactor.declineRecipe(inputData);

        // Verify recipe was NOT saved
        User user = dao.getUser("testUser");
        assertEquals(0, user.getSavedRecipes().size());

        // Should show next recipe
        assertNotNull(presenter.lastRecipeViewData);
        assertEquals("Salad", presenter.lastRecipeViewData.getRecipeNames().get(0));
    }

    @Test
    void testApproveRecipeWithNoRecipeAvailable() {
        // Don't load any recipes
        ApproveRecipeInputData inputData = new ApproveRecipeInputData(1, "testUser");
        interactor.approveRecipe(inputData);

        // Should call prepareFailView
        assertNotNull(presenter.lastFailMessage);
        assertEquals("No recipe to approve.", presenter.lastFailMessage);
    }

    @Test
    void testDeclineRecipeWithNoRecipeAvailable() {
        // Don't load any recipes
        DeclineRecipeInputData inputData = new DeclineRecipeInputData(1, "testUser");
        interactor.declineRecipe(inputData);

        // Should call prepareFailView
        assertNotNull(presenter.lastFailMessage);
        assertEquals("No recipe to decline.", presenter.lastFailMessage);
    }

    @Test
    void testApproveRecipePreventsCreation() {
        // Setup
        List<Recipe> recipes = new ArrayList<>();
        Recipe testRecipe = new Recipe(1, "Pasta", "img1.jpg", "Dinner");
        recipes.add(testRecipe);
        dao.setAvailableRecipes(recipes);

        interactor.loadRecipes();

        // Approve same recipe twice
        interactor.approveRecipe(new ApproveRecipeInputData(1, "testUser"));

        // Reload and approve again (simulating duplicate)
        dao.setAvailableRecipes(recipes);
        interactor.loadRecipes();
        interactor.approveRecipe(new ApproveRecipeInputData(1, "testUser"));

        // Should only have 1 recipe (no duplicates)
        User user = dao.getUser("testUser");
        assertEquals(1, user.getSavedRecipes().size());
    }
}

