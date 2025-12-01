package interface_adapter.login;

import interface_adapter.ViewManagerModel;
import interface_adapter.grocery_list.GroceryController;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.nav_bar.NavbarManagerViewModel;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;

/**
 * The Presenter for the Login Use Case.
 */
public class LoginPresenter implements LoginOutputBoundary {

    private final LoginViewModel loginViewModel;
    private final LoggedInViewModel loggedInViewModel;
    private final ViewManagerModel viewManagerModel;
    private final NavbarManagerViewModel navbarManagerViewModel;
    private final GroceryController groceryController;

    public LoginPresenter(ViewManagerModel viewManagerModel,
                          LoggedInViewModel loggedInViewModel,
                          LoginViewModel loginViewModel, NavbarManagerViewModel navbarManagerViewModel,
                          GroceryController groceryController) {
        this.viewManagerModel = viewManagerModel;
        this.loggedInViewModel = loggedInViewModel;
        this.loginViewModel = loginViewModel;
        this.navbarManagerViewModel = navbarManagerViewModel;
        this.groceryController = groceryController;
    }

    @Override
    public void prepareSuccessView(LoginOutputData response) {
        // On success, update the loggedInViewModel's state
        final LoggedInState loggedInState = loggedInViewModel.getState();
        loggedInState.setUsername(response.getUsername());
        this.loggedInViewModel.firePropertyChange();

        // and clear everything from the LoginViewModel's state
        loginViewModel.setState(new LoginState());

        // switch to the logged in view
        this.navbarManagerViewModel.setState(NavbarManagerViewModel.LOGGED_IN);
        this.navbarManagerViewModel.firePropertyChange();
        this.viewManagerModel.getState().viewName = loggedInViewModel.getViewName();
        this.viewManagerModel.getState().isLoggedIn = true;
        this.viewManagerModel.getState().userName = response.getUsername();
        this.viewManagerModel.firePropertyChange();
        this.groceryController.load();
    }

    @Override
    public void prepareFailView(String error) {
        final LoginState loginState = loginViewModel.getState();
        loginState.setLoginError(error);
        loginViewModel.firePropertyChange();
    }
}
