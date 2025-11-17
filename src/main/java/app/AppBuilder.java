package app;

import data_access.DummyCommunityDataAccessObject;
import data_access.InMemoryCommunityDataAccessObject;
import data_access.InMemoryUserDataAccessObject;
import data_access.UserDataAccess;
import entities.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.community.CommunityController;
import interface_adapter.community.CommunityPresenter;
import interface_adapter.community.CommunityViewModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.logout.LogoutPresenter;
import interface_adapter.nav_bar.NavbarController;
import interface_adapter.nav_bar.NavbarManagerViewModel;
import interface_adapter.nav_bar.NavbarPresenter;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import use_case.community.CommunityDataAccessInterface;
import use_case.community.CommunityInputBoundary;
import use_case.community.CommunityMarketInteractor;
import use_case.community.CommunityOutputBoundary;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInteractor;
import use_case.logout.LogoutOutputBoundary;
import use_case.nav_bar.NavbarInteractor;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import view.*;

import javax.swing.*;
import java.awt.*;

/**
 * Builder class for constructing the application.
 * Uses the builder pattern to construct all application components
 * and create the main JFrame window.
 */
public class AppBuilder {
    // Required components
    private UserFactory userFactory = new UserFactory();
    private UserDataAccess userDataAccessObject = new InMemoryUserDataAccessObject();
    private CommunityDataAccessInterface communityDataAccessObject = new DummyCommunityDataAccessObject();
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private ViewManagerModel viewManagerModel;
    private ViewManager viewManager;

    // Error pop up
    ErrorMessageView errorMessageView;

    // Community components
    private CommunityViewModel communityViewModel;
    private JPanel communityContentPanel;
    private CardLayout communityCardLayout;
    private CommunityView communityView;
    private SelectLikedRecipeView selectLikedRecipeView;
    private WriteReviewView writeReviewView;
    private CommunityController communityController;
    private CommunityManagerView communityManagerView;

    // Auth components
    private SignupViewModel signupViewModel;
    private SignupView signupView;
    private LoginViewModel loginViewModel;
    private LoginView loginView;
    private LoggedInViewModel loggedInViewModel;
    private LoggedInView loggedInView;

    // Navigation
    private NavBarManagerView navBarManagerView;
    private NavbarManagerViewModel navbarManagerViewModel;
    private NavbarUnloggedInView navBar;
    private NavbarLoggedInView navBarLoggedIn;
    private JPanel navBarContentPanel;
    private CardLayout navBarCardLayout;

    /**
     * Initialize the builder with default setup.
     */
    public AppBuilder() {
        initializeViewManagerAndLayouts();
    }

    /**
     * Initialize view manager and card layouts.
     */
    private void initializeViewManagerAndLayouts() {
        contentPanel = new JPanel();
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);

