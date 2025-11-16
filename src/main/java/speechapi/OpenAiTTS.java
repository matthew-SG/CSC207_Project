package speechapi;

import interface_adapter.speech.SpeechService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.MediaType;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

public class OpenAiTTS implements SpeechService {
    private final OkHttpClient client = new OkHttpClient();
    private final String apiKey;

    public OpenAiTTS(String apiKey) {
        this.apiKey = apiKey;
    }

    public byte[] synthesize(String text) throws IOException {
        JSONObject json = new JSONObject();
        json.put("model", "gpt-4o-mini-tts");
        json.put("input", text);
        json.put("voice", "nova");
        json.put("format", "mp3");

        RequestBody body = RequestBody.create(
                json.toString(),
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/audio/speech")
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        assert response.body() != null;
        return response.body().bytes();
    }
}
