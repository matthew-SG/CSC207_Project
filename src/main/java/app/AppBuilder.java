package app;

import data_access.*;
import entities.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.approve_recipe.ApproveRecipeController;
import interface_adapter.approve_recipe.ApproveRecipePresenter;
import interface_adapter.approve_recipe.ApproveRecipeViewModel;
import interface_adapter.community.CommunityController;
import interface_adapter.community.CommunityPresenter;
import interface_adapter.community.CommunityViewModel;
import interface_adapter.grocery_list.GroceryController;
import interface_adapter.grocery_list.GroceryPresenter;
import interface_adapter.grocery_list.GroceryViewModel;
import interface_adapter.load_meal_plan.LoadMealPlanController;
import interface_adapter.load_meal_plan.LoadMealPlanPresenter;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.logout.LogoutPresenter;
import interface_adapter.meal_plan.MealPlanController;
import interface_adapter.meal_plan.MealPlanGeneratedViewModel;
import interface_adapter.meal_plan.MealPlanGeneratorViewModel;
import interface_adapter.meal_plan.MealPlanPresenter;
import interface_adapter.nav_bar.NavbarController;
import interface_adapter.nav_bar.NavbarManagerViewModel;
import interface_adapter.nav_bar.NavbarPresenter;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import interface_adapter.view_meal_plans.ViewMealPlansController;
import interface_adapter.view_meal_plans.ViewMealPlansPresenter;
import interface_adapter.view_meal_plans.ViewMealPlansViewModel;
import use_case.approve_recipe.ApproveRecipeDataAccessInterface;
import use_case.approve_recipe.ApproveRecipeInputBoundary;
import use_case.approve_recipe.ApproveRecipeInteractor;
import use_case.approve_recipe.ApproveRecipeOutputBoundary;
import use_case.community.CommunityDataAccessInterface;
import use_case.community.CommunityInputBoundary;
import use_case.community.CommunityMarketInteractor;
import use_case.community.CommunityOutputBoundary;
import use_case.grocery_list.add.AddInteractor;
import use_case.grocery_list.delete.DeleteInteractor;
import use_case.grocery_list.edit.EditInteractor;
import use_case.grocery_list.load.LoadInteractor;
import use_case.load_meal_plan.LoadMealPlanInputBoundary;
import use_case.load_meal_plan.LoadMealPlanInteractor;
import use_case.load_meal_plan.LoadMealPlanOutputBoundary;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInteractor;
import use_case.logout.LogoutOutputBoundary;
import use_case.meal_plan.MealPlanInputBoundary;
import use_case.meal_plan.MealPlanInteractor;
import use_case.meal_plan.MealPlanOutputBoundary;
import use_case.nav_bar.NavbarInteractor;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import use_case.view_meal_plans.ViewMealPlansInputBoundary;
import use_case.view_meal_plans.ViewMealPlansInteractor;
import use_case.view_meal_plans.ViewMealPlansOutputBoundary;
import data_access.DummyRecipeDataAccessObject;
import interface_adapter.recipe_generator.RecipeGeneratorController;
import interface_adapter.recipe_generator.RecipeGeneratorPresenter;
import interface_adapter.recipe_generator.RecipeGeneratorViewModel;
import use_case.recipe_generator.RecipeGeneratorInteractor;
import use_case.recipe_generator.RecipeDataAccessInterface;
import use_case.recipe_generator.RecipeGeneratorInputBoundary;
import use_case.recipe_generator.RecipeGeneratorOutputBoundary;
import view.RecipeGeneratorView;

import view.*;

