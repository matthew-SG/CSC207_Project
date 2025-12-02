package use_case.user_management;

import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;

public class MockLoginPresenter implements LoginOutputBoundary {
    private String failMessage = null;
    private String successUsername = null;

    @Override
    public void prepareSuccessView(LoginOutputData outputData) {
        this.successUsername = outputData.getUsername();
    }

    @Override
    public void prepareFailView(String error) {
        this.failMessage = error;
    }
    public String getFailMessage() {
        return failMessage;
    }
    public String getSuccessUsername() {
        return successUsername;
    }
}