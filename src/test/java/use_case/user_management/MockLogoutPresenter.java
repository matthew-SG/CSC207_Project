package use_case.user_management;

import use_case.logout.LogoutOutputBoundary;
import use_case.logout.LogoutOutputData;

public class MockLogoutPresenter implements LogoutOutputBoundary {
    private String successUsername = null;

    @Override
    public void prepareSuccessView(LogoutOutputData outputData) {
        this.successUsername = outputData.getUsername();
    }

    public String getSuccessUsername() {
        return successUsername;
    }
}