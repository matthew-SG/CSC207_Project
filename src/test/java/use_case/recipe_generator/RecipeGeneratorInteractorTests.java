package use_case.recipe_generator;

import entities.Cuisine;
import entities.DietaryRestriction;
import entities.Intolerance;
import entities.Recipe;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for RecipeGeneratorInteractor (use case 1).
 */
class RecipeGeneratorInteractorTests {

    /**
     * Fake DAO that lets us:
     *  - control what recipes are returned
     *  - capture the arguments the interactor passes in
     *  - optionally throw an exception to simulate API failure
     */
    private static class FakeRecipeDAO implements RecipeDataAccessInterface {

        List<Recipe> recipesToReturn = new ArrayList<>();
        RuntimeException exceptionToThrow = null;

        // capture last call args (so we can assert them later)
        DietaryRestriction lastDiet;
        List<Intolerance> lastIntolerances;
        Cuisine lastCuisine;
        Integer lastMinCalories;
        Integer lastMaxCalories;
        Integer lastMinProtein;
        Integer lastMaxProtein;

        @Override
        public List<Recipe> getRecipes(DietaryRestriction dietaryRestriction,
                                       List<Intolerance> intolerances,
                                       Cuisine cuisine,
                                       Integer minCalories,
                                       Integer maxCalories,
                                       Integer minProtein,
                                       Integer maxProtein) {

            lastDiet = dietaryRestriction;
            lastIntolerances = intolerances;
            lastCuisine = cuisine;
            lastMinCalories = minCalories;
            lastMaxCalories = maxCalories;
            lastMinProtein = minProtein;
            lastMaxProtein = maxProtein;

            if (exceptionToThrow != null) {
                throw exceptionToThrow;
            }

            return recipesToReturn;
        }
    }

    /**
     * Spy presenter that records what the interactor sends.
     */
    private static class DummyRecipePresenter implements RecipeGeneratorOutputBoundary {

        GenerateRecipeOutputData lastOutputData;
        boolean prepareViewCalled = false;
        boolean presentErrorCalled = false;
        String lastErrorMessage;

        @Override
        public void prepareView(GenerateRecipeOutputData outputData) {
            prepareViewCalled = true;
            lastOutputData = outputData;
        }

        @Override
        public void presentError(String errorMessage) {
            presentErrorCalled = true;
            lastErrorMessage = errorMessage;
        }
    }

    @Test
    void generateRecipes_success_returnsRecipeSummaries() {
        // arrange: fake DAO with two recipes
        FakeRecipeDAO fakeDAO = new FakeRecipeDAO();
        DummyRecipePresenter spyPresenter = new DummyRecipePresenter();

        List<Recipe> daoRecipes = new ArrayList<>();

        Map<String, Double> nutrition = new HashMap<>();
        nutrition.put("calories", 500.0);
        nutrition.put("protein", 25.0);

        Recipe r1 = new Recipe(
                1,
                "Recipe One",
                "http://example.com/one.jpg",
                new ArrayList<>(),
                "UNKNOWN",
                new HashMap<>(nutrition)
        );
        Recipe r2 = new Recipe(
                2,
                "Recipe Two",
                "http://example.com/two.jpg",
                new ArrayList<>(),
                "UNKNOWN",
                new HashMap<>(nutrition)
        );

        daoRecipes.add(r1);
        daoRecipes.add(r2);
        fakeDAO.recipesToReturn = daoRecipes;

        // input filters
        GenerateRecipeInputData input = new GenerateRecipeInputData(
                DietaryRestriction.NONE,
                List.of(),          // no intolerances
                Cuisine.ANY,
                100,                // minCalories
                800,                // maxCalories
                10,                 // minProtein
                40                  // maxProtein
        );

        RecipeGeneratorInteractor interactor =
                new RecipeGeneratorInteractor(fakeDAO, spyPresenter);

        interactor.generateRecipes(input);

        // assert – DAO was called with our filters
        assertEquals(DietaryRestriction.NONE, fakeDAO.lastDiet);
        assertEquals(Cuisine.ANY, fakeDAO.lastCuisine);
        assertEquals(100, fakeDAO.lastMinCalories);
        assertEquals(800, fakeDAO.lastMaxCalories);
        assertEquals(10, fakeDAO.lastMinProtein);
        assertEquals(40, fakeDAO.lastMaxProtein);

        // assert – presenter was called correctly
        assertTrue(spyPresenter.prepareViewCalled);
        assertFalse(spyPresenter.presentErrorCalled);

        assertNotNull(spyPresenter.lastOutputData);
        assertEquals("", spyPresenter.lastOutputData.getMessage());

        List<RecipeSummary> summaries = spyPresenter.lastOutputData.getRecipes();
        assertEquals(2, summaries.size());

        RecipeSummary s1 = summaries.get(0);
        assertEquals("Recipe One", s1.getRecipeName());
        assertEquals(1, s1.getRecipeId());
        assertEquals("http://example.com/one.jpg", s1.getRecipeImage());
    }

