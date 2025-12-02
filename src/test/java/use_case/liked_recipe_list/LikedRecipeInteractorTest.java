package use_case.liked_recipe_list;

import entities.GroceryList;
import entities.Ingredient;
import entities.InstructionStep;
import entities.MealPlan;
import entities.Recipe;
import entities.RecipeInstructions;
import entities.User;
import org.junit.Test;
import use_case.step_by_step.StepByStepInputData;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Unit tests for LikedRecipeInteractor.
 * Aims for 100% line and branch coverage of the interactor.
 */
public class LikedRecipeInteractorTest {

    /**
     * Fake DataAccess implementing the boundary, fully controllable from tests.
     */
    private static class FakeDataAccess implements LikedRecipeDataAccessInterface {

        String currentUsername = "user1";

        // Storage
        Map<String, User> usersByName = new HashMap<>();
        Map<Integer, Recipe> recipesById = new HashMap<>();
        Map<String, List<Recipe>> likedByUser = new HashMap<>();

        // For handsfree
        List<InstructionStep> instructionsToReturn = new ArrayList<>();

        // For grocery integration
        String lastAddIngredientsUsername;
        List<Ingredient> lastAddedIngredients;

        // For delete
        String lastDeletedUsername;
        Integer lastDeletedRecipeId;

        // Flags to force exceptions
        boolean throwOnGetCurrentUsername = false;
        boolean throwOnGetLikedRecipes = false;
        boolean throwOnGetAnalyzedInstructions = false;
        boolean throwOnGetLikedRecipesInGrocery = false;

        // For addLikedRecipe save tracking
        String lastSavedUsername;
        Recipe lastSavedRecipe;

        @Override
        public void saveLikedRecipe(String username, Recipe recipe) {
            lastSavedUsername = username;
            lastSavedRecipe = recipe;

            // Mirror what real DAO would do: add to liked list
            List<Recipe> liked = likedByUser.computeIfAbsent(username, u -> new ArrayList<>());
            if (!liked.contains(recipe)) {
                liked.add(recipe);
            }
        }

        @Override
        public void deleteLikedRecipe(String username, int recipeId) {
            lastDeletedUsername = username;
            lastDeletedRecipeId = recipeId;

            List<Recipe> liked = likedByUser.get(username);
            if (liked != null) {
                liked.removeIf(r -> r.getRecipeId() == recipeId);
            }
        }

        @Override
        public List<Recipe> getLikedRecipes(String username) {
            if (throwOnGetLikedRecipes || throwOnGetLikedRecipesInGrocery) {
                throw new RuntimeException("Boom in getLikedRecipes");
            }
            List<Recipe> liked = likedByUser.get(username);
            if (liked != null) {
                return liked;
            }
            User u = usersByName.get(username);
            if (u != null) {
                return u.getSavedRecipes();
            }
            return new ArrayList<>();
        }

        @Override
        public Recipe getRecipeById(int recipeId) {
            return recipesById.get(recipeId);
        }

        @Override
        public String getCurrentUsername() {
            if (throwOnGetCurrentUsername) {
                throw new RuntimeException("Boom in getCurrentUsername");
            }
            return currentUsername;
        }

        @Override
        public List<InstructionStep> getAnalyzedInstructions(int recipeId) {
            if (throwOnGetAnalyzedInstructions) {
                throw new RuntimeException("Boom in getAnalyzedInstructions");
            }
            return instructionsToReturn;
        }

        @Override
        public User getUser(String username) {
            return usersByName.get(username);
        }

        @Override
        public void addIngredientsToGroceryList(String username, List<Ingredient> ingredients) {
            lastAddIngredientsUsername = username;
            lastAddedIngredients = new ArrayList<>(ingredients);
        }
    }

    /**
     * Fake presenter that just records what was last passed into it.
     */
    private static class FakePresenter implements LikedRecipeOutputBoundary {

        LikedRecipeOutputData lastOutput;
        StepByStepInputData lastHandsfreeInput;
        String lastError;

        int likedViewCalls = 0;
        int handsfreeCalls = 0;
        int failCalls = 0;

        @Override
        public void prepareLikedRecipeView(LikedRecipeOutputData likedRecipeOutputData) {
            likedViewCalls++;
            lastOutput = likedRecipeOutputData;
        }

