package use_case.user_management;

import data_access.InMemoryUserDataAccessObject;
import entities.UserFactory;

import org.junit.jupiter.api.Test;
import use_case.signup.SignupInputData;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import use_case.signup.SignupOutputData;

import static org.junit.jupiter.api.Assertions.*;

class MockSignupPresenter implements SignupOutputBoundary {
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

    public String getFailMessage() { return failMessage; }
    public String getSuccessUsername() { return successUsername; }
    public boolean isLoginViewSwitched() { return loginViewSwitched; }
}


public class SignupInteractorTest {

    private final String NEW_USERNAME = "newUserForTest";
    private final String EXISTING_USERNAME = "test_1";
    private final String TEST_PASSWORD = "testPassword";

    @Test
    void testSignupSuccess() {
        final InMemoryUserDataAccessObject dao = new InMemoryUserDataAccessObject();
        final MockSignupPresenter presenter = new MockSignupPresenter();

        // Use the actual UserFactory class now that it is available
        final UserFactory userFactory = new UserFactory();
        final SignupInteractor interactor = new SignupInteractor(dao, presenter, userFactory);

        final SignupInputData inputData = new SignupInputData(NEW_USERNAME, TEST_PASSWORD, TEST_PASSWORD);

        interactor.execute(inputData);

        // Verify Success Branch (Covers the successful path)
        assertNotNull(presenter.getSuccessUsername(), "Success view should be called.");
        assertEquals(NEW_USERNAME, presenter.getSuccessUsername());
    }

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