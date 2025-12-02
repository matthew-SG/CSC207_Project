package use_case.user_management;

import data_access.InMemoryUserDataAccessObject;
import org.junit.jupiter.api.Test;
import use_case.logout.LogoutInteractor;

import static org.junit.jupiter.api.Assertions.*;


class LogoutInteractorTest {

    private final String TEST_USERNAME = "test_1";
    private final String TEST_PASSWORD = "password";

    @Test
    void testLogoutSuccess() {
        final InMemoryUserDataAccessObject dao = new InMemoryUserDataAccessObject();
        dao.login(TEST_USERNAME, TEST_PASSWORD);

        assertEquals(TEST_USERNAME, dao.getCurrentUsername(),
                "Pre-condition failed: user should be logged in.");

        // 使用外部定义的 MockLogoutPresenter
        final MockLogoutPresenter presenter = new MockLogoutPresenter();
        final LogoutInteractor interactor = new LogoutInteractor(dao, presenter);

        interactor.execute();

        assertEquals(TEST_USERNAME, presenter.getSuccessUsername(),
                "Presenter should receive the logging-out username.");

    }
}