        @Override
        public void prepareHandsfree(StepByStepInputData stepByStepInputData) {
            handsfreeCalls++;
            lastHandsfreeInput = stepByStepInputData;
        }

        @Override
        public void prepareFailView(String error) {
            failCalls++;
            lastError = error;
        }
    }

    // Helper to create a basic User object
    private User newUser(String username) {
        return new User(username, "pw",
                new ArrayList<>(),
                new ArrayList<MealPlan>(),
                new GroceryList(new ArrayList<>()));
    }

    // Helper to create a basic Recipe
    private Recipe newRecipe(int id, String name, List<Ingredient> ingredients) {
        Map<String, Double> nutr = new HashMap<>();
        nutr.put("Calories", 100.0);
        nutr.put("Protein", 10.0);
        return new Recipe(id, name, "img.jpg", ingredients, "MAIN", nutr);
    }

    // Helper to create input data (only id matters to interactor)
    private LikedRecipeInputData inputFor(int id) {
        return new LikedRecipeInputData(id, "name" + id, "img" + id, new HashMap<>());
    }

    // ---------- addLikedRecipe tests ----------

    @Test
    public void addLikedRecipe_successNewRecipe() {
        FakeDataAccess dao = new FakeDataAccess();
        FakePresenter presenter = new FakePresenter();

        User user = newUser("user1");
        dao.usersByName.put("user1", user);

        Recipe recipe = newRecipe(1, "R1", new ArrayList<>());
        dao.recipesById.put(1, recipe);

        LikedRecipeInteractor interactor = new LikedRecipeInteractor(dao, presenter);

        interactor.addLikedRecipe(inputFor(1));

        assertEquals(1, user.getSavedRecipes().size());
        assertEquals(recipe, user.getSavedRecipes().get(0));
        assertEquals("user1", dao.lastSavedUsername);
        assertEquals(recipe, dao.lastSavedRecipe);
        assertNull(presenter.lastError);
        assertNotNull(presenter.lastOutput); // from loadLikedRecipes()
    }

    @Test
    public void addLikedRecipe_recipeNotFound() {
        FakeDataAccess dao = new FakeDataAccess();
        FakePresenter presenter = new FakePresenter();

        dao.usersByName.put("user1", newUser("user1"));
        // recipeById is empty -> returns null

        LikedRecipeInteractor interactor = new LikedRecipeInteractor(dao, presenter);

        interactor.addLikedRecipe(inputFor(99));

        assertNull(dao.lastSavedRecipe);
        assertEquals(1, presenter.failCalls);
        assertTrue(presenter.lastError.contains("Recipe not found with ID: 99"));
    }

    @Test
    public void addLikedRecipe_userNotFound() {
        FakeDataAccess dao = new FakeDataAccess();
        FakePresenter presenter = new FakePresenter();

        Recipe recipe = newRecipe(1, "R1", new ArrayList<>());
        dao.recipesById.put(1, recipe);
        // usersByName has no "user1"

        LikedRecipeInteractor interactor = new LikedRecipeInteractor(dao, presenter);

        interactor.addLikedRecipe(inputFor(1));

        assertNull(dao.lastSavedRecipe);
        assertEquals(1, presenter.failCalls);
        assertTrue(presenter.lastError.contains("User not found: user1"));
    }

    @Test
    public void addLikedRecipe_alreadySaved() {
        FakeDataAccess dao = new FakeDataAccess();
        FakePresenter presenter = new FakePresenter();

        User user = newUser("user1");
        Recipe recipe = newRecipe(1, "R1", new ArrayList<>());
        user.getSavedRecipes().add(recipe); // already saved

        dao.usersByName.put("user1", user);
        dao.recipesById.put(1, recipe);

        LikedRecipeInteractor interactor = new LikedRecipeInteractor(dao, presenter);

        interactor.addLikedRecipe(inputFor(1));

        // Should not call saveLikedRecipe
        assertNull(dao.lastSavedRecipe);
        // But still should loadLikedRecipes successfully
        assertNotNull(presenter.lastOutput);
        assertEquals(0, presenter.failCalls);
    }

