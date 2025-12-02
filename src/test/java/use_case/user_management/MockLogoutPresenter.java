package use_case.user_management;

import use_case.logout.LogoutOutputBoundary;
import use_case.logout.LogoutOutputData;

/**
 * Mock implementation of the LogoutOutputBoundary for testing purposes.
 * This class captures the success result passed by the Logout interactor
 * instead of navigating to a view, allowing test assertions on the output data.
 */
public class MockLogoutPresenter implements LogoutOutputBoundary {
    private String successUsername = null;

    /**
     * Captures the successful logout output, typically containing the username of the user who logged out.
     *
     * @param outputData The data indicating successful logout, typically including the username.
     */
    @Override
    public void prepareSuccessView(LogoutOutputData outputData) {
        this.successUsername = outputData.getUsername();
    }

    /**
     * Retrieves the captured username upon successful logout.
     *
     * @return The username string, or null if prepareSuccessView was not called.
     */
    public String getSuccessUsername() {
        return successUsername;
    }
}
