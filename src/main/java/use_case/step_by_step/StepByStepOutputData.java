package use_case.step_by_step;

/**
 * Output data for the Step-by-Step use case.
 * Contains the data that the interactor passes to the presenter.
 */
public class StepByStepOutputData {
    private final int stepNumber;
    private final String stepText;
    private final boolean canGoPrevious;
    private final boolean canGoNext;

    public StepByStepOutputData(int stepNumber, String stepText,
                                boolean canGoPrevious, boolean canGoNext) {
        this.stepNumber = stepNumber;
        this.stepText = stepText;
        this.canGoPrevious = canGoPrevious;
        this.canGoNext = canGoNext;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public String getStepText() {
        return stepText;
    }

    public boolean canGoPrevious() {
        return canGoPrevious;
    }

    public boolean canGoNext() {
        return canGoNext;
    }
}