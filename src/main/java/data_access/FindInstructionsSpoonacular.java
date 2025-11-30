package data_access;

import entities.InstructionStep;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FindInstructionsSpoonacular {
    private static final String INSTRUCTIONS_URL = "https://api.spoonacular.com/recipes/";

    public List<InstructionStep> getAnalyzedInstructions(int recipeId, String apiKey) {
        List<InstructionStep> steps = new ArrayList<>();

        String urlString = INSTRUCTIONS_URL + recipeId + "/analyzedInstructions";
        HttpUrl url = Objects.requireNonNull(HttpUrl.parse(urlString)).newBuilder()
                .addQueryParameter("apiKey", apiKey)
                .build();

        OkHttpClient client = new OkHttpClient.Builder().build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            assert response.body() != null;
            String bodyString = response.body().string();
            JSONArray root = new JSONArray(bodyString);

            if (root.isEmpty()) {
                // No instructions available
                steps.add(new InstructionStep(0, "No instructions found for this recipe"));
                return steps;
            }

            JSONObject instructionBlock = root.getJSONObject(0);
            JSONArray stepArray = instructionBlock.optJSONArray("steps");

            if (stepArray == null || stepArray.isEmpty()) {
                steps.add(new  InstructionStep(0, "No instructions found for this recipe"));
                return steps;
            }

            for (int i = 0; i < stepArray.length(); i++) {
                JSONObject stepObj = stepArray.getJSONObject(i);
                int number = stepObj.optInt("number", i + 1);
                String stepText = stepObj.optString("step", "").trim();

                if (!stepText.isEmpty()) {
                    steps.add(new InstructionStep(number, stepText));
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Error fetching the instructions", e);
        }

        return steps;
    }
}
