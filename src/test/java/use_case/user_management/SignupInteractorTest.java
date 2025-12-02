package use_case.user_management;

import data_access.InMemoryUserDataAccessObject;
import entities.UserFactory;

import org.junit.jupiter.api.Test;
import use_case.signup.SignupInputData;
import use_case.signup.SignupInteractor;

import static org.junit.jupiter.api.Assertions.*;


class SignupInteractorTest {

    private final String NEW_USERNAME = "newUserForTest";
    private final String EXISTING_USERNAME = "test_1";
    private final String TEST_PASSWORD = "testPassword";

    @Test
    void testSignupSuccess() {
        final InMemoryUserDataAccessObject dao = new InMemoryUserDataAccessObject();
        // 使用外部定义的 MockSignupPresenter
        final MockSignupPresenter presenter = new MockSignupPresenter();
        final UserFactory userFactory = new UserFactory();

        final SignupInteractor interactor = new SignupInteractor(dao, presenter, userFactory);
        final SignupInputData inputData = new SignupInputData(NEW_USERNAME, TEST_PASSWORD, TEST_PASSWORD);

        interactor.execute(inputData);

        assertNotNull(presenter.getSuccessUsername());
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