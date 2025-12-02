package interface_adapter.step_by_step;

import interface_adapter.ViewManagerModel;
import use_case.step_by_step.StepByStepOutputBoundary;
import use_case.step_by_step.StepByStepOutputData;

public class StepByStepPresenter implements StepByStepOutputBoundary {
    private final StepByStepViewModel viewModel;
    private final ViewManagerModel viewManagerModel;

    public StepByStepPresenter(StepByStepViewModel viewModel, ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void present(StepByStepOutputData outputData) {

        final StepByStepState newState = new StepByStepState();
        newState.setStepText(outputData.stepText());
        newState.setStepNumber(outputData.stepNumber());
        newState.setCanGoNext(outputData.hasNext());
        newState.setCanGoPrevious(outputData.hasPrevious());

        viewModel.setState(newState);
    }

    @Override
    public void prepareFailView(String error) {
        this.viewManagerModel.showsErrorMessage(error);
    }
}
