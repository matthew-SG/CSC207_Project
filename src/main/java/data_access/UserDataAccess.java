package data_access;

import entities.User;
import use_case.approve_recipe.ApproveRecipeDataAccessInterface;
import use_case.delete_meal_plan.DeleteMealPlanDataAccessInterface;
import use_case.likedRecipeList.LikedRecipeDataAccessInterface;
import use_case.load_meal_plan.LoadMealPlanDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.meal_plan.MealPlanUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;
import use_case.view_meal_plans.ViewMealPlansDataAccessInterface;

import java.util.Map;

public interface UserDataAccess extends SignupUserDataAccessInterface,
        LoginUserDataAccessInterface,
        LogoutUserDataAccessInterface,
        MealPlanUserDataAccessInterface, ViewMealPlansDataAccessInterface, LoadMealPlanDataAccessInterface,
        DeleteMealPlanDataAccessInterface, ApproveRecipeDataAccessInterface, LikedRecipeDataAccessInterface {

    /**
     * Get the users map for accessing user data.
     * @return the map of users
     */
    Map<String, User> getUsers();
}
