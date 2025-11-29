package use_case.search_by_ingr;

import data_access.SearchByIngredientSpoonacular;
import entities.Ingredient;
import entities.Recipe;
import entities.unitConverter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchByIngredientInteractor implements SearchByIngredientInputBoundary {

    private static final int MAX_RECIPES = 10;
    private final SearchByIngredientSpoonacular api;
    private final data_access.FileDataAccessObject approveRecipeDAO;

    public SearchByIngredientInteractor(SearchByIngredientSpoonacular api, 
                                        data_access.FileDataAccessObject approveRecipeDAO) {
        this.api = api;
        this.approveRecipeDAO = approveRecipeDAO;
    }

    @Override
    public SearchByIngredientOutputData execute(SearchByIngredientInputData inputData) {
        List<Ingredient> ingredients = inputData.getIngredients();

        if (ingredients == null || ingredients.isEmpty()) {
            return new SearchByIngredientOutputData(
                    List.of(),
                    "Enter at least one ingredient."
            );
        }

        // Business rule: missing amount cannot be negative.
        int allowedMissing = Math.max(0, inputData.getAmountMissing());

        JSONArray results = api.searchByIngredientSpoonacular(ingredients);
        List<Recipe> recipes = new ArrayList<>();

        for (int i = 0; i < results.length() && recipes.size() < MAX_RECIPES; i++) {
            JSONObject recipeJson = results.getJSONObject(i);

            if (shouldIncludeRecipe(recipeJson, ingredients, allowedMissing)) {
                recipes.add(buildRecipe(recipeJson));
            }
        }

        // Make recipes available for approval
        if (approveRecipeDAO != null) {
            approveRecipeDAO.setAvailableRecipes(recipes);
        }

        String msg = recipes.isEmpty()
                ? "No recipes found within the allowed missing ingredients."
                : "Found " + recipes.size() + " recipes.";

        return new SearchByIngredientOutputData(recipes, msg);
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
        // toTbsp + fromTbsp is your existing conversion API
        double asTbsp = unitConverter.toTbsp(userQuantity, userUnit);
        return unitConverter.fromTbsp(asTbsp, recipeUnit);
    }

    private Recipe buildRecipe(JSONObject recipeJson) {
        return new Recipe(
                recipeJson.getInt("id"),
                recipeJson.getString("title"),
                recipeJson.getString("image"),
                "N/A"
        );
    }
}