import interface_adapter.grocery_list.*;

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
    // In Memory Data Access Object
    // private UserDataAccess userDataAccessObject = new InMemoryUserDataAccessObject();
    // Persistent File Data Access Object
    private UserDataAccess userDataAccessObject = new FileDataAccessObject("data\\users.csv", userFactory);
    private CommunityDataAccessInterface communityDataAccessObject = new DBCommunityDataAccessObject();
    private ApproveRecipeDataAccessInterface approveRecipeDataAccessObject;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private ViewManagerModel viewManagerModel;
    private ViewManager viewManager;

    // Error pop up
    ErrorMessageView errorMessageView;

    // Approve Recipe components
    private ApproveRecipeViewModel approveRecipeViewModel;
    private ApproveRecipeView approveRecipeView;
    private ApproveRecipeController approveRecipeController;

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
    private NavbarPresenter navbarPresenter;

    // Meal Plan Generator Use Case
    private MealPlanGeneratorView mealPlanGeneratorView;
    private MealPlanGeneratorViewModel mealPlanGeneratorViewModel;
    private MealPlanGeneratedView mealPlanGeneratedView;
    private MealPlanGeneratedViewModel mealPlanGeneratedViewModel;

    // View Meal Plans Use Case
    private ViewMealPlansViewModel viewMealPlansViewModel;
    private ViewMealPlansView viewMealPlansView;

    private GroceryState groceryState;
    private GroceryViewModel groceryViewModel;
    private GroceryController groceryController;
    private GroceryView groceryView;

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

        // Initialize approve recipe DAO with API access to real recipes
        approveRecipeDataAccessObject = new SpoonacularApproveRecipeDataAccessObject(
                userDataAccessObject.getUsers()
        );
        communityViewModel = new CommunityViewModel();
    }

    public AppBuilder buildErrorPopUp(){
        errorMessageView = new ErrorMessageView(viewManagerModel);
        return this;
    }

    /**
     * Build approve recipe feature components.
     * Sets up view, controller, and use case for approving/declining recipes.
     *
     * @return this builder for method chaining
     */
    public AppBuilder buildApproveRecipeFeature() {
        approveRecipeViewModel = new ApproveRecipeViewModel();
        approveRecipeView = new ApproveRecipeView(approveRecipeViewModel, viewManagerModel);

        // Wire up use case
        ApproveRecipeOutputBoundary approveRecipePresenter = new ApproveRecipePresenter(
                viewManagerModel,
                approveRecipeViewModel
        );
        ApproveRecipeInputBoundary approveRecipeInteractor = new ApproveRecipeInteractor(
                approveRecipeDataAccessObject,
                approveRecipePresenter
        );
        approveRecipeController = new ApproveRecipeController(approveRecipeInteractor);

        // Set controller on view
        approveRecipeView.setApproveRecipeController(approveRecipeController);

        // Set controller on navbar presenter if it exists
        if (navbarPresenter != null) {
            navbarPresenter.setApproveRecipeController(approveRecipeController);
        }

        // Add to main content panel
        contentPanel.add(approveRecipeView, approveRecipeViewModel.getViewName());

        return this;
    }

    public AppBuilder buildRecipeGeneratorFeature() {
        RecipeGeneratorViewModel recipeGeneratorViewModel =
                new RecipeGeneratorViewModel();

        // Presenter
        RecipeGeneratorOutputBoundary recipeGeneratorPresenter =
                new RecipeGeneratorPresenter(recipeGeneratorViewModel, viewManagerModel);

        // 3. Data access accessing dummy recipe generator
        RecipeDataAccessInterface recipeGateway =
                new DummyRecipeDataAccessObject();

        //interactor
        RecipeGeneratorInputBoundary recipeGeneratorInteractor =
                new RecipeGeneratorInteractor(recipeGateway, recipeGeneratorPresenter);

        //Controller
        RecipeGeneratorController recipeGeneratorController =
                new RecipeGeneratorController(recipeGeneratorInteractor);

        // View
        RecipeGeneratorView recipeGeneratorView =
                new RecipeGeneratorView(
                        recipeGeneratorViewModel,
                        recipeGeneratorController,
                        viewManagerModel
                );

        // register view with main content panel
        contentPanel.add(recipeGeneratorView, recipeGeneratorViewModel.getViewName());

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
        writeReviewView = new WriteReviewView(communityViewModel, loggedInViewModel);

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
        communityViewModel.getState().setSubviewName(CommunityViewModel.VIEWING);
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

        navbarPresenter = new NavbarPresenter(viewManagerModel, communityViewModel, mealPlanGeneratorViewModel);
        NavbarController navbarController = new NavbarController(
                new NavbarInteractor(navbarPresenter)
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

    public AppBuilder buildGroceryList() {
        String jsonPath = "grocery_list.json";
        JsonGroceryRepository repo = new JsonGroceryRepository(jsonPath);

        GroceryViewModel vm = new GroceryViewModel();
        GroceryPresenter presenter = new GroceryPresenter(vm);

        AddInteractor addUC = new AddInteractor(repo, presenter);
        EditInteractor editUC = new EditInteractor(repo, presenter);
        DeleteInteractor deleteUC = new DeleteInteractor(repo, presenter);
        LoadInteractor loadUC = new LoadInteractor(repo, presenter);

        GroceryController controller = new GroceryController(addUC, editUC, deleteUC, loadUC);
        contentPanel.add(new GroceryView(controller, vm), "Grocery_List");
        return this;
    }



    public AppBuilder buildMealPlan() {
        mealPlanGeneratorViewModel = new MealPlanGeneratorViewModel();
        mealPlanGeneratorView = new MealPlanGeneratorView(mealPlanGeneratorViewModel);
        mealPlanGeneratedViewModel = new MealPlanGeneratedViewModel();
        mealPlanGeneratedView = new MealPlanGeneratedView(mealPlanGeneratedViewModel);
        viewMealPlansViewModel = new ViewMealPlansViewModel();
        viewMealPlansView = new ViewMealPlansView(viewMealPlansViewModel);


        final MealPlanOutputBoundary mealPlanPresenter = new MealPlanPresenter(mealPlanGeneratorViewModel,
                mealPlanGeneratedViewModel, viewManagerModel);
        final MealPlanInputBoundary mealPlanInteractor = new MealPlanInteractor(userDataAccessObject,
                mealPlanPresenter);
        final ViewMealPlansOutputBoundary viewMealPlansPresenter = new ViewMealPlansPresenter(
                mealPlanGeneratorViewModel, viewMealPlansViewModel, viewManagerModel);
        final ViewMealPlansInputBoundary viewMealPlansInteractor = new ViewMealPlansInteractor(userDataAccessObject,
                viewMealPlansPresenter);
        final LoadMealPlanOutputBoundary loadMealPlanPresenter = new LoadMealPlanPresenter(mealPlanGeneratedViewModel,
                viewManagerModel);
        final LoadMealPlanInputBoundary loadMealPlanInteractor = new LoadMealPlanInteractor(userDataAccessObject,
                loadMealPlanPresenter);

        MealPlanController mealPlanController = new MealPlanController(mealPlanInteractor);
        mealPlanGeneratorView.setMealPlanController(mealPlanController);
        ViewMealPlansController viewMealPlansController = new ViewMealPlansController(viewMealPlansInteractor);
        mealPlanGeneratorView.setViewMealPlansController(viewMealPlansController);
        LoadMealPlanController loadMealPlanController = new LoadMealPlanController(loadMealPlanInteractor);
        viewMealPlansView.setLoadMealPlanController(loadMealPlanController);

        contentPanel.add(mealPlanGeneratorView, mealPlanGeneratorView.getViewName());
        contentPanel.add(mealPlanGeneratedView, mealPlanGeneratedView.getViewName());
        contentPanel.add(viewMealPlansView, viewMealPlansView.getViewName());

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
        frame.setSize(1920, 1080);
        frame.setLayout(new BorderLayout());

        frame.add(navBarContentPanel, BorderLayout.NORTH);
        frame.add(contentPanel, BorderLayout.CENTER);
        navbarManagerViewModel.setState(NavbarManagerViewModel.UNLOGGED_IN);
        navbarManagerViewModel.firePropertyChange();
        communityViewModel.getState().setSubviewName(CommunityViewModel.VIEWING);
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
