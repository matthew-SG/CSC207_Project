package use_case.signup;

import entities.User;

/**
 * DAO interface for the Signup Use Case.
 */
public interface SignupUserDataAccessInterface {
    String USER_EXISTS_ERROR = "USER_EXISTS_ERROR";
    String SUCCESS = "SUCCESS";
    /*
    * Try to create a user with the credentials, and update DAO's state to store related credentials.
    * Return SUCCESS if successful
    * Return defined error messages otherwise
    * */
    String signupUser(String email, String password);

    /**
     * Returns the username of the current user of the application.
     * @return the username of the current user
     */
    String getCurrentUsername();
}
