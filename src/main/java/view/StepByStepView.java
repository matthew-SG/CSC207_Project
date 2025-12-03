package view;

import entities.InstructionStep;
import entities.RecipeInstructions;
import interface_adapter.ViewManagerModel;
import interface_adapter.speech.SpeechService;
import interface_adapter.step_by_step.*;
import use_case.step_by_step.StepByStepInteractor;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class StepByStepView extends JFrame implements PropertyChangeListener {

    private final StepByStepController stepByStepController;
    private final StepByStepViewModel stepByStepViewModel;

    private ImageIcon scaledIcon;
    private Image scaled;
    private Image resized;
    private JLabel imageLabel;
    private ImageIcon chefIcon;
    private JLabel stepLabel;
    private JTextArea stepTextArea;
    private JButton prevButton;
    private JButton nextButton;
    private JButton speakButton;

    public StepByStepView(StepByStepController controller,
                          StepByStepViewModel viewModel) {

        this.stepByStepController = controller;
        this.stepByStepViewModel = viewModel;

        // Register this view as a property change listener
        viewModel.addPropertyChangeListener(this);

        setupUI();
    }

    private void setupUI() {
        setTitle("Step-by-Step Instructions");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        stepLabel = new JLabel("Step 1");

        stepTextArea = new JTextArea();
        stepTextArea.setEditable(false);
        stepTextArea.setLineWrap(true);
        stepTextArea.setWrapStyleWord(true);

        chefIcon = new ImageIcon("src/main/java/view/chef-cooking-kitchen-while-wearing-professional-attire.jpg");
        resized = chefIcon.getImage();
        int newWidth = 200;
        int newHeight = 140;
        scaled = resized.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        scaledIcon = new ImageIcon(scaled);
        imageLabel = new JLabel(scaledIcon);

        JPanel stepButtonPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.1;
        stepButtonPanel.add(imageLabel, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        stepButtonPanel.add(stepLabel, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        stepButtonPanel.add(stepTextArea, gbc);

        add(stepButtonPanel, BorderLayout.CENTER);

        prevButton = new JButton("Previous Step");
        nextButton = new JButton("Next Step");
        speakButton = new JButton("Speak");

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(speakButton);
        add(buttonPanel, BorderLayout.SOUTH);

        addListeners();
    }

    private void addListeners() {
        prevButton.addActionListener(e -> stepByStepController.previousStep());
        nextButton.addActionListener(e -> stepByStepController.nextStep());

        // Now following Clean Architecture pattern
        speakButton.addActionListener(e -> stepByStepController.speak());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();

        if ("state".equals(propertyName)) {
            StepByStepState newState = (StepByStepState) evt.getNewValue();
            updateStepDisplay(newState);
        }
        else if ("error".equals(propertyName) || "speakError".equals(propertyName)) {
            StepByStepState newState = (StepByStepState) evt.getNewValue();
            handleError(newState);
        }
    }

    private void updateStepDisplay(StepByStepState state) {
        stepLabel.setText("Step " + state.getStepNumber());
        stepTextArea.setText(state.getStepText());
        nextButton.setEnabled(state.canGoNext());
        prevButton.setEnabled(state.canGoPrevious());
    }

    private void handleError(StepByStepState state) {
        if (state.getErrorMessage() != null) {
            JOptionPane.showMessageDialog(this,
                    state.getErrorMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}