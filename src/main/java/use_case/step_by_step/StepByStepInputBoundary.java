package use_case.step_by_step;

/**
 * Input boundary for the Step-by-Step use case.
 * Defines the contract for step navigation and TTS operations.
 */
public interface StepByStepInputBoundary {

    /**
     * Executes the step navigation logic.
     * @param inputData contains the instructions and current step index
     */
    void execute(StepByStepInputData inputData);

    /**
     * Executes the text-to-speech logic for the current step.
     * @param inputData contains the instructions and current step index
     */
    void executeSpeak(StepByStepInputData inputData);
}
