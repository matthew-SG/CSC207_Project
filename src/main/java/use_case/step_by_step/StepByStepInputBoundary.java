package use_case.step_by_step;

/**
 * Input Boundary for actions related to step-by-step instructions.
 */
public interface StepByStepInputBoundary {
    /**
     * Prepares the success view for the step_by_step use case.
     * @param Recipe the input data for this use case
     */
    void execute(StepByStepInputData Recipe);

    /**
     * Switches to the next step in the recipe.
     */
    void nextStep();

    /**
     * Switches to the previous step in the recipe.
     */
    void previousStep();
}