    @Test
    public void addLikedRecipe_exceptionHandled() {
        FakeDataAccess dao = new FakeDataAccess();
        FakePresenter presenter = new FakePresenter();

        dao.throwOnGetCurrentUsername = true;

        LikedRecipeInteractor interactor = new LikedRecipeInteractor(dao, presenter);

        interactor.addLikedRecipe(inputFor(1));

        assertEquals(1, presenter.failCalls);
        assertTrue(presenter.lastError.startsWith("Error adding recipe: "));
    }

    // ---------- deleteLikedRecipe tests ----------

    @Test
    public void deleteLikedRecipe_success() {
        FakeDataAccess dao = new FakeDataAccess();
        FakePresenter presenter = new FakePresenter();

        User user = newUser("user1");
        Recipe r1 = newRecipe(1, "R1", new ArrayList<>());
        Recipe r2 = newRecipe(2, "R2", new ArrayList<>());
        user.getSavedRecipes().add(r1);
        user.getSavedRecipes().add(r2);

        dao.usersByName.put("user1", user);
        dao.likedByUser.put("user1", user.getSavedRecipes());

        LikedRecipeInteractor interactor = new LikedRecipeInteractor(dao, presenter);

        interactor.deleteLikedRecipe(inputFor(1));

        assertEquals(1, user.getSavedRecipes().size());
        assertEquals(2, user.getSavedRecipes().get(0).getRecipeId());
        assertEquals("user1", dao.lastDeletedUsername);
        assertEquals(Integer.valueOf(1), dao.lastDeletedRecipeId);
        assertNotNull(presenter.lastOutput); // from loadLikedRecipes()
        assertEquals(0, presenter.failCalls);
    }

    @Test
    public void deleteLikedRecipe_userNotFound() {
        FakeDataAccess dao = new FakeDataAccess();
        FakePresenter presenter = new FakePresenter();
        // No user

        LikedRecipeInteractor interactor = new LikedRecipeInteractor(dao, presenter);

        interactor.deleteLikedRecipe(inputFor(1));

        assertEquals(1, presenter.failCalls);
        assertTrue(presenter.lastError.contains("User not found: user1"));
    }

    @Test
    public void deleteLikedRecipe_exceptionHandled() {
        FakeDataAccess dao = new FakeDataAccess();
        FakePresenter presenter = new FakePresenter();

        dao.throwOnGetCurrentUsername = true;

        LikedRecipeInteractor interactor = new LikedRecipeInteractor(dao, presenter);

        interactor.deleteLikedRecipe(inputFor(1));

        assertEquals(1, presenter.failCalls);
        assertTrue(presenter.lastError.startsWith("Error deleting recipe: "));
    }

    // ---------- loadLikedRecipes tests ----------

    @Test
    public void loadLikedRecipes_populatesOutput() {
        FakeDataAccess dao = new FakeDataAccess();
        FakePresenter presenter = new FakePresenter();

        User user = newUser("user1");

        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("Flour", 2.5, "cups"));
        ingredients.add(new Ingredient("Salt", 1.0, "tsp"));

        Recipe r1 = newRecipe(10, "Pizza", ingredients);
        user.getSavedRecipes().add(r1);

        dao.usersByName.put("user1", user);
        dao.likedByUser.put("user1", user.getSavedRecipes());

        LikedRecipeInteractor interactor = new LikedRecipeInteractor(dao, presenter);

        interactor.loadLikedRecipes();

        assertEquals(1, presenter.likedViewCalls);
        assertNotNull(presenter.lastOutput);

        LikedRecipeOutputData out = presenter.lastOutput;
        assertArrayEquals(new int[]{10}, out.getRecipeIds());
        assertArrayEquals(new String[]{"Pizza"}, out.getRecipeNames());
        assertEquals("img.jpg", out.getRecipeImages()[0]);
        assertEquals(1, out.getRecipeNutrition().size());
        assertEquals(1, out.getRecipeIngredients().size());

