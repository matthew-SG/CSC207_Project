package data_access;

import entities.User;

public class DBUserDataAccessObject implements UserDataAccess {
    private String currentUsername;

    @Override
    public String login(String userEmail, String password) {
        return "";
    }

    @Override
    public String signupUser(String email, String password) {
        return "";
    }

    public String getCurrentUsername() {
        return "";
    }

    /**
     * Logout and reset current username.
     */
    @Override
    public void logout() {

    }

}
