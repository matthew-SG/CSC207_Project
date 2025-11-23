package use_case.signup;

import entities.User;
import entities.UserFactory;

/**
 * The Signup Interactor.
 */
public class SignupInteractor implements SignupInputBoundary {
    private final SignupUserDataAccessInterface userDataAccessObject;
    private final SignupOutputBoundary userPresenter;
    private final UserFactory userFactory;

    public SignupInteractor(SignupUserDataAccessInterface signupDataAccessInterface,
                            SignupOutputBoundary signupOutputBoundary,
                            UserFactory userFactory) {
        this.userDataAccessObject = signupDataAccessInterface;
        this.userPresenter = signupOutputBoundary;
        this.userFactory = userFactory;
    }

    @Override
    public void execute(SignupInputData signupInputData) {
        if (!signupInputData.getPassword().equals(signupInputData.getRepeatPassword())) {
            userPresenter.prepareFailView("Passwords don't match.");
        }
        else if ("".equals(signupInputData.getPassword())) {
            userPresenter.prepareFailView("New password cannot be empty");
        }
        else if ("".equals(signupInputData.getUsername())) {
            userPresenter.prepareFailView("Username cannot be empty");
        }
        else {
            String returnCode = userDataAccessObject.signupUser(signupInputData.getUsername(),
                    signupInputData.getPassword());
            if (returnCode.equals(SignupUserDataAccessInterface.USER_EXISTS_ERROR)) {
                userPresenter.prepareFailView("Username already exists");
            }
            else if (returnCode.equals(SignupUserDataAccessInterface.SUCCESS)) {
                final SignupOutputData signupOutputData = new SignupOutputData(userDataAccessObject.getCurrentUsername());
                userPresenter.prepareSuccessView(signupOutputData);
            }

        }
    }

    @Override
    public void switchToLoginView() {
        userPresenter.switchToLoginView();
    }
}
