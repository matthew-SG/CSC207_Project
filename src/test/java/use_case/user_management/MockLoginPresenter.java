package use_case.user_management;

import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;

/**
 * Mock implementation of the LoginOutputBoundary for testing purposes.
 * This class captures the success or failure results passed by the Login interactor
 * instead of updating a view model, allowing test assertions on the output data.
 */
public class MockLoginPresenter implements LoginOutputBoundary {
    private String failMessage = null;
    private String successUsername = null;

    /**
     * Captures the successful login output.
     *
     * @param outputData The data indicating successful login, typically including the logged-in username.
     */
    @Override
    public void prepareSuccessView(LoginOutputData outputData) {
        this.successUsername = outputData.getUsername();
    }

    /**
     * Captures the failure message from the Login interactor.
     *
     * @param error The error message detailing why the login failed.
     */
    @Override
    public void prepareFailView(String error) {
        this.failMessage = error;
    }

    /**
     * Retrieves the captured failure message.
     *
     * @return The error message string, or null if login was successful.
     */
    public String getFailMessage() {
        return failMessage;
    }

    /**
     * Retrieves the captured username upon successful login.
     *
     * @return The username string, or null if login failed.
     */
    public String getSuccessUsername() {
        return successUsername;
    }
}
