package interface_adapter.step_by_step;

import interface_adapter.ViewManagerModel;
import use_case.step_by_step.StepByStepOutputBoundary;
import use_case.step_by_step.StepByStepOutputData;

/**
 * Presenter for the Step-by-Step use case.
 * Transforms output data from the interactor into view model state.
 */
public class StepByStepPresenter implements StepByStepOutputBoundary {

    private final StepByStepViewModel viewModel;
    private final ViewManagerModel viewManagerModel;

    public StepByStepPresenter(StepByStepViewModel viewModel,
                               ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(StepByStepOutputData outputData) {
        StepByStepState state = new StepByStepState();
        state.setStepNumber(outputData.getStepNumber());
        state.setStepText(outputData.getStepText());
        state.setCanGoNext(outputData.canGoNext());
        state.setCanGoPrevious(outputData.canGoPrevious());

        viewModel.setState(state);
        viewModel.firePropertyChanged("state");
    }

    @Override
    public void prepareFailView(String errorMessage) {
        StepByStepState state = viewModel.getState();
        if (state == null) {
            state = new StepByStepState();
        }
        state.setErrorMessage(errorMessage);

        viewModel.setState(state);
        viewModel.firePropertyChanged("error");
    }

    @Override
    public void prepareSpeakFailView(String errorMessage) {
        StepByStepState state = viewModel.getState();
        if (state == null) {
            state = new StepByStepState();
        }
        state.setErrorMessage(errorMessage);
        state.setIsSpeaking(false);

        viewModel.setState(state);
        viewModel.firePropertyChanged("speakError");
    }
}