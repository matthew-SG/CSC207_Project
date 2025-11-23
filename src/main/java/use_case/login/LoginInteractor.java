package use_case.login;

import java.util.Objects;

/**
 * The Login Interactor.
 */
public class LoginInteractor implements LoginInputBoundary {
    private final LoginUserDataAccessInterface userDataAccessObject;
    private final LoginOutputBoundary loginPresenter;

    public LoginInteractor(LoginUserDataAccessInterface userDataAccessInterface,
                           LoginOutputBoundary loginOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        this.loginPresenter = loginOutputBoundary;
    }

    @Override
    public void execute(LoginInputData loginInputData) {
        final String username = loginInputData.getUsername();
        final String password = loginInputData.getPassword();
        String returnCode = userDataAccessObject.login(username, password);
        if (Objects.equals(returnCode, LoginUserDataAccessInterface.USER_DNE_ERROR)) {
            loginPresenter.prepareFailView(username + ": Account does not exist.");
        } else if (Objects.equals(returnCode, LoginUserDataAccessInterface.INCORRECT_PASSWORD_ERROR)) {
            loginPresenter.prepareFailView("Incorrect password for \"" + username + "\".");
        }
        else if (returnCode.equals(LoginUserDataAccessInterface.SUCCESS)) {
            final LoginOutputData loginOutputData = new LoginOutputData(userDataAccessObject.getCurrentUsername());
            loginPresenter.prepareSuccessView(loginOutputData);
        }
    }
}
