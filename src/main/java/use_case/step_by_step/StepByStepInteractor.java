package use_case.step_by_step;

public class StepByStepInteractor implements StepByStepInputBoundary{
    private final StepByStepOutputBoundary presenter;

    public StepByStepInteractor(StepByStepOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute(StepByStepInputData inputData) {
        var steps = inputData.instructions().steps();
        int index = inputData.currentStepIndex();

        var currentStep = steps.get(index);

        boolean hasNext = index < steps.size() - 1;
        boolean hasPrev = index > 0;

        StepByStepOutputData outputData = new StepByStepOutputData(
                currentStep.getStep(),
                currentStep.getNumber(),
                hasNext,
                hasPrev
        );

        presenter.present(outputData);
    }
}
