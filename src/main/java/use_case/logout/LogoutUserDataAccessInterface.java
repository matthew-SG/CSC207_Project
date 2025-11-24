package use_case.logout;

/**
 * DAO interface for the Logout Use Case.
 */
public interface LogoutUserDataAccessInterface {
    /**
     * Returns the username of the current user of the application.
     * @return the username of the current user
     */
    String getCurrentUsername();

    /**
     * Logout and reset current username.
     */
    void logout();
}
