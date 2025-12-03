package use_case.step_by_step;

/**
 * Output boundary for the Step-by-Step use case.
 * Defines the contract for presenting step navigation and TTS results.
 */
public interface StepByStepOutputBoundary {

    /**
     * Prepares the success view for step navigation.
     * @param outputData the data to be presented
     */
    void prepareSuccessView(StepByStepOutputData outputData);

    /**
     * Prepares the fail view for step navigation.
     * @param errorMessage the error message to display
     */
    void prepareFailView(String errorMessage);

    /**
     * Prepares the fail view for TTS errors.
     * @param errorMessage the error message to display
     */
    void prepareSpeakFailView(String errorMessage);
}
