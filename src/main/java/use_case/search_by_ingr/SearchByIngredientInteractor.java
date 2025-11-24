package use_case.search_by_ingr;

import API.SearchByIngredientSpoonacular;
import entities.Ingredient;
import entities.Recipe;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.*;

import java.util.*;

public class SearchByIngredientInteractor implements SearchByIngredientInputBoundary{
    private final SearchByIngredientSpoonacular api;
    public SearchByIngredientInteractor(SearchByIngredientSpoonacular api) {
        this.api = api;
    }

    @Override
    public SearchByIngredientOutputData execute(SearchByIngredientInputData inputData) {
        List<Ingredient> ingredients = inputData.getIngredients();
        if(ingredients.isEmpty()){return new SearchByIngredientOutputData(List.of(),"Enter at least one ingredient");}
        JSONArray results= api.searchByIngredientSpoonacular(ingredients);
        ArrayList<Recipe> recipes=new ArrayList<>();

        System.out.println(results.toString());

        for (int i = 0; i < results.length() && recipes.size() < 5; i++) {
            JSONObject object = results.getJSONObject(i);
            int missedIngredients = object.getInt("missedIngredientCount");
            if (missedIngredients == 0) {
                JSONArray usedIngredients = object.getJSONArray("usedIngredients");
                boolean haveEnough = true;
                for (int j = 0; j < usedIngredients.length()&&haveEnough; j++) {
                    JSONObject usedIngredient = usedIngredients.getJSONObject(j);
                    double amount = usedIngredient.getDouble("amount");
                    String name = usedIngredient.getString("name").toLowerCase();
                    String[] unitName = new String[3];
                    unitName[0] = usedIngredient.getString("unit");
                    unitName[1] = usedIngredient.getString("unitShort");
                    unitName[2] = usedIngredient.getString("unitLong");
                    for (Ingredient ingredient : ingredients) {
                        if (ingredient.getName().toLowerCase().contains(name)) {
                            if (ingredient.getQuantity() < amount &&
                                            (ingredient.getUnit().equals(unitName[0])
                                            ||ingredient.getUnit().equals(unitName[1])
                                            ||ingredient.getUnit().equals(unitName[2])) ) {
                                haveEnough = false;
                                break;
                            }
                        }
                    }
                }
                if (haveEnough) {
                    Recipe recipe = new Recipe(
                            object.getInt("id"),
                            object.getString("title"),
                            object.getString("image"),
                            "N/A");
                    recipes.add(recipe);
                }
            }
        }


        String msg;
        if(recipes.isEmpty()) msg="no perfect match found";
        else msg="found";
        return new SearchByIngredientOutputData(recipes,msg);
    }
}

/*for (int i = 0; i < results.length() && result.size() < 5; i++) {
JSONObject object = results.getJSONObject(i);
int missedIngredients = object.getInt("missedIngredientCount");
                if (missedIngredients == 0) {
JSONArray usedIngredients = object.getJSONArray("usedIngredients");
boolean haveEnough = true;
                    for (int j = 0; j < usedIngredients.length()&&haveEnough; j++) {
JSONObject usedIngredient = usedIngredients.getJSONObject(j);
double amount = usedIngredient.getDouble("amount");
String name = usedIngredient.getString("name").toLowerCase();
                        for (Ingredient ingredient : ingredients) {
        if (ingredient.getName().toLowerCase().contains(name)) {
        if (ingredient.getQuantity() < amount) {
haveEnough = false;
        break;
        }
        }
        }
        }
        if (haveEnough) {
Recipe recipe = new Recipe(
        object.getInt("id"),
        object.getString("title"),
        object.getString("image"),
        "N/A");
                        result.add(recipe);
                    }
                            }
                            }
                            return result;*/