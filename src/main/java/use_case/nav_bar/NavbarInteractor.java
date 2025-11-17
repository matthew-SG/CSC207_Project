package use_case.nav_bar;

public class NavbarInteractor implements NavbarInputBoundary{
    NavbarOutputBoundary navbarPresenter;

    public NavbarInteractor(NavbarOutputBoundary navbarPresenter){
        this.navbarPresenter = navbarPresenter;
    }

    @Override
    public void switchToLogin() {
        this.navbarPresenter.switchToLogin();
    }

    @Override
    public void switchToSignUp() {
        this.navbarPresenter.switchToSignUp();
    }

    @Override
    public void switchToCommunity() {
        this.navbarPresenter.switchToCommunity();
    }

    @Override
    public void switchToGenerateRecipe() {
        this.navbarPresenter.switchToGenerateRecipe();
    }

    @Override
    public void switchToApproveRecipe() {
        this.navbarPresenter.switchToApproveRecipe();
    }

    @Override
    public void switchToProfile(){
        this.navbarPresenter.switchToProfile();
    }
}