        List<String[]> ingStrings = out.getRecipeIngredients().get(0);
        assertEquals(2, ingStrings.size());
        assertEquals("Flour", ingStrings.get(0)[0]);
        assertEquals("2.5", ingStrings.get(0)[1]);
        assertEquals("cups", ingStrings.get(0)[2]);
    }

    @Test
    public void loadLikedRecipes_exceptionHandled() {
        FakeDataAccess dao = new FakeDataAccess();
        FakePresenter presenter = new FakePresenter();

        dao.throwOnGetLikedRecipes = true;

        LikedRecipeInteractor interactor = new LikedRecipeInteractor(dao, presenter);

        interactor.loadLikedRecipes();

        assertEquals(1, presenter.failCalls);
        assertTrue(presenter.lastError.startsWith("Error loading recipes: "));
    }

    // ---------- handsfree tests ----------

    @Test
    public void handsfree_success() {
        FakeDataAccess dao = new FakeDataAccess();
        FakePresenter presenter = new FakePresenter();

        dao.instructionsToReturn = Arrays.asList(
                new InstructionStep(1, "Step 1"),
                new InstructionStep(2, "Step 2")
        );

        LikedRecipeInteractor interactor = new LikedRecipeInteractor(dao, presenter);

        List<InstructionStep> result = interactor.handsfree(inputFor(123));

        assertEquals(2, result.size());
        assertEquals(1, presenter.handsfreeCalls);
        assertNotNull(presenter.lastHandsfreeInput);

        RecipeInstructions instructions = presenter.lastHandsfreeInput.instructions();
        assertEquals(2, instructions.steps().size());
        assertEquals("Step 1", instructions.steps().get(0).getStep());
    }

    @Test
    public void handsfree_exceptionHandled() {
        FakeDataAccess dao = new FakeDataAccess();
        FakePresenter presenter = new FakePresenter();

        dao.throwOnGetAnalyzedInstructions = true;

        LikedRecipeInteractor interactor = new LikedRecipeInteractor(dao, presenter);

        List<InstructionStep> result = interactor.handsfree(inputFor(123));

        assertTrue(result.isEmpty());
        assertEquals(1, presenter.failCalls);
        assertTrue(presenter.lastError.startsWith("Error loading instructions: "));
    }

    // ---------- addIngredientsToGrocery tests ----------

    @Test
    public void addIngredientsToGrocery_recipeNotFound() {
        FakeDataAccess dao = new FakeDataAccess();
        FakePresenter presenter = new FakePresenter();

        // liked recipes list is empty
        dao.likedByUser.put("user1", new ArrayList<>());

        LikedRecipeInteractor interactor = new LikedRecipeInteractor(dao, presenter);

        interactor.addIngredientsToGrocery(inputFor(999));

        assertNull(dao.lastAddIngredientsUsername);
        assertEquals(1, presenter.failCalls);
        assertTrue(presenter.lastError.contains("Recipe not found with ID: 999"));
    }

    @Test
    public void addIngredientsToGrocery_noIngredients() {
        FakeDataAccess dao = new FakeDataAccess();
        FakePresenter presenter = new FakePresenter();

        User user = newUser("user1");
        Recipe recipe = newRecipe(10, "Empty", new ArrayList<>());
        user.getSavedRecipes().add(recipe);

        dao.usersByName.put("user1", user);
        dao.likedByUser.put("user1", user.getSavedRecipes());

        LikedRecipeInteractor interactor = new LikedRecipeInteractor(dao, presenter);

        interactor.addIngredientsToGrocery(inputFor(10));

        assertNull(dao.lastAddIngredientsUsername);
        assertEquals(1, presenter.failCalls);
        assertTrue(presenter.lastError.contains("This recipe has no ingredients"));
    }

    @Test
    public void addIngredientsToGrocery_success() {
        FakeDataAccess dao = new FakeDataAccess();
        FakePresenter presenter = new FakePresenter();

        User user = newUser("user1");
        List<Ingredient> ing = new ArrayList<>();
        ing.add(new Ingredient("Flour", 2.0, "cups"));
        ing.add(new Ingredient("Salt", 1.0, "tsp"));
        Recipe recipe = newRecipe(10, "WithIngredients", ing);
        user.getSavedRecipes().add(recipe);

        dao.usersByName.put("user1", user);
        dao.likedByUser.put("user1", user.getSavedRecipes());

        LikedRecipeInteractor interactor = new LikedRecipeInteractor(dao, presenter);

        interactor.addIngredientsToGrocery(inputFor(10));

        assertEquals("user1", dao.lastAddIngredientsUsername);
        assertNotNull(dao.lastAddedIngredients);
        assertEquals(2, dao.lastAddedIngredients.size());
        assertEquals(1, presenter.likedViewCalls); // from loadLikedRecipes()
        assertEquals(0, presenter.failCalls);
    }

    @Test
    public void addIngredientsToGrocery_exceptionHandled() {
        FakeDataAccess dao = new FakeDataAccess();
        FakePresenter presenter = new FakePresenter();

        dao.throwOnGetLikedRecipesInGrocery = true;

        LikedRecipeInteractor interactor = new LikedRecipeInteractor(dao, presenter);

        interactor.addIngredientsToGrocery(inputFor(10));

        assertEquals(1, presenter.failCalls);
        assertTrue(presenter.lastError.startsWith("Error adding ingredients to grocery list: "));
    }

    @Test
    public void addLikedRecipe_alreadyHasDifferentRecipeId() {
        FakeDataAccess dao = new FakeDataAccess();
        FakePresenter presenter = new FakePresenter();

        User user = newUser("user1");

        // Existing recipe in list with a different ID
        Recipe existing = newRecipe(99, "Other", new ArrayList<>());
        user.getSavedRecipes().add(existing);

        // New recipe we’re trying to add (ID 1)
        Recipe newOne = newRecipe(1, "New", new ArrayList<>());

        dao.usersByName.put("user1", user);
        dao.recipesById.put(1, newOne);

        LikedRecipeInteractor interactor = new LikedRecipeInteractor(dao, presenter);

        interactor.addLikedRecipe(inputFor(1));

        // We should now have both recipes, and saveLikedRecipe was called for the new ID
        assertEquals(2, user.getSavedRecipes().size());
        assertEquals("user1", dao.lastSavedUsername);
        assertEquals(1, dao.lastSavedRecipe.getRecipeId());
        assertNotNull(presenter.lastOutput); // loadLikedRecipes called
        assertEquals(0, presenter.failCalls);
    }

    @Test
    public void addIngredientsToGrocery_nonMatchingRecipeIdInList() {
        FakeDataAccess dao = new FakeDataAccess();
        FakePresenter presenter = new FakePresenter();

        User user = newUser("user1");

        // One liked recipe, but with a different ID than the one we search for
        List<Ingredient> ing = new ArrayList<>();
        ing.add(new Ingredient("Flour", 2.0, "cups"));
        Recipe r = newRecipe(5, "SomeRecipe", ing); // ID 5
        user.getSavedRecipes().add(r);

        dao.usersByName.put("user1", user);
        dao.likedByUser.put("user1", user.getSavedRecipes());

        LikedRecipeInteractor interactor = new LikedRecipeInteractor(dao, presenter);

        // Look for ID 10, which doesn’t match any recipe
        interactor.addIngredientsToGrocery(inputFor(10));

        // Should behave like "recipe not found"
        assertNull(dao.lastAddIngredientsUsername);
        assertEquals(1, presenter.failCalls);
        assertTrue(presenter.lastError.contains("Recipe not found with ID: 10"));
    }

    @Test
    public void addIngredientsToGrocery_ingredientsNull() {
        FakeDataAccess dao = new FakeDataAccess();
        FakePresenter presenter = new FakePresenter();

        User user = newUser("user1");

        // Build a recipe with null ingredients
        Map<String, Double> nutr = new HashMap<>();
        nutr.put("Calories", 100.0);
        Recipe recipeWithNullIngredients =
                new Recipe(42, "NullIngredients", null, null, "MAIN", nutr);

        user.getSavedRecipes().add(recipeWithNullIngredients);

        dao.usersByName.put("user1", user);
        dao.likedByUser.put("user1", user.getSavedRecipes());

        LikedRecipeInteractor interactor = new LikedRecipeInteractor(dao, presenter);

        interactor.addIngredientsToGrocery(inputFor(42));

        // Should hit the "no ingredients" fail-view path
        assertNull(dao.lastAddIngredientsUsername);
        assertEquals(1, presenter.failCalls);
        assertTrue(presenter.lastError.contains("This recipe has no ingredients"));
    }
}
