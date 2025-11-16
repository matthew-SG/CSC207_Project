package API;

import entities.Recipe;
import okhttp3.*;
import org.json.*;
import java.io.IOException;
import java.util.*;

public class SearchByIngredientSpoonacular {
    private static final String URL = "https://api.spoonacular.com/recipes/findByIngredients";
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
        HttpUrl url = HttpUrl.parse(URL).newBuilder().
                addQueryParameter("apikey", apiKey).
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
            final JSONArray responseBody = new JSONArray(response.body().string());
            if (responseBody.isEmpty()) throw new IOException("Empty response");
            for (int i = 0; i < responseBody.length() && result.size() < 5; i++) {
                JSONObject object = responseBody.getJSONObject(i);
                int missedIngredients = object.getInt("missedIngredientCount");
                if (missedIngredients == 0) {
                    Recipe recipe = new Recipe(
                            object.getInt("id"),
                            object.getString("title"),
                            object.getString("image"),

                }
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
