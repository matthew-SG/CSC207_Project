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

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public String getStepText() {
        return stepText;
    }

    public void setStepText(String stepText) {
        this.stepText = stepText;
    }

    public boolean canGoNext() {
        return canGoNext;
    }

    public void setCanGoNext(boolean canGoNext) {
        this.canGoNext = canGoNext;
    }

    public boolean canGoPrevious() {
        return canGoPrevious;
    }

    public void setCanGoPrevious(boolean canGoPrevious) {
        this.canGoPrevious = canGoPrevious;
    }

    public boolean isSpeaking() {
        return isSpeaking;
    }

    public void setIsSpeaking(boolean speaking) {
        isSpeaking = speaking;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}