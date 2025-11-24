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
        showCurrentStep();
    }

    /**
     * Move to the next step, if possible.
     */
    @Override
    public void nextStep() {
        if (steps == null || currentIndex >= steps.size() - 1) return;
        currentIndex++;
        showCurrentStep();
    }

    /**
     * Move to the previous step, if possible.
     */
    @Override
    public void previousStep() {
        if (steps == null || currentIndex <= 0) return;
        currentIndex--;
        showCurrentStep();
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
                currentStep.getNumber(),
                hasNext,
                hasPrev
        );

        presenter.present(outputData);
    }
}
