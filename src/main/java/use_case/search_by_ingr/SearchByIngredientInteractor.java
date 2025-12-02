package use_case.search_by_ingr;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import entities.Ingredient;
import entities.Recipe;
import entities.UnitConverter;
import use_case.approve_recipe.ApproveRecipeDataAccessInterface;

import static data_access.Constants.UNIT;

/**
 * Interactor for the search by ingredient use case.
 */
public class SearchByIngredientInteractor implements SearchByIngredientInputBoundary {

    private static final int MAX_RECIPES = 10;
    private static final String ID = "id";
    private static final String NOT_APPLICABLE = "N/A";
    private static final String NAME = "name";
    //private static final String UNIT = "unit";
    private static final String AMOUNT = "amount";

    private final SearchByIngredientGateway gateway;
    private final SearchByIngredientOutputBoundary presenter;
    private final ApproveRecipeDataAccessInterface approveRecipeDataAccessObject;

    /**
     * Creates a new interactor for the search by ingredient use case.
     *
     * @param gateway the gateway used to call the external API
     * @param presenter the presenter used to format output
     * @param approveRecipeDataAccessObject data access for available recipes (can be null)
     */
    public SearchByIngredientInteractor(SearchByIngredientGateway gateway,
                                        SearchByIngredientOutputBoundary presenter,
                                        ApproveRecipeDataAccessInterface approveRecipeDataAccessObject) {
        this.gateway = gateway;
        this.presenter = presenter;
        this.approveRecipeDataAccessObject = approveRecipeDataAccessObject;
    }

    /**
     * Executes the use case: validates input, calls the API, filters recipes,
     * and sends results to the presenter.
     *
     * @param inputData the input data containing ingredients and allowed missing count
     */
    @Override
    public void execute(SearchByIngredientInputData inputData) {
        final List<Ingredient> ingredients = inputData.getIngredients();

        if (ingredients == null || ingredients.isEmpty()) {
            presenter.prepareFailView("Enter at least one ingredient.");
            return;
        }

        final int allowedMissing = Math.max(0, inputData.getAmountMissing());

        final JSONObject apiResult = gateway.searchByIngredients(ingredients);

        if (apiResult == null) {
            presenter.prepareFailView("Failed to call the API.");
            return;
        }

        final JSONArray findResults = apiResult.getJSONArray("findResults");
        final JSONArray bulkResults = apiResult.getJSONArray("bulkResults");
        final ArrayList<Integer> acceptedIds = new ArrayList<>();

        for (int i = 0; i < findResults.length() && acceptedIds.size() < MAX_RECIPES; i++) {
            final JSONObject recipeJson = findResults.getJSONObject(i);

            if (shouldIncludeRecipe(recipeJson, ingredients, allowedMissing)) {
                final int id = recipeJson.getInt(ID);
                acceptedIds.add(id);
            }
        }

        if (acceptedIds.isEmpty()) {
            presenter.prepareSuccessView(new SearchByIngredientOutputData(
                    new ArrayList<>(),
                    "No recipes found within the allowed missing ingredients."
            ));
            return;
        }

        final List<Recipe> recipes = new ArrayList<>();
        for (int i = 0; i < bulkResults.length(); i++) {
            final JSONObject recipeJson = bulkResults.getJSONObject(i);
            final int id = recipeJson.getInt(ID);

            if (!acceptedIds.contains(id)) {
                continue;
            }

            final Recipe recipe = buildFullRecipeFromBulk(recipeJson);
            recipes.add(recipe);
        }
        if (approveRecipeDataAccessObject != null) {
            approveRecipeDataAccessObject.setAvailableRecipes(recipes);
        }
        final String message = "Found " + recipes.size() + " recipes.";
        presenter.prepareSuccessView(new SearchByIngredientOutputData(recipes, message));
    }

