package use_case.user_management;

import data_access.InMemoryUserDataAccessObject;
import entities.UserFactory;

import org.junit.jupiter.api.Test;
import use_case.signup.SignupInputData;
import use_case.signup.SignupInteractor;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Test class for SignupInteractor.
 * It verifies the business logic for user registration, including successful signup
 * and various failure scenarios (password mismatch, empty fields, user already exists).
 */
class SignupInteractorTest {

    private final String NEW_USERNAME = "newUserForTest";
    private final String EXISTING_USERNAME = "test_1";
    private final String TEST_PASSWORD = "testPassword";

    /**
     * Tests the scenario for a successful user signup.
     * It verifies that the interactor calls the presenter's success view with the new username.
     */
    @Test
    void testSignupSuccess() {
        final InMemoryUserDataAccessObject dao = new InMemoryUserDataAccessObject("5b07df6820b74cf1b2eae9c1b440f014");
        final MockSignupPresenter presenter = new MockSignupPresenter();
        final UserFactory userFactory = new UserFactory();

        final SignupInteractor interactor = new SignupInteractor(dao, presenter, userFactory);
        final SignupInputData inputData = new SignupInputData(NEW_USERNAME, TEST_PASSWORD, TEST_PASSWORD);

        interactor.execute(inputData);

        assertNotNull(presenter.getSuccessUsername());
        assertEquals(NEW_USERNAME, presenter.getSuccessUsername());
    }

    /**
     * Tests the scenario where the provided passwords do not match.
     * It verifies that the interactor calls the presenter's fail view with the appropriate error message.
     */
    @Test
    void testSignupFailure_PasswordsDontMatch() {
        final InMemoryUserDataAccessObject dao = new InMemoryUserDataAccessObject();
        final MockSignupPresenter presenter = new MockSignupPresenter();
        final UserFactory userFactory = new UserFactory();
        final SignupInteractor interactor = new SignupInteractor(dao, presenter, userFactory);
        final SignupInputData inputData = new SignupInputData(NEW_USERNAME, TEST_PASSWORD, "mismatch");

        interactor.execute(inputData);

        assertNull(presenter.getSuccessUsername());
        assertEquals("Passwords don't match.", presenter.getFailMessage());
    }

    /**
     * Tests the scenario where the password field is left empty.
     * It verifies that the interactor calls the presenter's fail view with the appropriate error message.
     */
    @Test
    void testSignupFailure_EmptyPassword() {
        final InMemoryUserDataAccessObject dao = new InMemoryUserDataAccessObject();
        final MockSignupPresenter presenter = new MockSignupPresenter();
        final UserFactory userFactory = new UserFactory();
        final SignupInteractor interactor = new SignupInteractor(dao, presenter, userFactory);
        final SignupInputData inputData = new SignupInputData(NEW_USERNAME, "", "");

        interactor.execute(inputData);

        assertNull(presenter.getSuccessUsername());
        assertEquals("New password cannot be empty", presenter.getFailMessage());
    }

    /**
     * Tests the scenario where the username field is left empty.
     * It verifies that the interactor calls the presenter's fail view with the appropriate error message.
     */
    @Test
    void testSignupFailure_EmptyUsername() {
        final InMemoryUserDataAccessObject dao = new InMemoryUserDataAccessObject();
        final MockSignupPresenter presenter = new MockSignupPresenter();
        final UserFactory userFactory = new UserFactory();
        final SignupInteractor interactor = new SignupInteractor(dao, presenter, userFactory);
        final SignupInputData inputData = new SignupInputData("", TEST_PASSWORD, TEST_PASSWORD);

        interactor.execute(inputData);

        assertNull(presenter.getSuccessUsername());
        assertEquals("Username cannot be empty", presenter.getFailMessage());
    }

    /**
     * Tests the scenario where the attempted username already exists in the system.
     * It verifies that the interactor calls the presenter's fail view with the appropriate error message.
     */
    @Test
    void testSignupFailure_UserExists() {
        final InMemoryUserDataAccessObject dao = new InMemoryUserDataAccessObject();
        final MockSignupPresenter presenter = new MockSignupPresenter();
        final UserFactory userFactory = new UserFactory();
        final SignupInteractor interactor = new SignupInteractor(dao, presenter, userFactory);
        final SignupInputData inputData = new SignupInputData(EXISTING_USERNAME, TEST_PASSWORD, TEST_PASSWORD);

        interactor.execute(inputData);

        assertNull(presenter.getSuccessUsername());
        assertEquals("Username already exists", presenter.getFailMessage());
    }

    /**
     * Tests the switchToLoginView method, ensuring the presenter is instructed
     * to switch the current view to the login screen.
     */
    @Test
    void testSwitchToLoginView() {
        final InMemoryUserDataAccessObject dao = new InMemoryUserDataAccessObject();
        final MockSignupPresenter presenter = new MockSignupPresenter();
        final UserFactory userFactory = new UserFactory();
        final SignupInteractor interactor = new SignupInteractor(dao, presenter, userFactory);

        interactor.switchToLoginView();

        assertTrue(presenter.isLoginViewSwitched(), "Presenter's switchToLoginView method should be called.");
    }
}
