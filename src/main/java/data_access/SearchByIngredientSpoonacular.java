package data_access;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import entities.Ingredient;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.search_by_ingr.SearchByIngredientGateway;

public class SearchByIngredientSpoonacular implements SearchByIngredientGateway {
    private static final String BYINGREDIENTSURL = "https://api.spoonacular.com/recipes/findByIngredients";
    private static final String RECIPEINFOURL = "https://api.spoonacular.com/recipes/informationBulk";
    private String apiKey;

    public SearchByIngredientSpoonacular(String apiKey) {
        this.apiKey = apiKey;
    }

    public SearchByIngredientSpoonacular() {
        this.apiKey = "6e0b1d9ab8b94b9dbf723c0203286189";
    }

    @Override
    public JSONObject searchByIngredients(List<Ingredient> ingredients) {
        JSONArray recipesJSON = findResipes(ingredients);

        if (recipesJSON == null || recipesJSON.isEmpty()) {
            return null;
        }

        List<Integer> ids = new ArrayList<>();

        for (int i = 0; i < recipesJSON.length(); i++) {
            JSONObject r = recipesJSON.getJSONObject(i);
            ids.add(r.getInt("id"));
        }

        JSONArray infoJSON;
        if (ids.isEmpty()) {
            infoJSON = new JSONArray();
        } else {
            infoJSON = getInfo(ids);
        }

        JSONObject result = new JSONObject();
        result.put("findResults", recipesJSON);
        result.put("bulkResults", infoJSON);
        return result;
    }

    private JSONArray findResipes(List<Ingredient> ingredients) {
        if (ingredients.isEmpty()) {
            return null;
        }

        List<String> names = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            names.add(ingredient.getName());
        }

        String ingredientsString = String.join(",", names);
        ingredientsString = ingredientsString.replace(" ", "+");

        HttpUrl url = HttpUrl.parse(BYINGREDIENTSURL).newBuilder()
                .addQueryParameter("apiKey", apiKey)
                .addQueryParameter("ingredients", ingredientsString)
                // returns 9 results
                .addQueryParameter("number", "9")
                // "minimize missing ingredients"
                .addQueryParameter("ranking", "2")
                .addQueryParameter("ignorePantry", "true")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        final OkHttpClient client = new OkHttpClient.Builder()
                .build();

        try {
            Response response = client.newCall(request).execute();
            String rspns = response.body().string();
            if (rspns.charAt(0) == '{') {
                return null;
            }
            final JSONArray responseBody = new JSONArray(rspns);
            if (responseBody.isEmpty()) {
                throw new IOException("Empty response");
            }
            return responseBody;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public JSONArray getInfo(List<Integer> ids) {
        if (ids.isEmpty()) {
            return null;
        }

        List<String> recIds = new ArrayList<>();
        for (Integer id : ids) {
            recIds.add(id.toString());
        }

        String recipeIds = String.join(",", recIds);

        HttpUrl url = HttpUrl.parse(RECIPEINFOURL).newBuilder()
                .addQueryParameter("apiKey", apiKey)
                .addQueryParameter("ids", recipeIds)
                .addQueryParameter("includeNutrition", "true")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        final OkHttpClient client = new OkHttpClient.Builder()
                .build();

        try {
            Response response = client.newCall(request).execute();
            String rspns = response.body().string();
            final JSONArray responseBody = new JSONArray(rspns);
            if (responseBody.length() == 0) {
                throw new IOException("Empty response");
            }
            return responseBody;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
