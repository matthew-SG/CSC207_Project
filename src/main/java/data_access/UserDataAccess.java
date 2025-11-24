package data_access;

import entities.User;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.meal_plan.MealPlanUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

import java.util.Map;

public interface UserDataAccess extends SignupUserDataAccessInterface,
        LoginUserDataAccessInterface,
        LogoutUserDataAccessInterface,
        MealPlanUserDataAccessInterface {

    /**
     * Get the users map for accessing user data.
     * @return the map of users
     */
    Map<String, User> getUsers();
}
