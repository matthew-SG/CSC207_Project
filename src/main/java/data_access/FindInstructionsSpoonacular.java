package data_access;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONObject;

import entities.InstructionStep;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Data access class for fetching recipe instructions from the Spoonacular API.
 * Retrieves analyzed instructions and parses them into InstructionStep objects,
 * splitting long steps into individual sentences for better readability.
 */
public class FindInstructionsSpoonacular {
    // Base URL for the Spoonacular recipe instructions endpoint
    private static final String INSTRUCTIONS_URL = "https://api.spoonacular.com/recipes/";

    /**
     * Fetches and parses analyzed instructions for a given recipe from Spoonacular API.
     * Long instruction steps are automatically split into individual sentences.
     * @param recipeId the unique identifier for the recipe
     * @param apiKey the Spoonacular API key for authentication
     * @return a list of instruction steps, or a single-item list with an error message if no steps found
     * @throws RuntimeException if the API call fails or returns an error
     */
    public List<InstructionStep> getAnalyzedInstructions(int recipeId, String apiKey) {
        final List<InstructionStep> steps = new ArrayList<>();

        // Build the API URL with recipe ID and API key
        final String urlString = INSTRUCTIONS_URL + recipeId + "/analyzedInstructions";
        final HttpUrl url = Objects.requireNonNull(HttpUrl.parse(urlString)).newBuilder()
                .addQueryParameter("apiKey", apiKey)
                .build();

        // Create HTTP client and request
        final OkHttpClient client = new OkHttpClient.Builder().build();
        final Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try {
            // Execute the API request
            final Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            // Parse the JSON response
            assert response.body() != null;
            final String bodyString = response.body().string();
            final JSONArray root = new JSONArray(bodyString);

            // Check if the response contains any instruction blocks
            if (root.isEmpty()) {
                steps.add(new InstructionStep(0, "No instructions found for this recipe"));
                return steps;
            }

            // Get the first instruction block (main recipe steps)
            final JSONObject instructionBlock = root.getJSONObject(0);
            final JSONArray stepArray = instructionBlock.optJSONArray("steps");

            // Validate that steps exist in the instruction block
            if (stepArray == null || stepArray.isEmpty()) {
                steps.add(new InstructionStep(0, "No instructions found for this recipe"));
                return steps;
            }

            // Parse each step and split long steps into sentences
            int stepNumber = 1;
            for (int i = 0; i < stepArray.length(); i++) {
                final JSONObject stepObj = stepArray.getJSONObject(i);
                final String stepText = stepObj.optString("step", "").trim();

                if (!stepText.isEmpty()) {
                    // Split by sentence boundaries: period/!/? followed by optional space and capital letter
                    // This handles cases like "bath.Slowly" where punctuation is missing a space
                    final String[] sentences = stepText.split("(?<=[.!?])\\s*(?=[A-Z])");

                    // Add each sentence as a separate step for better readability
                    for (String sentence : sentences) {
                        final String trimmed = sentence.trim();
                        if (!trimmed.isEmpty()) {
                            steps.add(new InstructionStep(stepNumber++, trimmed));
                        }
                    }
                }
            }

        }
        catch (IOException ioEx) {
            throw new RuntimeException("Error fetching the instructions", ioEx);
        }

        return steps;
    }
}
