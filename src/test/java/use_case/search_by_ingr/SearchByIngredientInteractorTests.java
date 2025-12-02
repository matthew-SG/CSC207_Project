package use_case.search_by_ingr;

import data_access.FileDataAccessObject;
import entities.Ingredient;
import entities.Recipe;
import entities.UserFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class SearchByIngredientInteractorTests {

    //helpers
    private static class RecordingPresenter implements SearchByIngredientOutputBoundary {
        SearchByIngredientOutputData lastSuccess;
        String lastError;

        @Override
        public void prepareSuccessView(SearchByIngredientOutputData outputData) {
            lastSuccess = outputData;
        }

        @Override
        public void prepareFailView(String error) {
            lastError = error;
        }
    }

    private static class FakeGateway implements SearchByIngredientGateway {
        private final JSONObject result;
        FakeGateway(JSONObject result) { this.result = result; }
        @Override public JSONObject searchByIngredients(List<Ingredient> ingredients) { return result; }
    }

    /** Build an apiResult JSONObject from bulk recipes, sharing id/missed/used fields into findResults. */
    private static JSONObject apiResult(JSONObject... bulkRecipes) {
        JSONArray findResults = new JSONArray();
        JSONArray bulkResults = new JSONArray();
        for (JSONObject recipe : bulkRecipes) {
            int id = recipe.getInt("id");
            JSONObject findEntry = new JSONObject()
                    .put("id", id)
                    .put("missedIngredientCount", recipe.optInt("missedIngredientCount", 0))
                    .put("usedIngredients",
                            recipe.optJSONArray("usedIngredients") != null
                                    ? recipe.optJSONArray("usedIngredients")
                                    : new JSONArray());
            findResults.put(findEntry);
            bulkResults.put(recipe);
        }
        return new JSONObject()
                .put("findResults", findResults)
                .put("bulkResults", bulkResults);
    }

    /** Minimal “blank” recipe with all keys present. Tests can tweak fields as needed. */
    private static JSONObject baseRecipe(int id, String title) {
        return new JSONObject()
                .put("id", id)
                .put("title", title)
                .put("image", "")
                .put("missedIngredientCount", 0)
                .put("usedIngredients", new JSONArray())
                .put("dishTypes", new JSONArray())
                .put("extendedIngredients", new JSONArray())
                .put("analyzedInstructions", new JSONArray())
                .put("nutrition", new JSONObject());
    }




    @Test
    public void testExecuteValidationAndApiNull() {
        RecordingPresenter presenter = new RecordingPresenter();
        SearchByIngredientGateway dummyGateway = new FakeGateway(new JSONObject());

        SearchByIngredientInteractor interactor =
                new SearchByIngredientInteractor(dummyGateway, presenter, null);

        //ingredients == null
        interactor.execute(new SearchByIngredientInputData(null, 0));
        assertEquals("Enter at least one ingredient.", presenter.lastError);
        assertNull(presenter.lastSuccess);

        //ingredients empty
        presenter.lastError = null;
        interactor.execute(new SearchByIngredientInputData(Collections.emptyList(), 2));
        assertEquals("Enter at least one ingredient.", presenter.lastError);
        assertNull(presenter.lastSuccess);

        //apiResult == null + negative allowedMissing
        presenter.lastError = null;
        List<Ingredient> ingredients = List.of(new Ingredient("carrot", 1.0, "cup"));
        SearchByIngredientInteractor interactorNullApi =
                new SearchByIngredientInteractor(new FakeGateway(null), presenter, null);

        interactorNullApi.execute(new SearchByIngredientInputData(ingredients, -5));
        assertEquals("Failed to call the API.", presenter.lastError);
        assertNull(presenter.lastSuccess);
    }

    @Test
    public void testNoRecipesAcceptedWhenBaseMissedTooHigh() {
        RecordingPresenter presenter = new RecordingPresenter();

        JSONObject api = new JSONObject()
                .put("findResults", new JSONArray().put(new JSONObject()
                        .put("id", 1)
                        .put("missedIngredientCount", 5)
                        .put("usedIngredients", new JSONArray())))
                .put("bulkResults", new JSONArray());

        SearchByIngredientInteractor interactor =
                new SearchByIngredientInteractor(new FakeGateway(api), presenter, null);

        List<Ingredient> userIngredients = List.of(new Ingredient("carrot", 1.0, "cup"));
        interactor.execute(new SearchByIngredientInputData(userIngredients, 0));

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastSuccess);
        assertTrue(presenter.lastSuccess.getRecipes().isEmpty());
        assertEquals("No recipes found within the allowed missing ingredients.",
                presenter.lastSuccess.getMsg());
    }

    @Test
    public void testSuccessfulFlowAndDaoUpdate() {
        RecordingPresenter presenter = new RecordingPresenter();

        List<Ingredient> userIngredients = List.of(new Ingredient("carrot", 4.0, "cup"));

        JSONArray usedIngredients = new JSONArray()
                .put(new JSONObject()
                        .put("name", "carrots")
                        .put("amount", 1.0)
                        .put("unitShort", "cup")
                        .put("unit", "cup"));

        JSONObject bulkRecipe = baseRecipe(1, "Carrot Soup")
                .put("image", "image.jpg")
                .put("usedIngredients", usedIngredients)
                .put("dishTypes", new JSONArray().put("soup"));

        // extendedIngredients
        bulkRecipe.getJSONArray("extendedIngredients").put(
                new JSONObject()
                        .put("name", "carrot")
                        .put("amount", 1.0)
                        .put("unit", "cup")
        );

        // steps
        JSONArray steps = new JSONArray()
                .put(new JSONObject().put("number", 1).put("step", "Chop carrots."))
                .put(new JSONObject().put("number", 2).put("step", "Cook carrots."));
        bulkRecipe.getJSONArray("analyzedInstructions").put(
                new JSONObject().put("steps", steps)
        );

        // nutrition
        JSONArray nutrients = new JSONArray()
                .put(new JSONObject().put("name", "Calories").put("amount", 100.0).put("unit", "kcal"))
                .put(new JSONObject().put("name", "Protein").put("amount", 5.0).put("unit", "g"));
        bulkRecipe.getJSONObject("nutrition").put("nutrients", nutrients);

        JSONObject api = apiResult(bulkRecipe);

        UserFactory userFactory = new UserFactory();
        FileDataAccessObject dao =
                new FileDataAccessObject("build/test-users-search-by-ingredient.csv", userFactory);

        SearchByIngredientInteractor interactor =
                new SearchByIngredientInteractor(new FakeGateway(api), presenter, dao);

        interactor.execute(new SearchByIngredientInputData(userIngredients, 0));

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastSuccess);
        List<Recipe> recipes = presenter.lastSuccess.getRecipes();
        assertEquals(1, recipes.size());
        assertEquals("Found 1 recipes.", presenter.lastSuccess.getMsg());

        Recipe r = recipes.get(0);
        assertEquals(1, r.getRecipeId());
        assertEquals("Carrot Soup", r.getRecipeName());
        assertEquals("soup", r.getMealType());
        assertEquals(1, r.getIngredients().size());
        assertEquals("carrot", r.getIngredients().get(0).getName());
        assertTrue(r.getNutritionalValues().containsKey("Calories"));
        assertTrue(r.getNutritionalValues().containsKey("Protein"));

        List<Recipe> available = dao.getAvailableRecipes();
        assertEquals(1, available.size());
        assertEquals(1, available.get(0).getRecipeId());
    }

    @Test
    public void testQuantityInsufficientCausesRejection() {
        RecordingPresenter presenter = new RecordingPresenter();
        List<Ingredient> userIngredients = List.of(new Ingredient("salt", 1.0, "tsp"));

        JSONArray used = new JSONArray().put(new JSONObject()
                .put("name", "salt")
                .put("amount", 1.0)
                .put("unitShort", "tbsp")
                .put("unit", "tbsp"));

        JSONObject bulkRecipe = baseRecipe(10, "Too Salty Soup")
                .put("usedIngredients", used)
                .put("missedIngredientCount", 1)
                .put("dishTypes", new JSONArray().put("soup"));

        JSONObject api = apiResult(bulkRecipe);

        SearchByIngredientInteractor interactor =
                new SearchByIngredientInteractor(new FakeGateway(api), presenter, null);

        interactor.execute(new SearchByIngredientInputData(userIngredients, 1));

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastSuccess);
        assertTrue(presenter.lastSuccess.getRecipes().isEmpty());
    }

    @Test
    public void testExtraMissingFromUnknownIngredientAllowedWithinLimit() {
        RecordingPresenter presenter = new RecordingPresenter();
        List<Ingredient> userIngredients = List.of(new Ingredient("garlic", 2.0, "tbsp"));

        JSONArray usedOnion = new JSONArray().put(new JSONObject()
                .put("name", "onion")
                .put("amount", 1.0)
                .put("unitShort", "tbsp")
                .put("unit", "tbsp"));

        JSONObject bulkRecipe = baseRecipe(20, "Onion-Free Dish")
                .put("usedIngredients", usedOnion);

        JSONObject api = apiResult(bulkRecipe);

        SearchByIngredientInteractor interactor =
                new SearchByIngredientInteractor(new FakeGateway(api), presenter, null);

        // allowedMissing = 1
        interactor.execute(new SearchByIngredientInputData(userIngredients, 1));

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastSuccess);
        assertEquals(1, presenter.lastSuccess.getRecipes().size());
        assertEquals(20, presenter.lastSuccess.getRecipes().get(0).getRecipeId());
    }

    @Test
    public void testBulkParsingEdgeCasesAndMaxRecipesLimit() {
        RecordingPresenter presenter = new RecordingPresenter();
        List<Ingredient> userIngredients = List.of(new Ingredient("anything", 1.0, "tbsp"));

        JSONObject r1 = baseRecipe(1, "No Meta");
        r1.remove("dishTypes");
        r1.remove("analyzedInstructions");
        r1.remove("nutrition");
        r1.remove("extendedIngredients");

        JSONObject r2 = baseRecipe(2, "Weird Instructions");
        r2.put("dishTypes", new JSONArray());
        r2.getJSONArray("analyzedInstructions").put(JSONObject.NULL);

        JSONObject r3 = baseRecipe(3, "No Steps");
        r3.put("dishTypes", new JSONArray().put("main course"));
        r3.getJSONArray("analyzedInstructions").put(new JSONObject());
        r3.getJSONObject("nutrition").put("nutrients", new JSONArray());

        JSONObject[] many = new JSONObject[12];
        for (int i = 0; i < 12; i++) {
            many[i] = baseRecipe(100 + i, "Recipe " + i);
        }

        JSONObject api = apiResult(r1, r2, r3, many[0], many[1], many[2], many[3], many[4],
                many[5], many[6], many[7], many[8], many[9], many[10], many[11]);

        SearchByIngredientInteractor interactor =
                new SearchByIngredientInteractor(new FakeGateway(api), presenter, null);

        interactor.execute(new SearchByIngredientInputData(userIngredients, 0));

        assertNull(presenter.lastError);
        assertNotNull(presenter.lastSuccess);

        List<Recipe> recipes = presenter.lastSuccess.getRecipes();
        // 3 + 12 = 15 > 10
        assertEquals(10, recipes.size());

        Recipe recipe1 = recipes.get(0);
        Recipe recipe2 = recipes.get(1);
        Recipe recipe3 = recipes.get(2);

        assertEquals("N/A", recipe1.getMealType());
        assertEquals("N/A", recipe2.getMealType());
        assertEquals("main course", recipe3.getMealType());

        assertEquals("", recipe1.getSteps());
        assertEquals("", recipe2.getSteps());
        assertEquals("", recipe3.getSteps());

        assertTrue(recipe1.getNutritionalValues().isEmpty());
        assertTrue(recipe2.getNutritionalValues().isEmpty());
        assertTrue(recipe3.getNutritionalValues().isEmpty());
    }

    @Test
    public void testMealTypeWhenDishTypesPresentButNotArray() {
        RecordingPresenter presenter = new RecordingPresenter();
        List<Ingredient> userIngredients = List.of(new Ingredient("anything", 1.0, "tbsp"));

        JSONObject recipeJson = baseRecipe(600, "Weird DishTypes")
                .put("dishTypes", "not-an-array");

        JSONObject api = apiResult(recipeJson);

        SearchByIngredientInteractor interactor =
                new SearchByIngredientInteractor(new FakeGateway(api), presenter, null);

        interactor.execute(new SearchByIngredientInputData(userIngredients, 0));

        Recipe r = presenter.lastSuccess.getRecipes().get(0);
        assertEquals("N/A", r.getMealType());
    }

    @Test
    public void testFindMatchingIngredientReverseContainsAndNutritionSkipEmptyName() {
        RecordingPresenter presenter = new RecordingPresenter();
        List<Ingredient> userIngredients = List.of(new Ingredient("salt", 2.0, "tsp"));

        JSONArray usedIngredients = new JSONArray()
                .put(new JSONObject()
                        .put("name", "sea salt")
                        .put("amount", 1.0)
                        .put("unitShort", "tsp")
                        .put("unit", "tsp"));

        JSONObject recipeJson = baseRecipe(123, "Sea Salt Soup")
                .put("usedIngredients", usedIngredients);

        JSONArray nutrients = new JSONArray()
                .put(new JSONObject().put("name", "").put("amount", 999.0).put("unit", "g"))
                .put(new JSONObject().put("name", "Fiber").put("amount", 5.0).put("unit", "g"));
        recipeJson.getJSONObject("nutrition").put("nutrients", nutrients);

        JSONObject api = apiResult(recipeJson);

        SearchByIngredientInteractor interactor =
                new SearchByIngredientInteractor(new FakeGateway(api), presenter, null);

        interactor.execute(new SearchByIngredientInputData(userIngredients, 0));

        Recipe r = presenter.lastSuccess.getRecipes().get(0);
        assertEquals(123, r.getRecipeId());
        assertEquals(1, r.getNutritionalValues().size());
        assertTrue(r.getNutritionalValues().containsKey("Fiber"));
    }

    @Test
    public void testNormalizeNameNullEmptyAndPluralViaReflection() throws Exception {
        SearchByIngredientInteractor interactor =
                new SearchByIngredientInteractor(null, null, null);

        Method normalize = SearchByIngredientInteractor.class
                .getDeclaredMethod("normalizeName", String.class);
        normalize.setAccessible(true);

        assertEquals("", normalize.invoke(interactor, new Object[]{null}));
        assertEquals("", normalize.invoke(interactor, ""));
        assertEquals("carrot", normalize.invoke(interactor, "carrots"));
    }
}
