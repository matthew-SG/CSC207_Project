package use_case.nav_bar;

import use_case.nav_bar.NavbarOutputBoundary;

/**
 * Mock implementation of NavbarOutputBoundary for testing purposes.
 * Tracks which view was last switched to, allowing tests to verify
 * that the interactor correctly delegates to the presenter.
 */
public class NavbarMockPresenter implements NavbarOutputBoundary {
    private String lastViewSwitched = null;

    @Override
    public void switchToLogin() {
        this.lastViewSwitched = "login";
    }

    @Override
    public void switchToSignUp() {
        this.lastViewSwitched = "signup";
    }

    @Override
    public void switchToCommunity() {
        this.lastViewSwitched = "community";
    }

    @Override
    public void switchToGenerateRecipe() {
        this.lastViewSwitched = "generateRecipe";
    }

    @Override
    public void switchToApproveRecipe() {
        this.lastViewSwitched = "approveRecipe";
    }

    @Override
    public void switchToProfile() {
        this.lastViewSwitched = "profile";
    }

    @Override
    public void switchToGroceryList() {
        this.lastViewSwitched = "groceryList";
    }

    @Override
    public void switchToMealPlan() {
        this.lastViewSwitched = "mealPlan";
    }

    @Override
    public void switchToSearchByIngredients() {
        this.lastViewSwitched = "searchByIngredients";
    }

    @Override
    public void switchToLikedRecipeList() {
        this.lastViewSwitched = "likedRecipeList";
    }

    /**
     * Gets the last view that was switched to.
     * @return the name of the last view switched to, or null if no switch has occurred
     */
    public String getLastViewSwitched() {
        return lastViewSwitched;
    }
}
