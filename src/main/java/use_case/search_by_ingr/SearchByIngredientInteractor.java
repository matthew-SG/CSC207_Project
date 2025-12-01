package use_case.search_by_ingr;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import entities.Ingredient;
import entities.Recipe;
import entities.UnitConverter;

/**
 * Interactor for the search by ingredient use case.
 */
public class SearchByIngredientInteractor implements SearchByIngredientInputBoundary {

    private static final int MAX_RECIPES = 10;
    private static final String ID = "id";
    private static final String NOT_APPLICABLE = "N/A";
    private static final String NAME = "name";
    private static final String UNIT = "unit";
    private static final String AMOUNT = "amount";

    private final SearchByIngredientGateway gateway;
    private final SearchByIngredientOutputBoundary presenter;
    private final data_access.FileDataAccessObject approveRecipeDataAccessObject;

    public SearchByIngredientInteractor(SearchByIngredientGateway gateway,
                                        SearchByIngredientOutputBoundary presenter,
                                        data_access.FileDataAccessObject approveRecipeDataAccessObject) {
        this.gateway = gateway;
        this.presenter = presenter;
        this.approveRecipeDataAccessObject = approveRecipeDataAccessObject;
    }

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

    private Ingredient findMatchingUserIngredient(String apiName, List<Ingredient> userIngredients) {
        for (Ingredient userIng : userIngredients) {
            final String userName = userIng.getName().toLowerCase();
            if (userName.contains(apiName) || apiName.contains(userName)) {
                return userIng;
            }
        }
        return null;
    }

    private double convertUserToRecipeUnit(double userQuantity,
                                           String userUnit,
                                           String recipeUnit) {
        final double asTbsp = UnitConverter.toTbsp(userQuantity, userUnit);
        return UnitConverter.fromTbsp(asTbsp, recipeUnit);
    }

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
            final String unit = n.optString(UNIT, "");
            if (!name.isEmpty()) {
                recipe.addNutritionalValue(name + " (" + unit + ")", amount);
            }
        }
    }
}
