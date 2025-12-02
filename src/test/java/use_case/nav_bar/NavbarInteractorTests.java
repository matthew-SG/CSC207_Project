package use_case.nav_bar;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for NavbarInteractor.
 * It verifies that the interactor correctly delegates navigation requests
 * to the presenter for all available views.
 */
public class NavbarInteractorTests {
    /**
     * Tests that switchToLogin correctly calls the presenter's switchToLogin method.
     */
    @Test
    void testSwitchToLogin() {
        final NavbarMockPresenter presenter = new NavbarMockPresenter();
        final NavbarInteractor interactor = new NavbarInteractor(presenter);

        interactor.switchToLogin();

        assertEquals("login", presenter.getLastViewSwitched(),
                "Presenter should switch to login view.");
    }

    /**
     * Tests that switchToSignUp correctly calls the presenter's switchToSignUp method.
     */
    @Test
    void testSwitchToSignUp() {
        final NavbarMockPresenter presenter = new NavbarMockPresenter();
        final NavbarInteractor interactor = new NavbarInteractor(presenter);

        interactor.switchToSignUp();

        assertEquals("signup", presenter.getLastViewSwitched(),
                "Presenter should switch to signup view.");
    }

    /**
     * Tests that switchToCommunity correctly calls the presenter's switchToCommunity method.
     */
    @Test
    void testSwitchToCommunity() {
        final NavbarMockPresenter presenter = new NavbarMockPresenter();
        final NavbarInteractor interactor = new NavbarInteractor(presenter);

        interactor.switchToCommunity();

        assertEquals("community", presenter.getLastViewSwitched(),
                "Presenter should switch to community view.");
    }

    /**
     * Tests that switchToGenerateRecipe correctly calls the presenter's switchToGenerateRecipe method.
     */
    @Test
    void testSwitchToGenerateRecipe() {
        final NavbarMockPresenter presenter = new NavbarMockPresenter();
        final NavbarInteractor interactor = new NavbarInteractor(presenter);

        interactor.switchToGenerateRecipe();

        assertEquals("generateRecipe", presenter.getLastViewSwitched(),
                "Presenter should switch to generate recipe view.");
    }

    /**
     * Tests that switchToApproveRecipe correctly calls the presenter's switchToApproveRecipe method.
     */
    @Test
    void testSwitchToApproveRecipe() {
        final NavbarMockPresenter presenter = new NavbarMockPresenter();
        final NavbarInteractor interactor = new NavbarInteractor(presenter);

        interactor.switchToApproveRecipe();

        assertEquals("approveRecipe", presenter.getLastViewSwitched(),
                "Presenter should switch to approve recipe view.");
    }

    /**
     * Tests that switchToProfile correctly calls the presenter's switchToProfile method.
     */
    @Test
    void testSwitchToProfile() {
        final NavbarMockPresenter presenter = new NavbarMockPresenter();
        final NavbarInteractor interactor = new NavbarInteractor(presenter);

        interactor.switchToProfile();

        assertEquals("profile", presenter.getLastViewSwitched(),
                "Presenter should switch to profile view.");
    }

    /**
     * Tests that switchToGroceryList correctly calls the presenter's switchToGroceryList method.
     */
    @Test
    void testSwitchToGroceryList() {
        final NavbarMockPresenter presenter = new NavbarMockPresenter();
        final NavbarInteractor interactor = new NavbarInteractor(presenter);

        interactor.switchToGroceryList();

        assertEquals("groceryList", presenter.getLastViewSwitched(),
                "Presenter should switch to grocery list view.");
    }

    /**
     * Tests that switchToMealPlan correctly calls the presenter's switchToMealPlan method.
     */
    @Test
    void testSwitchToMealPlan() {
        final NavbarMockPresenter presenter = new NavbarMockPresenter();
        final NavbarInteractor interactor = new NavbarInteractor(presenter);

        interactor.switchToMealPlan();

        assertEquals("mealPlan", presenter.getLastViewSwitched(),
                "Presenter should switch to meal plan view.");
    }

    /**
     * Tests that switchToSearchByIngredients correctly calls the presenter's switchToSearchByIngredients method.
     */
    @Test
    void testSwitchToSearchByIngredients() {
        final NavbarMockPresenter presenter = new NavbarMockPresenter();
        final NavbarInteractor interactor = new NavbarInteractor(presenter);

        interactor.switchToSearchByIngredients();

        assertEquals("searchByIngredients", presenter.getLastViewSwitched(),
                "Presenter should switch to search by ingredients view.");
    }

    /**
     * Tests that switchToLikedRecipeList correctly calls the presenter's switchToLikedRecipeList method.
     */
    @Test
    void testSwitchToLikedRecipeList() {
        final NavbarMockPresenter presenter = new NavbarMockPresenter();
        final NavbarInteractor interactor = new NavbarInteractor(presenter);

        interactor.switchToLikedRecipeList();

        assertEquals("likedRecipeList", presenter.getLastViewSwitched(),
                "Presenter should switch to liked recipe list view.");
    }

    /**
     * Tests that multiple sequential switch calls work correctly.
     * Verifies that the presenter tracks the most recent view switch.
     */
    @Test
    void testMultipleSwitchCallsInSequence() {
        final NavbarMockPresenter presenter = new NavbarMockPresenter();
        final NavbarInteractor interactor = new NavbarInteractor(presenter);

        interactor.switchToLogin();
        assertEquals("login", presenter.getLastViewSwitched(),
                "First switch should be to login view.");

        interactor.switchToProfile();
        assertEquals("profile", presenter.getLastViewSwitched(),
                "Second switch should be to profile view.");

        interactor.switchToCommunity();
        assertEquals("community", presenter.getLastViewSwitched(),
                "Third switch should be to community view.");
    }
}
