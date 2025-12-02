package data_access;

import interface_adapter.speech.SpeechService;

public class SystemTTS implements SpeechService {

    @Override
    public void synthesize(String text) {
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
                System.err.println("TTS not supported on this OS: " + os);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
