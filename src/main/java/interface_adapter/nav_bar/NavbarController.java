package interface_adapter.nav_bar;


import use_case.nav_bar.NavbarInputBoundary;

public class NavbarController {
    private final NavbarInputBoundary navbarInteractor;


    public NavbarController(NavbarInputBoundary navbarInteractor) {
        this.navbarInteractor = navbarInteractor;
    }

    public void switchToLogin(){
        this.navbarInteractor.switchToLogin();
    }
    public void switchToSignUp(){
        this.navbarInteractor.switchToSignUp();
    }
    public void switchToCommunity(){
        this.navbarInteractor.switchToCommunity();
    }
    public void switchToGenerateRecipe(){
        this.navbarInteractor.switchToGenerateRecipe();
    }

    public void switchToApproveRecipe(){
        this.navbarInteractor.switchToApproveRecipe();
    }

    public void switchToProfile(){
        this.navbarInteractor.switchToProfile();
    }

    public void switchToGroceryList(){this.navbarInteractor.switchToGroceryList();
    }

    public void switchToMealPlan(){
        this.navbarInteractor.switchToMealPlan();
    }

    public void switchToSearchByIngredients(){this.navbarInteractor.switchToSearchByIngredients();}

    public void switchToLikedRecipeList() {
        this.navbarInteractor.switchToLikedRecipeList();
    }
}
