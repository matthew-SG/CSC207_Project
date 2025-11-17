package interface_adapter.step_by_step;

import use_case.step_by_step.StepByStepOutputBoundary;
import use_case.step_by_step.StepByStepOutputData;

public class StepByStepPresenter implements StepByStepOutputBoundary {
    private final StepByStepViewModel viewModel;

    public StepByStepPresenter(StepByStepViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(StepByStepOutputData outputData) {

        StepByStepState newState = new StepByStepState();
        newState.setStepText(outputData.stepText());
        newState.setStepNumber(outputData.stepNumber());
        newState.setCanGoNext(outputData.hasNext());
        newState.setCanGoPrevious(outputData.hasPrevious());

        viewModel.setState(newState);
    }
}
