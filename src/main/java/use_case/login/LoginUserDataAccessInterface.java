package use_case.login;

/**
 * DAO interface for the Login Use Case.
 */
public interface LoginUserDataAccessInterface {
    public String SUCCESS = "SUCC";
    public String USER_DNE_ERROR = "USER_DNE_ERROR";
    public String INCORRECT_PASSWORD_ERROR = "INCORRECT_PASSWORD";
    /*
    * Try logging the user
    * Return SUCCESS if successful
    * Return other defined errors otherwise.
    * */
    String login(String userEmail, String password);

    String getCurrentUsername();
}
