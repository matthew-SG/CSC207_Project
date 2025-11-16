package interface_adapter.step_by_step;

public class StepByStepState {
    private String stepText;
    private int stepNumber;
    private boolean canGoNext;
    private boolean canGoPrevious;

    public String getStepText() {
        return stepText;
    }

    public void setStepText(String stepText) {
        this.stepText = stepText;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
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
}