    @Test
    void generateRecipes_noRecipes_showsNoRecipesMessage() {
        // arrange: fake DAO returns empty list
        FakeRecipeDAO fakeDAO = new FakeRecipeDAO();
        fakeDAO.recipesToReturn = new ArrayList<>();

        DummyRecipePresenter spyPresenter = new DummyRecipePresenter();

        GenerateRecipeInputData input = new GenerateRecipeInputData(
                DietaryRestriction.NONE,
                List.of(),
                Cuisine.ANY,
                100,
                800,
                10,
                40
        );

        RecipeGeneratorInteractor interactor =
                new RecipeGeneratorInteractor(fakeDAO, spyPresenter);

        interactor.generateRecipes(input);

        // assert – presenter called with empty recipes + proper message
        assertTrue(spyPresenter.prepareViewCalled);
        assertNotNull(spyPresenter.lastOutputData);

        assertEquals(0, spyPresenter.lastOutputData.getRecipes().size());

        String msg = spyPresenter.lastOutputData.getMessage();
        assertNotNull(msg);
        assertFalse(msg.isEmpty());
        assertTrue(msg.toLowerCase().contains("no recipes found, please try different filter options"));
    }

    @Test
    void generateRecipes_daoThrows_showsGenericErrorMessage() {
        // fake DAO throws to simulate API failure
        FakeRecipeDAO fakeDAO = new FakeRecipeDAO();
        fakeDAO.exceptionToThrow = new RuntimeException("Simulated API failure");

        DummyRecipePresenter spyPresenter = new DummyRecipePresenter();

        GenerateRecipeInputData input = new GenerateRecipeInputData(
                DietaryRestriction.NONE,
                List.of(),
                Cuisine.ANY,
                null,  // no filters
                null,
                null,
                null
        );

        RecipeGeneratorInteractor interactor =
                new RecipeGeneratorInteractor(fakeDAO, spyPresenter);

        interactor.generateRecipes(input);

        // assert – presenter still called, but with error message
        assertTrue(spyPresenter.prepareViewCalled);
        assertNotNull(spyPresenter.lastOutputData);

        // recipes list should be empty in error case
        assertEquals(0, spyPresenter.lastOutputData.getRecipes().size());

        String msg = spyPresenter.lastOutputData.getMessage();
        assertNotNull(msg);
        assertFalse(msg.isEmpty());
        assertTrue(msg.toLowerCase().contains("could not load recipes right now. please try again later."));
    }

    @Test
    void generateRecipes_nullRecipes_treatedAsNoRecipes() {
        // arrange: fake DAO returns null (not just empty list)
        FakeRecipeDAO fakeDAO = new FakeRecipeDAO();
        fakeDAO.recipesToReturn = null;  // ← key difference

        DummyRecipePresenter spyPresenter = new DummyRecipePresenter();

        GenerateRecipeInputData input = new GenerateRecipeInputData(
                DietaryRestriction.NONE,
                List.of(),
                Cuisine.ANY,
                100,
                800,
                10,
                40
        );

        RecipeGeneratorInteractor interactor =
                new RecipeGeneratorInteractor(fakeDAO, spyPresenter);

        interactor.generateRecipes(input);

        // assert – we go through the "no recipes" path
        assertTrue(spyPresenter.prepareViewCalled);
        assertNotNull(spyPresenter.lastOutputData);
        assertEquals(0, spyPresenter.lastOutputData.getRecipes().size());

        String msg = spyPresenter.lastOutputData.getMessage();
        assertNotNull(msg);
        assertFalse(msg.isEmpty());
        // use whatever exact text you have in the interactor:
        // "No recipes found, try changing your recipe filters"
        assertTrue(msg.toLowerCase().contains("no recipes"));
    }



}
