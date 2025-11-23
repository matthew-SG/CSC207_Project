package data_access;

import entities.User;
import use_case.login.LoginUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * In-memory implementation of the DAO for storing user data. This implementation does
 * NOT persist data between runs of the program.
 */
public class InMemoryUserDataAccessObject implements UserDataAccess {

    private final Map<String, User> users = new HashMap<>();

    private String currentUsername;

    @Override
    public String getCurrentUsername() {
        return currentUsername;
    }

    @Override
    public void logout() {

    }

    /**
     * Get the users map for accessing user data.
     * @return the map of users
     */
    public Map<String, User> getUsers() {
        return users;
    }

    @Override
    public String login(String username, String password) {
        if (!users.containsKey(username)) {
            return LoginUserDataAccessInterface.USER_DNE_ERROR;
        } else if (!users.get(username).getPassword().equals(password)) {
            return LoginUserDataAccessInterface.INCORRECT_PASSWORD_ERROR;
        }
        currentUsername = username;
        return LoginUserDataAccessInterface.SUCCESS;
    }

    @Override
    public String signupUser(String email, String password) {
        if (users.containsKey(email)) {
            return SignupUserDataAccessInterface.USER_EXISTS_ERROR;
        }
        User user = new User(email, password);
        currentUsername = email;
        users.put(currentUsername, user);
        return SignupUserDataAccessInterface.SUCCESS;
    }
}
