package API;

import entities.Recipe;
import entities.Ingredient;
import okhttp3.*;
import org.json.*;
import java.io.IOException;
import java.util.*;

public class SearchByIngredientSpoonacular {
    private static final String BYINGREDIENTSURL = "https://api.spoonacular.com/recipes/findByIngredients";
    private static final String RECIPEINFOURL = "https://api.spoonacular.com/recipes/";
    private String apiKey;

    public SearchByIngredientSpoonacular(String apiKey) {
        this.apiKey = apiKey;
    }

    public List<Recipe> searchByIngredientSpoonacular(List<String> ingredients) {
        List<Recipe> result = new ArrayList<>();
        if (ingredients.isEmpty()) {
            return result;
        }
        String ingredientsString = String.join(",", ingredients);
        ingredientsString = ingredientsString.replace(" ", "+");
        HttpUrl url = HttpUrl.parse(BYINGREDIENTSURL).newBuilder().
                addQueryParameter("apiKey", apiKey).
                addQueryParameter("ingredients", ingredientsString).
                //returns 15 results
                addQueryParameter("number", "15").
                //"minimize missing ingredients"
                addQueryParameter("ranking", "2").
                addQueryParameter("ignorePantry", "true").
                build();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        final OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        try {
            Response response = client.newCall(request).execute();
           String rspns = response.body().string();
            final JSONArray responseBody = new JSONArray(rspns);
            if (responseBody.length() == 0) throw new IOException("Empty response");
            for (int i = 0; i < responseBody.length() && result.size() < 5; i++) {
                JSONObject object = responseBody.getJSONObject(i);
                int missedIngredients = object.getInt("missedIngredientCount");
                if (missedIngredients == 0) {
                    Recipe recipe = new Recipe(
                            object.getInt("id"),
                            object.getString("title"),
                            object.getString("image"),
                            "N/A");
                    result.add(recipe);
                }
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //The api only allows 2 calls per second, so we cant populate the recipe with its steps and ingredient in the same method
    public void populateRecipeDetails(Recipe recipe) {
        if (recipe.getIngredients() != null && !recipe.getIngredients().isEmpty()
                && recipe.getSteps() != null && !recipe.getSteps().isEmpty()) {
            return;
    }
        String recipeInfoUrl = RECIPEINFOURL+recipe.getRecipeId()+"/information";
        HttpUrl url = HttpUrl.parse(recipeInfoUrl).newBuilder().addQueryParameter("apiKey", apiKey).build();
        final OkHttpClient client = new OkHttpClient.Builder().build();
        Request request = new Request.Builder().url(url).get().build();
        try{
            Response response = client.newCall(request).execute();
            final JSONObject responseBody = new JSONObject(response.body().string());
            if (responseBody.isEmpty()) throw new IOException("Empty response");
            //setting the ingredients
            JSONArray extendedIngredients = responseBody.optJSONArray("extendedIngredients");
            List<Ingredient> ingredients = new ArrayList<>();
            for(int i=0;extendedIngredients != null&&i<extendedIngredients.length();i++){
                JSONObject ingredient = extendedIngredients.getJSONObject(i);
                String ingredientName = ingredient.getString("name");
                double ingredientQuantity = ingredient.getDouble("amount");
                String unit = ingredient.getString("unit");
                ingredients.add(new Ingredient(ingredientName, ingredientQuantity, unit));
            }
            recipe.setIngredients(ingredients);
            //setting the steps
            String instructions = responseBody.optString("instructions", null);
            String stepText="";
            if (instructions!=null&& !instructions.isEmpty())
                stepText = instructions.trim();
            else
                stepText = "No instructions provided";
            recipe.setSteps(stepText);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
