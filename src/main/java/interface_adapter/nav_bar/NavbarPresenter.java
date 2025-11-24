package interface_adapter.nav_bar;

import interface_adapter.ViewManagerModel;
import interface_adapter.approve_recipe.ApproveRecipeViewModel;
import interface_adapter.community.CommunityViewModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginViewModel;
import interface_adapter.meal_plan.MealPlanGeneratorState;
import interface_adapter.meal_plan.MealPlanGeneratorViewModel;
import interface_adapter.signup.SignupViewModel;
import use_case.community.output_data.CommunityRatingsOutputData;
import use_case.nav_bar.NavbarOutputBoundary;
import view.MealPlanGeneratorView;

import java.util.Objects;

public class NavbarPresenter implements NavbarOutputBoundary {
    ViewManagerModel viewManagerModel;
    private interface_adapter.approve_recipe.ApproveRecipeController approveRecipeController;
    CommunityViewModel communityViewModel;
    MealPlanGeneratorViewModel mealPlanGeneratorViewModel;

    public NavbarPresenter(ViewManagerModel viewManagerModel, CommunityViewModel communityViewModel,
                           MealPlanGeneratorViewModel mealPlanGeneratorViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.communityViewModel = communityViewModel;
        this.mealPlanGeneratorViewModel = mealPlanGeneratorViewModel;
    }

    public void setApproveRecipeController(interface_adapter.approve_recipe.ApproveRecipeController controller) {
        this.approveRecipeController = controller;
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
        if (Objects.equals(communityViewModel.getState().getSubviewName(), CommunityViewModel.PUB_SUCC)) {
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
    public void switchToApproveRecipe() {
        // Load recipes before switching to the view
        if (approveRecipeController != null) {
            approveRecipeController.loadRecipes();
        }
        viewManagerModel.getState().viewName = ApproveRecipeViewModel.viewName;
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void switchToProfile() {
        viewManagerModel.getState().viewName = LoggedInViewModel.viewName;
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void switchToMealPlan() {
        MealPlanGeneratorState mealPlanGeneratorState = mealPlanGeneratorViewModel.getState();
        mealPlanGeneratorState.setTargetCalories("");
        mealPlanGeneratorState.setTargetProtein("");
        mealPlanGeneratorState.setTargetCarbs("");
        mealPlanGeneratorState.setTargetFats("");
        mealPlanGeneratorState.setInsufficientRecipesError(null);
        mealPlanGeneratorState.setInputsError(null);
        mealPlanGeneratorState.setNoMealPlansError(null);
        mealPlanGeneratorViewModel.firePropertyChange();
        viewManagerModel.getState().viewName = MealPlanGeneratorView.getViewName();
        viewManagerModel.firePropertyChange();
    }
}
