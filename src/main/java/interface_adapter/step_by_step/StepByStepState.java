package interface_adapter.step_by_step;

/**
 * State object for the Step-by-Step view.
 * Contains all data needed to render the current step.
 */
public class StepByStepState {
    private int stepNumber;
    private String stepText;
    private boolean canGoNext;
    private boolean canGoPrevious;
    private boolean isSpeaking;
    private String errorMessage;

    /**
     * Gets the step number.
     * @return the step number (1-indexed)
     */
    public int getStepNumber() {
        return stepNumber;
    }

    /**
     * Sets the step number.
     * @param stepNumber the step number to display (1-indexed)
     */
    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

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
     * Checks if the user can navigate to the next step.
     * @return true if there is a next step, false if on the last step
     */
    public boolean canGoNext() {
        return canGoNext;
    }

    /**
     * Sets whether the user can navigate to the next step.
     * @param canGoNext true if next step is available, false otherwise
     */
    public void setCanGoNext(boolean canGoNext) {
        this.canGoNext = canGoNext;
    }

    /**
     * Checks if the user can navigate to the previous step.
     * @return true if there is a previous step, false if on the first step
     */
    public boolean canGoPrevious() {
        return canGoPrevious;
    }

    /**
     * Sets whether the user can navigate to the previous step.
     * @param canGoPrevious true if previous step is available, false otherwise
     */
    public void setCanGoPrevious(boolean canGoPrevious) {
        this.canGoPrevious = canGoPrevious;
    }

    /**
     * Checks if text-to-speech is currently playing.
     * @return true if TTS is playing, false otherwise
     */
    public boolean isSpeaking() {
        return isSpeaking;
    }

    /**
     * Sets whether text-to-speech is currently playing.
     * @param speaking true if TTS is playing, false otherwise
     */
    public void setIsSpeaking(boolean speaking) {
        isSpeaking = speaking;
    }

    /**
     * Gets the error message if an error occurred.
     * @return the error message, or null if no error
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets the error message.
     * @param errorMessage the error message to display, or null to clear
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
