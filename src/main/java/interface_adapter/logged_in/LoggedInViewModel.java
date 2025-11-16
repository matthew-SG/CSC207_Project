package interface_adapter.logged_in;

import interface_adapter.ViewModel;

/**
 * The View Model for the Logged In View.
 */
public class LoggedInViewModel extends ViewModel<LoggedInState> {
    public static String viewName = "logged in";
    public LoggedInViewModel() {
        super(viewName);
        setState(new LoggedInState());
    }

}
