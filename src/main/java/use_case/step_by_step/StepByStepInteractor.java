package use_case.step_by_step;

import entities.InstructionStep;
import entities.RecipeInstructions;
import interface_adapter.speech.SpeechService;

import java.util.List;

/**
 * Interactor for the Step-by-Step use case.
 * Handles the business logic for navigating through recipe steps and TTS.
 */
public class StepByStepInteractor implements StepByStepInputBoundary {

    private final StepByStepOutputBoundary presenter;
    private final SpeechService speechService;

    /**
     * Constructs a new StepByStepInteractor.
     * @param presenter the output boundary for presenting results
     * @param speechService the service for text-to-speech functionality
     */
    public StepByStepInteractor(StepByStepOutputBoundary presenter,
                                SpeechService speechService) {
        this.presenter = presenter;
        this.speechService = speechService;
    }

    @Override
    public void execute(StepByStepInputData inputData) {
        RecipeInstructions instructions = inputData.getInstructions();
        int currentIndex = inputData.getCurrentStepIndex();
        List<InstructionStep> steps = instructions.steps();

        // Validate index
        if (currentIndex < 0 || currentIndex >= steps.size()) {
            presenter.prepareFailView("Invalid step index");
            return;
        }

        // Get current step
        InstructionStep currentStep = steps.get(currentIndex);

        // Prepare output data
        StepByStepOutputData outputData = new StepByStepOutputData(
                currentStep.getNumber(),
                currentStep.getStep(),
                currentIndex > 0,  // can go previous
                currentIndex < steps.size() - 1  // can go next
        );

        presenter.prepareSuccessView(outputData);
    }

    @Override
    public void executeSpeak(StepByStepInputData inputData) {
        RecipeInstructions instructions = inputData.getInstructions();
        int currentIndex = inputData.getCurrentStepIndex();
        List<InstructionStep> steps = instructions.steps();

        // Validate index
        if (currentIndex < 0 || currentIndex >= steps.size()) {
            presenter.prepareSpeakFailView("Invalid step index for TTS");
            return;
        }

        // Get current step text
        InstructionStep currentStep = steps.get(currentIndex);
        String stepText = currentStep.getStep();

        // Execute TTS in background thread
        new Thread(() -> {
            try {
                speechService.synthesize(stepText);
                // TTS completed successfully - no need to notify presenter
                // User can click speak again immediately
            } catch (Exception e) {
                presenter.prepareSpeakFailView("TTS error: " + e.getMessage());
            }
        }).start();
    }
}