    /**
     * Decides whether a recipe should be included based on missing ingredients and quantities.
     *
     * @param recipeJson the recipe JSON from the API
     * @param userIngredients the user's ingredients
     * @param allowedMissing allowed number of missing ingredients
     * @return true if the recipe should be included, false otherwise
     */
    private boolean shouldIncludeRecipe(JSONObject recipeJson,
                                        List<Ingredient> userIngredients,
                                        int allowedMissing) {

        final int baseMissed = recipeJson.getInt("missedIngredientCount");

        // If API already reports more missed than allowed, bail early.
        if (baseMissed > allowedMissing) {
            return false;
        }

        final int extraMissingFromQuantity = calculateExtraMissingFromQuantity(recipeJson, userIngredients, baseMissed, allowedMissing);

        final int totalMissing = baseMissed + extraMissingFromQuantity;

        return totalMissing <= allowedMissing;
    }

    /**
     * Calculates extra "missing" ingredients based on quantity checks.
     *
     * @param recipeJson the recipe JSON from the API
     * @param userIngredients the user's ingredients
     * @param baseMissed base missed count from the API
     * @param allowedMissing allowed number of missing ingredients
     * @return additional missing count due to not enough quantity
     */
    private int calculateExtraMissingFromQuantity(JSONObject recipeJson,
                                                  List<Ingredient> userIngredients,
                                                  int baseMissed,
                                                  int allowedMissing) {

        final JSONArray usedIngredientsJson = recipeJson.getJSONArray("usedIngredients");
        int extraMissing = 0;

        for (int j = 0; j < usedIngredientsJson.length(); j++) {
            final JSONObject usedIngredient = usedIngredientsJson.getJSONObject(j);

            final String apiName = normalizeName(usedIngredient.getString("name"));
            final double requiredAmount = usedIngredient.getDouble(AMOUNT);

            final String unitShort = usedIngredient.optString("unitShort", "");
            final String unit = usedIngredient.optString(UNIT, unitShort);

            final Ingredient matchingUserIng = findMatchingUserIngredient(apiName, userIngredients);

            if (matchingUserIng == null) {
                // No ingredient by that name found; treat as missing
                extraMissing++;
            }
            else {
                // Convert user's amount into the recipe's unit and compare
                final double userQuantity = matchingUserIng.getQuantity();
                final double userInRecipeUnit = convertUserToRecipeUnit(userQuantity,
                        matchingUserIng.getUnit(),
                        unit);

                if (userInRecipeUnit < requiredAmount) {
                    // Not enough quantity -> counts as missing
                    extraMissing++;
                }
            }

            // Early exit: if total missing already exceeds allowed, no need to continue
            if (baseMissed + extraMissing > allowedMissing) {
                break;
            }
        }

        return extraMissing;
    }