        viewManagerModel = new ViewManagerModel();
        viewManager = new ViewManager(contentPanel, cardLayout, viewManagerModel);
        communityViewModel = new CommunityViewModel();
    }

    public AppBuilder buildErrorPopUp(){
        errorMessageView = new ErrorMessageView(viewManagerModel);
        return this;
    }

    /**
     * Build community feature components.
     * Sets up all views, controllers, and use cases related to the community review feature.
     * 
     * @return this builder for method chaining
     */
    public AppBuilder buildCommunityFeature() {
        communityContentPanel = new JPanel();
        communityCardLayout = new CardLayout();
        communityContentPanel.setLayout(communityCardLayout);

        // Create community views
        communityView = new CommunityView(communityViewModel, viewManagerModel);
        selectLikedRecipeView = new SelectLikedRecipeView(communityViewModel);
        writeReviewView = new WriteReviewView(communityViewModel);

        // Add views to community panel
        communityContentPanel.add(communityView, CommunityViewModel.VIEWING);
        communityContentPanel.add(selectLikedRecipeView, CommunityViewModel.SELECTING_RECIPE);
        communityContentPanel.add(writeReviewView, CommunityViewModel.WRITING_REVIEW);

        // Setup community manager
        communityManagerView = new CommunityManagerView(
                communityContentPanel,
                communityCardLayout,
                communityViewModel
        );

        // Wire up community use case
        CommunityOutputBoundary communityPresenter = new CommunityPresenter(
                viewManagerModel,
                communityViewModel
        );
        CommunityInputBoundary communityInteractor = new CommunityMarketInteractor(
                communityDataAccessObject,
                communityPresenter
        );
        communityController = new CommunityController(communityInteractor);

        // Set controllers on views
        communityView.setCommunityController(communityController);
        selectLikedRecipeView.setCommunityController(communityController);
        writeReviewView.setCommunityController(communityController);

        // Set initial state
        communityViewModel.getState().subviewName = CommunityViewModel.VIEWING;
        communityViewModel.firePropertyChange();

        // Add to main content panel
        contentPanel.add(communityContentPanel, communityViewModel.getViewName());

        return this;
    }

    /**
     * Build authentication feature components.
     * Sets up signup and login views with their view models.
     * 
     * @return this builder for method chaining
     */
    public AppBuilder buildAuthFeature() {
        loggedInViewModel = new LoggedInViewModel();
        loggedInView = new LoggedInView(loggedInViewModel);

        signupViewModel = new SignupViewModel();
        signupView = new SignupView(signupViewModel);

        loginViewModel = new LoginViewModel();
        loginView = new LoginView(loginViewModel);

        final SignupOutputBoundary signupOutputBoundary = new SignupPresenter(viewManagerModel,
                signupViewModel, loginViewModel);
        final SignupInputBoundary userSignupInteractor = new SignupInteractor(
                userDataAccessObject, signupOutputBoundary, userFactory);

        SignupController controller = new SignupController(userSignupInteractor);
        signupView.setSignupController(controller);

        final LoginOutputBoundary loginOutputBoundary = new LoginPresenter(viewManagerModel,
                loggedInViewModel, loginViewModel, navbarManagerViewModel);
        final LoginInputBoundary loginInteractor = new LoginInteractor(
                userDataAccessObject, loginOutputBoundary);

        LoginController loginController = new LoginController(loginInteractor);
        loginView.setLoginController(loginController);

        final LogoutOutputBoundary logoutOutputBoundary = new LogoutPresenter(viewManagerModel,
                loggedInViewModel, loginViewModel, navbarManagerViewModel);

        final LogoutInputBoundary logoutInteractor =
                new LogoutInteractor(userDataAccessObject, logoutOutputBoundary);

        final LogoutController logoutController = new LogoutController(logoutInteractor);
        loggedInView.setLogoutController(logoutController);

        // Add to main content panel
        contentPanel.add(signupView, signupViewModel.getViewName());
        contentPanel.add(loginView, loginViewModel.getViewName());
        contentPanel.add(loggedInView, loggedInView.getViewName());


        return this;
    }

    /**
     * Build navigation bar.
     * Sets up the navigation bar with its controller and presenter.
     * 
     * @return this builder for method chaining
     */
    public AppBuilder buildNavigation() {
        navBar = new NavbarUnloggedInView();
        navBarLoggedIn = new NavbarLoggedInView();

        NavbarController navbarController = new NavbarController(
                new NavbarInteractor(
                        new NavbarPresenter(viewManagerModel, communityViewModel)
                )
        );
        navBar.setNavbarController(navbarController);
        navBarLoggedIn.setNavbarController(navbarController);

        navBarCardLayout = new CardLayout();
        navBarContentPanel = new JPanel();
        navBarContentPanel.setLayout(navBarCardLayout);

        navBarContentPanel.add(navBar, NavbarManagerViewModel.UNLOGGED_IN);
        navBarContentPanel.add(navBarLoggedIn, NavbarManagerViewModel.LOGGED_IN);

        navbarManagerViewModel = new NavbarManagerViewModel();
        navBarManagerView = new NavBarManagerView(navBarContentPanel, navBarCardLayout,navbarManagerViewModel);



        return this;
    }

    /**
     * Build and display the application window.
     * Creates a JFrame with all configured components and makes it visible.
     * 
     * @return the created and displayed JFrame
     */
    public JFrame build() {
        validateBuilder();

        return createAndShowFrame();
    }

    /**
     * Create and configure the main application frame.
     * 
     * @return the configured JFrame
     */
    private JFrame createAndShowFrame() {
        JFrame frame = new JFrame("Recipe Generator Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        frame.add(navBarContentPanel, BorderLayout.NORTH);
        frame.add(contentPanel, BorderLayout.CENTER);
        navbarManagerViewModel.setState(NavbarManagerViewModel.UNLOGGED_IN);
        navbarManagerViewModel.firePropertyChange();
        communityViewModel.getState().subviewName = CommunityViewModel.VIEWING;
        communityViewModel.firePropertyChange();
        viewManagerModel.getState().viewName = LoginViewModel.viewName;
        viewManagerModel.firePropertyChange();

        frame.setVisible(true);
        return frame;
    }

    /**
     * Validate that all required components are initialized.
     * 
     * @throws IllegalStateException if required components are missing
     */
    private void validateBuilder() {
        if (contentPanel == null || cardLayout == null) {
            throw new IllegalStateException("Content panel and card layout must be initialized");
        }
        if (viewManagerModel == null || viewManager == null) {
            throw new IllegalStateException("View manager components must be initialized");
        }
        if (navBar == null) {
            throw new IllegalStateException("Navigation bar must be built before creating App");
        }
    }

    // Getters for testing purposes

    public JPanel getContentPanel() {
        return contentPanel;
    }

    public ViewManagerModel getViewManagerModel() {
        return viewManagerModel;
    }

    public CommunityViewModel getCommunityViewModel() {
        return communityViewModel;
    }

    public CommunityController getCommunityController() {
        return communityController;
    }

    public SignupViewModel getSignupViewModel() {
        return signupViewModel;
    }

    public LoginViewModel getLoginViewModel() {
        return loginViewModel;
    }
}
