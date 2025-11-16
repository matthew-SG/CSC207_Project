package interface_adapter.login;

import interface_adapter.ViewModel;

/**
 * The View Model for the Login View.
 */
public class LoginViewModel extends ViewModel<LoginState> {
    public static String viewName = "log in";
    public LoginViewModel() {
        super(viewName);
        setState(new LoginState());
    }

}
