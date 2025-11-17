package interface_adapter.nav_bar;

import interface_adapter.ViewManagerModel;
import interface_adapter.community.CommunityViewModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginViewModel;
import interface_adapter.signup.SignupViewModel;
import use_case.community.output_data.CommunityRatingsOutputData;
import use_case.nav_bar.NavbarOutputBoundary;

import java.util.Objects;

public class NavbarPresenter implements NavbarOutputBoundary {
    ViewManagerModel viewManagerModel;
    CommunityViewModel communityViewModel;

    public NavbarPresenter(ViewManagerModel viewManagerModel, CommunityViewModel communityViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.communityViewModel = communityViewModel;
    }

    @Override
    public void switchToLogin() {
        viewManagerModel.getState().viewName = LoginViewModel.viewName;
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void switchToSignUp() {
        viewManagerModel.getState().viewName = SignupViewModel.viewName;
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void switchToCommunity() {
        if (Objects.equals(communityViewModel.getState().subviewName, CommunityViewModel.PUB_SUCC)) {
            communityViewModel.getState().prompt = CommunityRatingsOutputData.PROMPT;
            communityViewModel.firePropertyChange();
        }
        viewManagerModel.getState().viewName = CommunityViewModel.viewName;
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void switchToGenerateRecipe() {
        // TODO: implement this
    }

    @Override
    public void switchToProfile() {
        viewManagerModel.getState().viewName = LoggedInViewModel.viewName;
        viewManagerModel.firePropertyChange();
    }
}
