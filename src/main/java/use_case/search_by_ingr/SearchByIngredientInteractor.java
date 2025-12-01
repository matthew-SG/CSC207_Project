package use_case.search_by_ingr;

import entities.Ingredient;
import entities.Recipe;
import entities.unitConverter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchByIngredientInteractor implements SearchByIngredientInputBoundary {

    private static final int MAX_RECIPES = 10;
    private final SearchByIngredientGateway gateway;
    private final SearchByIngredientOutputBoundary presenter;
    private final data_access.FileDataAccessObject approveRecipeDAO;

    public SearchByIngredientInteractor(SearchByIngredientGateway gateway,
                                        SearchByIngredientOutputBoundary presenter,
                                        data_access.FileDataAccessObject approveRecipeDAO) {
        this.gateway = gateway;
        this.presenter = presenter;
        this.approveRecipeDAO = approveRecipeDAO;
    }

    @Override
    public void execute(SearchByIngredientInputData inputData) {
        List<Ingredient> ingredients = inputData.getIngredients();

        if (ingredients == null || ingredients.isEmpty()) {
            presenter.prepareFailView("Enter at least one ingredient.");
            return;
        }

        int allowedMissing = Math.max(0, inputData.getAmountMissing());

        JSONObject apiResult = gateway.searchByIngredients(ingredients);

        if (apiResult == null) {
            presenter.prepareFailView("Failed to call the API.");
            return;
        }

        JSONArray findResults = apiResult.getJSONArray("findResults");
        JSONArray bulkResults = apiResult.getJSONArray("bulkResults");
        ArrayList<Integer> acceptedIds = new ArrayList<>();

        for (int i = 0; i < findResults.length() && acceptedIds.size() < MAX_RECIPES; i++) {
            JSONObject recipeJson = findResults.getJSONObject(i);

            if (shouldIncludeRecipe(recipeJson, ingredients, allowedMissing)) {
                int id = recipeJson.getInt("id");
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

        List<Recipe> recipes = new ArrayList<>();
        for (int i = 0; i < bulkResults.length(); i++) {
            JSONObject recipeJson = bulkResults.getJSONObject(i);
            int id = recipeJson.getInt("id");

            if (!acceptedIds.contains(id)) {
                continue;
            }

            Recipe recipe = buildFullRecipeFromBulk(recipeJson);
            recipes.add(recipe);
        }
        if (approveRecipeDAO != null) {
            approveRecipeDAO.setAvailableRecipes(recipes);
        }
        String message = "Found " + recipes.size() + " recipes.";
        presenter.prepareSuccessView(new SearchByIngredientOutputData(recipes, message));
    }

    private boolean shouldIncludeRecipe(JSONObject recipeJson,
                                        List<Ingredient> userIngredients,
                                        int allowedMissing) {

        int baseMissed = recipeJson.getInt("missedIngredientCount");

        // If API already reports more missed than allowed, bail early.
        if (baseMissed > allowedMissing) {
            return false;
        }

        int extraMissingFromQuantity = calculateExtraMissingFromQuantity(recipeJson, userIngredients, baseMissed, allowedMissing);

        int totalMissing = baseMissed + extraMissingFromQuantity;

        return totalMissing <= allowedMissing;
    }

    private int calculateExtraMissingFromQuantity(JSONObject recipeJson,
                                                  List<Ingredient> userIngredients,
                                                  int baseMissed,
                                                  int allowedMissing) {

        JSONArray usedIngredientsJson = recipeJson.getJSONArray("usedIngredients");
        int extraMissing = 0;

        for (int j = 0; j < usedIngredientsJson.length(); j++) {
            JSONObject usedIngredient = usedIngredientsJson.getJSONObject(j);

            String apiName = normalizeName(usedIngredient.getString("name"));
            double requiredAmount = usedIngredient.getDouble("amount");

            String unitShort = usedIngredient.optString("unitShort", "");
            String unit = usedIngredient.optString("unit", unitShort);

            Ingredient matchingUserIng = findMatchingUserIngredient(apiName, userIngredients);

            if (matchingUserIng == null) {
                // No ingredient by that name found; treat as missing
                extraMissing++;
            } else {
                // Convert user's amount into the recipe's unit and compare
                double userQuantity = matchingUserIng.getQuantity();
                double userInRecipeUnit = convertUserToRecipeUnit(userQuantity,
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
            String userName = userIng.getName().toLowerCase();
            if (userName.contains(apiName) || apiName.contains(userName)) {
                return userIng;
            }
        }
        return null;
    }


    private double convertUserToRecipeUnit(double userQuantity,
                                           String userUnit,
                                           String recipeUnit) {
        double asTbsp = unitConverter.toTbsp(userQuantity, userUnit);
        return unitConverter.fromTbsp(asTbsp, recipeUnit);
    }

    private Recipe buildFullRecipeFromBulk(JSONObject recipeJson) {
        int id = recipeJson.getInt("id");
        String title = recipeJson.optString("title", "");
        String image = recipeJson.optString("image", "");
        String mealType = extractMealType(recipeJson);

        Recipe recipe = new Recipe(id, title, image, mealType);
        recipe.setIngredients(extractIngredients(recipeJson));
        recipe.setSteps(extractSteps(recipeJson));
        addNutrition(recipe, recipeJson);

        return recipe;
    }

    private String extractMealType(JSONObject recipeJson) {
        if (!recipeJson.has("dishTypes")) {
            return "N/A";
        }

        JSONArray dishTypes = recipeJson.optJSONArray("dishTypes");
        if (dishTypes == null || dishTypes.length() == 0) {
            return "N/A";
        }

        return dishTypes.optString(0, "N/A");
    }

    private List<Ingredient> extractIngredients(JSONObject recipeJson) {
        List<Ingredient> ingredients = new ArrayList<>();

        JSONArray extIngr = recipeJson.optJSONArray("extendedIngredients");
        if (extIngr == null) {
            return ingredients;
        }

        for (int i = 0; i < extIngr.length(); i++) {
            JSONObject ingJson = extIngr.getJSONObject(i);
            String name = ingJson.optString("name", "");
            double amount = ingJson.optDouble("amount", 0.0);
            String unit = ingJson.optString("unit", "");
            ingredients.add(new Ingredient(name, amount, unit));
        }

        return ingredients;
    }

    private String extractSteps(JSONObject recipeJson) {
        StringBuilder stepsBuilder = new StringBuilder();

        JSONArray instructions = recipeJson.optJSONArray("analyzedInstructions");
        if (instructions == null || instructions.length() == 0) {
            return "";
        }

        JSONObject firstInstr = instructions.optJSONObject(0);
        if (firstInstr == null) {
            return "";
        }

        JSONArray steps = firstInstr.optJSONArray("steps");
        if (steps == null) {
            return "";
        }

        for (int i = 0; i < steps.length(); i++) {
            JSONObject stepObj = steps.getJSONObject(i);
            int number = stepObj.optInt("number", i + 1);
            String stepText = stepObj.optString("step", "");
            stepsBuilder
                    .append(number)
                    .append(". ")
                    .append(stepText)
                    .append("\n");
        }

        return stepsBuilder.toString().trim();
    }

    private void addNutrition(Recipe recipe, JSONObject recipeJson) {
        JSONObject nutrition = recipeJson.optJSONObject("nutrition");
        if (nutrition == null) {
            return;
        }

        JSONArray nutrients = nutrition.optJSONArray("nutrients");
        if (nutrients == null) {
            return;
        }

        for (int i = 0; i < nutrients.length(); i++) {
            JSONObject n = nutrients.getJSONObject(i);
            String name = n.optString("name", "");
            double amount = n.optDouble("amount", 0.0);
            String unit = n.optString("unit", "");
            if (!name.isEmpty()) {
                recipe.addNutritionalValue(name + " (" + unit + ")", amount);
            }
        }
    }
}


