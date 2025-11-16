package interface_adapter.speech;

import java.io.IOException;

public interface SpeechService {
    byte[] synthesize(String text) throws IOException;
}