    /**
     * Normalizes ingredient names for comparison.
     *
     * @param name the original name
     * @return a lowercased, trimmed, singular name
     */
    private String normalizeName(String name) {
        if (name == null) {
            return "";
        }
        String result = name.toLowerCase().trim();
        if (!result.isEmpty() && result.charAt(result.length() - 1) == 's') {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    /**
     * Finds a user ingredient that matches the API ingredient name.
     *
     * @param apiName the name from the API
     * @param userIngredients the user's ingredients
     * @return the matching ingredient or null if not found
     */
    private Ingredient findMatchingUserIngredient(String apiName, List<Ingredient> userIngredients) {
        for (Ingredient userIng : userIngredients) {
            final String userName = userIng.getName().toLowerCase();
            if (userName.contains(apiName) || apiName.contains(userName)) {
                return userIng;
            }
        }
        return null;
    }

    /**
     * Converts the user's quantity into the recipe's unit using the UnitConverter.
     *
     * @param userQuantity the user's quantity
     * @param userUnit the user's unit
     * @param recipeUnit the unit expected by the recipe
     * @return the quantity expressed in the recipe's unit
     */
    private double convertUserToRecipeUnit(double userQuantity,
                                           String userUnit,
                                           String recipeUnit) {
        final double asTbsp = UnitConverter.toTbsp(userQuantity, userUnit);
        return UnitConverter.fromTbsp(asTbsp, recipeUnit);
    }

    /**
     * Builds a full Recipe object from the bulk API JSON.
     *
     * @param recipeJson the recipe JSON from the bulk API
     * @return a Recipe object with ingredients, steps, and nutrition
     */
    private Recipe buildFullRecipeFromBulk(JSONObject recipeJson) {
        final int id = recipeJson.getInt(ID);
        final String title = recipeJson.optString("title", "");
        final String image = recipeJson.optString("image", "");
        final String mealType = extractMealType(recipeJson);

        final Recipe recipe = new Recipe(id, title, image, mealType);
        recipe.setIngredients(extractIngredients(recipeJson));
        recipe.setSteps(extractSteps(recipeJson));
        addNutrition(recipe, recipeJson);

        return recipe;
    }

    /**
     * Extracts the meal type (dish type) from the recipe JSON.
     *
     * @param recipeJson the recipe JSON
     * @return the first dish type or "N/A" if missing
     */
    private String extractMealType(JSONObject recipeJson) {
        if (!recipeJson.has("dishTypes")) {
            return NOT_APPLICABLE;
        }

        final JSONArray dishTypes = recipeJson.optJSONArray("dishTypes");
        if (dishTypes == null || dishTypes.length() == 0) {
            return NOT_APPLICABLE;
        }

        return dishTypes.optString(0, "N/A");
    }

    /**
     * Extracts ingredients from the recipe JSON into a list.
     *
     * @param recipeJson the recipe JSON
     * @return a list of Ingredient objects
     */
    private List<Ingredient> extractIngredients(JSONObject recipeJson) {
        final List<Ingredient> ingredients = new ArrayList<>();

        final JSONArray extIngr = recipeJson.optJSONArray("extendedIngredients");
        if (extIngr == null) {
            return ingredients;
        }

        for (int i = 0; i < extIngr.length(); i++) {
            final JSONObject ingJson = extIngr.getJSONObject(i);
            final String name = ingJson.optString(NAME, "");
            final double amount = ingJson.optDouble(AMOUNT, 0.0);
            final String unit = ingJson.optString(UNIT, "");
            ingredients.add(new Ingredient(name, amount, unit));
        }

        return ingredients;
    }

    /**
     * Extracts the step-by-step instructions as a single string.
     *
     * @param recipeJson the recipe JSON
     * @return the steps text, or an empty string if none exist
     */
    private String extractSteps(JSONObject recipeJson) {
        final StringBuilder stepsBuilder = new StringBuilder();

        final JSONArray instructions = recipeJson.optJSONArray("analyzedInstructions");
        if (instructions == null || instructions.isEmpty()) {
            return "";
        }

        final JSONObject firstInstr = instructions.optJSONObject(0);
        if (firstInstr == null) {
            return "";
        }

        final JSONArray steps = firstInstr.optJSONArray("steps");
        if (steps == null) {
            return "";
        }

        for (int i = 0; i < steps.length(); i++) {
            final JSONObject stepObj = steps.getJSONObject(i);
            final int number = stepObj.optInt("number", i + 1);
            final String stepText = stepObj.optString("step", "");
            stepsBuilder
                    .append(number)
                    .append(". ")
                    .append(stepText)
                    .append("\n");
        }

        return stepsBuilder.toString().trim();
    }

    /**
     * Adds basic nutrition information from the JSON into the Recipe.
     *
     * @param recipe the recipe to update
     * @param recipeJson the recipe JSON
     */
    private void addNutrition(Recipe recipe, JSONObject recipeJson) {
        final JSONObject nutrition = recipeJson.optJSONObject("nutrition");
        if (nutrition == null) {
            return;
        }

        final JSONArray nutrients = nutrition.optJSONArray("nutrients");
        if (nutrients == null) {
            return;
        }

        for (int i = 0; i < nutrients.length(); i++) {
            final JSONObject n = nutrients.getJSONObject(i);
            final String name = n.optString(NAME, "");
            final double amount = n.optDouble(AMOUNT, 0.0);
            //final String unit = n.optString(UNIT, "");
            if (!name.isEmpty()) {
                recipe.addNutritionalValue(name, amount);
            }
        }
    }
}