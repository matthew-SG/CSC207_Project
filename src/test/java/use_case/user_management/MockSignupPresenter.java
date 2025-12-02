package use_case.user_management;

import use_case.signup.SignupOutputBoundary;
import use_case.signup.SignupOutputData;

public class MockSignupPresenter implements SignupOutputBoundary {
    private String failMessage = null;
    private String successUsername = null;
    private boolean loginViewSwitched = false;

    @Override
    public void prepareSuccessView(SignupOutputData outputData) {
        this.successUsername = outputData.getUsername();
    }

    @Override
    public void prepareFailView(String error) {
        this.failMessage = error;
    }

    @Override
    public void switchToLoginView() {
        this.loginViewSwitched = true;
    }

    public String getFailMessage() {
        return failMessage;
    }

    public String getSuccessUsername() {
        return successUsername;
    }

    public boolean isLoginViewSwitched() {
        return loginViewSwitched;
    }
}
