package data_access;

import interface_adapter.speech.SpeechService;

/**
 * System text-to-speech implementation using native OS TTS engines.
 * Supports macOS (say), Windows (PowerShell SAPI), and Linux (espeak).
 */
public class SystemTTS implements SpeechService {

    @Override
    public void synthesize(String text) throws Exception {
        if (text == null || text.trim().isEmpty()) {
            throw new Exception("No text to synthesize");
        }

        String os = System.getProperty("os.name").toLowerCase();
        Process process;

        try {
            if (os.contains("mac")) {
                // macOS
                process = new ProcessBuilder("say", text).start();
            } else if (os.contains("win")) {
                // Windows (PowerShell + SAPI)
                String cmd = "Add-Type –AssemblyName System.Speech; " +
                        "$speak = New-Object System.Speech.Synthesis.SpeechSynthesizer; " +
                        "$speak.Speak('" + text.replace("'", "''") + "')";
                process = new ProcessBuilder("powershell", "-Command", cmd).start();
            } else if (os.contains("nux") || os.contains("nix")) {
                // Linux, requires espeak installed
                process = new ProcessBuilder("espeak", text).start();
            } else {
                throw new Exception("TTS not supported on this OS: " + os);
            }

            // Optional: wait for process to complete
            process.waitFor();

        } catch (Exception e) {
            throw new Exception("TTS error: " + e.getMessage(), e);
        }
    }
}