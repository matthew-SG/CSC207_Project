package interface_adapter.speech;

/**
 * Interface for text-to-speech services.
 * Allows different TTS implementations to be used interchangeably.
 */
public interface SpeechService {

    /**
     * Converts the given text to speech and plays it.
     * @param text the text to synthesize
     * @throws Exception if TTS fails
     */
    void synthesize(String text) throws Exception;
}
