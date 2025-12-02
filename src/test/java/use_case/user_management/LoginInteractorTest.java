package use_case.user_management;

import data_access.InMemoryUserDataAccessObject;
import org.junit.jupiter.api.Test;

import use_case.login.LoginInputData;
import use_case.login.LoginInteractor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for LoginInteractor.
 * It verifies the business logic for user login, including successful login
 * and various failure scenarios (user non-existent, incorrect password).
 */
class LoginInteractorTest {

    private static final String EXISTING_USERNAME = "test_1";
    private static final String CORRECT_PASSWORD = "password";
    private static final String DNE_USERNAME = "nonExistentUser";
    private static final String WRONG_PASSWORD = "wrongPassword";

    /**
     * Tests the scenario for a successful user login.
     * It verifies that the interactor calls the presenter's success view with the correct username.
     */
    @Test
    void testLoginSuccess() {
        final InMemoryUserDataAccessObject dao = new InMemoryUserDataAccessObject(null);
        final MockLoginPresenter presenter = new MockLoginPresenter();

        final LoginInteractor interactor = new LoginInteractor(dao, presenter);
        final LoginInputData inputData = new LoginInputData(EXISTING_USERNAME, CORRECT_PASSWORD);

        interactor.execute(inputData);

        assertEquals(EXISTING_USERNAME, presenter.getSuccessUsername(),
                "Success view should be called with correct username.");
        assertNull(presenter.getFailMessage(), "Fail view should not be called.");
    }

    /**
     * Tests the scenario where a login attempt is made with a username that does not exist.
     * It verifies that the interactor calls the presenter's fail view with the appropriate error message.
     */
    @Test
    void testLoginFailure_UserDNE() {
        final InMemoryUserDataAccessObject dao = new InMemoryUserDataAccessObject(null);
        final MockLoginPresenter presenter = new MockLoginPresenter();
        final LoginInteractor interactor = new LoginInteractor(dao, presenter);
        final LoginInputData inputData = new LoginInputData(DNE_USERNAME, CORRECT_PASSWORD);

        interactor.execute(inputData);

        assertNull(presenter.getSuccessUsername(), "Success view should not be called.");
        assertTrue(presenter.getFailMessage().contains(DNE_USERNAME + ": Account does not exist."),
                "Should return DNE error message.");
    }

    /**
     * Tests the scenario where a login attempt is made with an existing username but an incorrect password.
     * It verifies that the interactor calls the presenter's fail view with the appropriate error message.
     */
    @Test
    void testLoginFailure_IncorrectPassword() {
        // NOTE: Similar to testLoginSuccess, relies on implicit setup of "test_1" in the DAO.
        final InMemoryUserDataAccessObject dao = new InMemoryUserDataAccessObject(null);
        final MockLoginPresenter presenter = new MockLoginPresenter();
        final LoginInteractor interactor = new LoginInteractor(dao, presenter);
        final LoginInputData inputData = new LoginInputData(EXISTING_USERNAME, WRONG_PASSWORD);

        interactor.execute(inputData);

        assertNull(presenter.getSuccessUsername(), "Success view should not be called.");
        assertTrue(presenter.getFailMessage().contains("Incorrect password for \"" + EXISTING_USERNAME + "\"."),
                "Should return incorrect password error message.");
    }
}
