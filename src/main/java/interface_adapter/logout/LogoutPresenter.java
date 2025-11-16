
package interface_adapter.logout;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;
import interface_adapter.nav_bar.NavbarManagerViewModel;
import use_case.logout.LogoutOutputBoundary;
import use_case.logout.LogoutOutputData;

/**
 * The Presenter for the Logout Use Case.
 */
public class LogoutPresenter implements LogoutOutputBoundary {

    private final LoggedInViewModel loggedInViewModel;
    private final ViewManagerModel viewManagerModel;
    private final LoginViewModel loginViewModel;
    private final NavbarManagerViewModel navbarManagerViewModel;

    public LogoutPresenter(ViewManagerModel viewManagerModel,
                           LoggedInViewModel loggedInViewModel,
                           LoginViewModel loginViewModel, NavbarManagerViewModel navbarManagerViewModel) {
        // assign to the three instance variables.
        this.viewManagerModel = viewManagerModel;
        this.loggedInViewModel = loggedInViewModel;
        this.loginViewModel = loginViewModel;
        this.navbarManagerViewModel = navbarManagerViewModel;
    }

    @Override
    public void prepareSuccessView(LogoutOutputData response) {
        // Update the LoggedInState: clear the username.
        final LoggedInState loggedInState = loggedInViewModel.getState();
        loggedInState.setUsername("");
        loginViewModel.firePropertyChange();

        loggedInState.setPassword("");
        loggedInViewModel.firePropertyChange();

        // Update the LoginState: pre-fill username with the user who just logged out.
        final LoginState loginState = loginViewModel.getState();
        loginState.setUsername(response.getUsername());
        loginViewModel.firePropertyChange();

        loginState.setPassword("");
        loginViewModel.firePropertyChange();

        // Switch to the LoginView.
        this.navbarManagerViewModel.setState(NavbarManagerViewModel.UNLOGGED_IN);
        this.navbarManagerViewModel.firePropertyChange();
        this.viewManagerModel.getState().viewName = loginViewModel.getViewName();
        this.viewManagerModel.getState().isLoggedIn = false;
        this.viewManagerModel.getState().userName = "";
        this.viewManagerModel.firePropertyChange();
    }
}
