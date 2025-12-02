package use_case.user_management;

import data_access.InMemoryUserDataAccessObject;
import org.junit.jupiter.api.Test;
import use_case.logout.LogoutInteractor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for LogoutInteractor.
 * It verifies the business logic for logging out a user, ensuring the user's session
 * is cleared and the appropriate presenter view is called.
 */
class LogoutInteractorTest {

    private final String TEST_USERNAME = "test_1";
    private final String TEST_PASSWORD = "password";

    /**
     * Tests the scenario for a successful user logout.
     * It verifies that the user is logged out in the DAO and the presenter's success view is called.
     */
    @Test
    void testLogoutSuccess() {
        final InMemoryUserDataAccessObject dao = new InMemoryUserDataAccessObject();
        dao.login(TEST_USERNAME, TEST_PASSWORD);

        assertEquals(TEST_USERNAME, dao.getCurrentUsername(),
                "Pre-condition failed: user should be logged in.");

        final MockLogoutPresenter presenter = new MockLogoutPresenter();
        final LogoutInteractor interactor = new LogoutInteractor(dao, presenter);

        interactor.execute();

        assertEquals(TEST_USERNAME, presenter.getSuccessUsername(),
                "Presenter should receive the logging-out username.");
    }
}
