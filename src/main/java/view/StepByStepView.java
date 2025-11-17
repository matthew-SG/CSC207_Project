package view;


import interface_adapter.login.LoginController;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;
import interface_adapter.speech.SpeechService;
import interface_adapter.step_by_step.StepByStepController;
import interface_adapter.step_by_step.StepByStepState;
import interface_adapter.step_by_step.StepByStepViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;


public class StepByStepView extends JFrame implements PropertyChangeListener{

    private final StepByStepController stepByStepController;
    private final StepByStepViewModel stepByStepViewModel;
    private final SpeechService speechService;

    private JLabel stepLabel;
    private JTextArea stepTextArea;
    private JButton prevButton;
    private JButton nextButton;
    private JButton speakButton;
    private JButton stopButton;

    private Clip audioClip;

    public StepByStepView(StepByStepController controller,
                           StepByStepViewModel viewModel,
                           SpeechService speechService) {

        this.stepByStepController = controller;
        this.stepByStepViewModel = viewModel;
        this.speechService = speechService;

        viewModel.addPropertyChangeListener(this);

        setupUI();
        setVisible(true);
    }


    private void setupUI() {
        setTitle("Step-by-Step Instructions");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        stepLabel = new JLabel("Step 1", SwingConstants.CENTER);
        stepLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(stepLabel, BorderLayout.NORTH);

        stepTextArea = new JTextArea();
        stepTextArea.setEditable(false);
        stepTextArea.setLineWrap(true);
        stepTextArea.setWrapStyleWord(true);
        add(new JScrollPane(stepTextArea), BorderLayout.CENTER);

        prevButton = new JButton("Previous Step");
        nextButton = new JButton("Next Step");
        speakButton = new JButton("Speak");
        stopButton = new JButton("Stop");

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4));
        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(speakButton);
        buttonPanel.add(stopButton);
        add(buttonPanel, BorderLayout.SOUTH);

        addListeners();
    }

    private void addListeners() {
        prevButton.addActionListener(e -> stepByStepController.previousStep());
        nextButton.addActionListener(e -> stepByStepController.nextStep());

        speakButton.addActionListener(e -> playSpeech());
        stopButton.addActionListener(e -> stopAudio());
    }

    private void playSpeech() {
        StepByStepState state = stepByStepViewModel.getState();
        if (state == null || state.getStepText() == null) return;

        new Thread(() -> {
            try {
                byte[] audioBytes = speechService.synthesize(state.getStepText());
                playAudioBytes(audioBytes);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error synthesizing speech");
            }
        }).start();
    }

    private void playAudioBytes(byte[] audioData) {
        try {
            stopAudio(); // Stop any audio already playing

            // Convert MP3 bytes to audio stream using Java Sound API (MP3 SPI required)
            AudioInputStream ais = AudioSystem.getAudioInputStream(new ByteArrayInputStream(audioData));

            audioClip = AudioSystem.getClip();
            audioClip.open(ais);
            audioClip.start();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error playing audio (MP3 SPI may be missing)");
        }
    }

    private void stopAudio() {
        if (audioClip != null && audioClip.isOpen()) {
            audioClip.stop();
            audioClip.close();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!"state".equals(evt.getPropertyName())) return;

        StepByStepState newState = (StepByStepState) evt.getNewValue();

        stepLabel.setText("Step " + newState.getStepNumber());
        stepTextArea.setText(newState.getStepText());
    }
}
