package use_case.user_management;

import data_access.InMemoryUserDataAccessObject;
import org.junit.jupiter.api.Test;

import use_case.login.LoginInputData;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;

import static org.junit.jupiter.api.Assertions.*;


class MockLoginPresenter implements LoginOutputBoundary {
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

    public String getFailMessage() { return failMessage; }
    public String getSuccessUsername() { return successUsername; }
}


class LoginInteractorTest {

    private static final String EXISTING_USERNAME = "test_1";
    private static final String CORRECT_PASSWORD = "password";
    private static final String DNE_USERNAME = "nonExistentUser";
    private static final String WRONG_PASSWORD = "wrongPassword";

    @Test
    void testLoginSuccess() {
        final InMemoryUserDataAccessObject dao = new InMemoryUserDataAccessObject();
        final MockLoginPresenter presenter = new MockLoginPresenter();

        final LoginInteractor interactor = new LoginInteractor(dao, presenter);

        final LoginInputData inputData = new LoginInputData(EXISTING_USERNAME, CORRECT_PASSWORD);

        interactor.execute(inputData);

        assertNotNull(presenter.getSuccessUsername(), "Success view should be called.");
        assertEquals(EXISTING_USERNAME, presenter.getSuccessUsername());
        assertNull(presenter.getFailMessage(), "Fail view should not be called.");
    }

    @Test
    void testLoginFailure_UserDNE() {
        final InMemoryUserDataAccessObject dao = new InMemoryUserDataAccessObject();
        final MockLoginPresenter presenter = new MockLoginPresenter();
        final LoginInteractor interactor = new LoginInteractor(dao, presenter);
        final LoginInputData inputData = new LoginInputData(DNE_USERNAME, CORRECT_PASSWORD);

        interactor.execute(inputData);

        assertNull(presenter.getSuccessUsername(), "Success view should not be called.");
        assertTrue(presenter.getFailMessage().contains(DNE_USERNAME + ": Account does not exist."),
                "Should return DNE error message.");
    }

    @Test
    void testLoginFailure_IncorrectPassword() {
        final InMemoryUserDataAccessObject dao = new InMemoryUserDataAccessObject();
        final MockLoginPresenter presenter = new MockLoginPresenter();
        final LoginInteractor interactor = new LoginInteractor(dao, presenter);
        final LoginInputData inputData = new LoginInputData(EXISTING_USERNAME, WRONG_PASSWORD);

        interactor.execute(inputData);

        assertNull(presenter.getSuccessUsername(), "Success view should not be called.");
        assertTrue(presenter.getFailMessage().contains("Incorrect password for \"" + EXISTING_USERNAME + "\"."),
                "Should return incorrect password error message.");
    }
}