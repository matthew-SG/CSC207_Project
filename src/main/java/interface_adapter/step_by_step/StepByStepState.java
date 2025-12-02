package interface_adapter.step_by_step;

/**
 * State class representing the current state of the step-by-step navigation view.
 * Contains the current step's text, number, and navigation availability flags.
 * This state is updated by the presenter and observed by the view.
 */
public class StepByStepState {
    // The instruction text for the current step
    private String stepText;

    // The step number displayed to the user (1-based)
    private int stepNumber;

    // Flag indicating whether a next step is available
    private boolean canGoNext;

    // Flag indicating whether a previous step is available
    private boolean canGoPrevious;

    /**
     * Gets the instruction text for the current step.
     * @return the step text
     */
    public String getStepText() {
        return stepText;
    }

    /**
     * Sets the instruction text for the current step.
     * @param stepText the step text to display
     */
    public void setStepText(String stepText) {
        this.stepText = stepText;
    }

    /**
     * Gets the current step number (1-based for display).
     * @return the step number
     */
    public int getStepNumber() {
        return stepNumber;
    }

    /**
     * Sets the current step number.
     * @param stepNumber the step number (1-based)
     */
    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    /**
     * Checks if navigation to the next step is available.
     * @return true if there is a next step, false if on the last step
     */
    public boolean canGoNext() {
        return canGoNext;
    }

    /**
     * Sets whether navigation to the next step is available.
     * @param canGoNext true if a next step exists, false otherwise
     */
    public void setCanGoNext(boolean canGoNext) {
        this.canGoNext = canGoNext;
    }

    /**
     * Checks if navigation to the previous step is available.
     * @return true if there is a previous step, false if on the first step
     */
    public boolean canGoPrevious() {
        return canGoPrevious;
    }

    /**
     * Sets whether navigation to the previous step is available.
     * @param canGoPrevious true if a previous step exists, false otherwise
     */
    public void setCanGoPrevious(boolean canGoPrevious) {
        this.canGoPrevious = canGoPrevious;
    }
}
