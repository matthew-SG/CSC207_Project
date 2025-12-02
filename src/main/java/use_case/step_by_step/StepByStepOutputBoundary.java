package use_case.step_by_step;

/**
 * Output Boundary for actions related to step-by-step instructions.
 */

public interface StepByStepOutputBoundary {
    /**
     * Executes the step_by_step use case.
     * @param outputData output data for this use case
     */
    void present(StepByStepOutputData outputData);

    /**
     * Prepares the fail view for the use case.
     * @param error contains the error message to display.
     */
    void prepareFailView(String error);
}
