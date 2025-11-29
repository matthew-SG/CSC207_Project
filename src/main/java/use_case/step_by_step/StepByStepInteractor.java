package use_case.step_by_step;

import entities.InstructionStep;
import java.util.List;

/**
 * Interactor for StepByStep use case, implementing navigation.
 */
public class StepByStepInteractor implements StepByStepInputBoundary {
    private final StepByStepOutputBoundary presenter;
    private List<InstructionStep> steps;
    private int currentIndex = 0;

    public StepByStepInteractor(StepByStepOutputBoundary presenter) {
        this.presenter = presenter;
    }

    /**
     * Initializes the steps and current index.
     */
    @Override
    public void execute(StepByStepInputData inputData) {
        this.steps = inputData.instructions().steps();
        this.currentIndex = inputData.currentStepIndex();

        if (steps == null || steps.isEmpty()) {
            presenter.prepareFailView("There are no steps");
            return;
        }

        if (currentIndex < 0 || currentIndex >= steps.size()) {
            presenter.prepareFailView("Invalid step index");
            return;
        }

        showCurrentStep();
    }

    /**
     * Move to the next step, if possible.
     */
    @Override
    public void nextStep() {
        if (currentIndex < steps.size() - 1) {
            currentIndex++;
            showCurrentStep();
        }
    }

    /**
     * Move to the previous step, if possible.
     */
    @Override
    public void previousStep() {
        if (currentIndex > 0) {
            currentIndex--;
            showCurrentStep();
        }
    }

    /**
     * Helper method to present the current step via the presenter.
     */
    private void showCurrentStep() {
        InstructionStep currentStep = steps.get(currentIndex);
        boolean hasNext = currentIndex < steps.size() - 1;
        boolean hasPrev = currentIndex > 0;

        StepByStepOutputData outputData = new StepByStepOutputData(
                currentStep.getStep(),
                currentIndex + 1,
                hasNext,
                hasPrev
        );

        presenter.present(outputData);
    }
}