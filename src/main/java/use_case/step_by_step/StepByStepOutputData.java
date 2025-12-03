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

    /**
     * Constructs a new StepByStepOutputData.
     * @param stepNumber the step number to display (e.g., 1, 2, 3...)
     * @param stepText the instruction text for this step
     * @param canGoPrevious whether the user can navigate to the previous step
     * @param canGoNext whether the user can navigate to the next step
     */
    public StepByStepOutputData(int stepNumber, String stepText,
                                boolean canGoPrevious, boolean canGoNext) {
        this.stepNumber = stepNumber;
        this.stepText = stepText;
        this.canGoPrevious = canGoPrevious;
        this.canGoNext = canGoNext;
    }

    /**
     * Gets the step number.
     * @return the step number (1-indexed)
     */
    public int getStepNumber() {
        return stepNumber;
    }

    /**
     * Gets the instruction text for this step.
     * @return the step text
     */
    public String getStepText() {
        return stepText;
    }

    /**
     * Checks if the user can go to the previous step.
     * @return true if there is a previous step, false otherwise
     */
    public boolean canGoPrevious() {
        return canGoPrevious;
    }

    /**
     * Checks if the user can go to the next step.
     * @return true if there is a next step, false otherwise
     */
    public boolean canGoNext() {
        return canGoNext;
    }
}
