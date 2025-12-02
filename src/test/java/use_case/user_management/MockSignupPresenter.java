package use_case.user_management;

import use_case.signup.SignupOutputBoundary;
import use_case.signup.SignupOutputData;

/**
 * Mock implementation of the SignupOutputBoundary for testing purposes.
 * This class captures the success or failure results passed by the Signup interactor
 * and records attempts to switch views, allowing test assertions on the output behavior.
 */
public class MockSignupPresenter implements SignupOutputBoundary {
    private String failMessage = null;
    private String successUsername = null;
    private boolean loginViewSwitched = false;

    /**
     * Captures the successful signup output.
     *
     * @param outputData The data indicating successful signup, typically including the new username.
     */
    @Override
    public void prepareSuccessView(SignupOutputData outputData) {
        this.successUsername = outputData.getUsername();
    }

    /**
     * Captures the failure message from the Signup interactor.
     *
     * @param error The error message detailing why the signup failed.
     */
    @Override
    public void prepareFailView(String error) {
        this.failMessage = error;
    }

    /**
     * Records the call to switch the view to the login screen.
     */
    @Override
    public void switchToLoginView() {
        this.loginViewSwitched = true;
    }

    /**
     * Retrieves the captured failure message.
     *
     * @return The error message string, or null if signup was successful.
     */
    public String getFailMessage() {
        return failMessage;
    }

    /**
     * Retrieves the captured username upon successful signup.
     *
     * @return The username string, or null if signup failed.
     */
    public String getSuccessUsername() {
        return successUsername;
    }

    /**
     * Checks if the presenter was instructed to switch to the login view.
     *
     * @return true if switchToLoginView was called, false otherwise.
     */
    public boolean isLoginViewSwitched() {
        return loginViewSwitched;
    }
}
