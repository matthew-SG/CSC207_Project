package data_access;

import entities.MealPlan;
import entities.Recipe;
import entities.User;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.meal_plan.MealPlanUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * In-memory implementation of the DAO for storing user data. This implementation does
 * NOT persist data between runs of the program.
 */
public class InMemoryUserDataAccessObject implements UserDataAccess, MealPlanUserDataAccessInterface {

    private final Map<String, User> users = new HashMap<>();

    private String currentUsername;

    @Override
    public boolean existsByName(String identifier) {
        return users.containsKey(identifier);
    }

    @Override
    public void save(User user) {
        users.put(user.getName(), user);
    }

    @Override
    public User get(String username) {
        return users.get(username);
    }

    @Override
    public void setCurrentUsername(String name) {
        currentUsername = name;
    }

    @Override
    public String getCurrentUsername() {
        return currentUsername;
    }

    /**
     * Get the users map for accessing user data.
     * @return the map of users
     */
    public Map<String, User> getUsers() {
        return users;
    }

    @Override
    public List<Recipe> getSavedRecipes() {
        return users.get(currentUsername).getSavedRecipes();
    }

    @Override
    public void saveMealPlan(MealPlan mealPlan) {
        users.get(currentUsername).saveMealPlan(mealPlan);
    }
}
