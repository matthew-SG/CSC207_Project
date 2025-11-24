package data_access;

import entities.Ingredient;
import entities.MealPlan;
import entities.Recipe;
import entities.User;
import use_case.login.LoginUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;
import use_case.view_meal_plans.ViewMealPlansDataAccessInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * In-memory implementation of the DAO for storing user data. This implementation does
 * NOT persist data between runs of the program.
 */
public class InMemoryUserDataAccessObject implements UserDataAccess, MealPlanUserDataAccessInterface,
        ViewMealPlansDataAccessInterface {

    private final Map<String, User> users = new HashMap<>();
    
    private String currentUsername;

    /**
     * Constructor for InMemoryUserDAO that constructs pregenerated Users for testing purposes (primarily for Meal
     *      Plan Use Case, other Use Cases may add to test users if they wish)
     */
    public InMemoryUserDataAccessObject() {
        User testUserOne = new User("test_1", "password", new ArrayList<>());
        User testUserTwo = new User("test_2", "password", new ArrayList<>());
        User testUserThree = new User("test_3", "password", new ArrayList<>());

        Recipe miniPastaTuna = new Recipe(654959, "Pasta With Tuna",
                "https://img.spoonacular.com/recipes/654959-312x231.jpg", "Italian");
        Ingredient flour = new Ingredient("flour", 2, "Tbsps");
        Ingredient greenO = new Ingredient("green onions", 100, "g");
        miniPastaTuna.addIngredient(flour); miniPastaTuna.addIngredient(greenO);
        miniPastaTuna.addNutritionalValue("Calories", 422);
        miniPastaTuna.addNutritionalValue("Carbohydrates", 57);
        miniPastaTuna.addNutritionalValue("Protein", 24);
        miniPastaTuna.addNutritionalValue("Fat", 10);

        Recipe copyOne = new Recipe(654959, "Pasta With Tuna",
                "https://img.spoonacular.com/recipes/654959-312x231.jpg", "Italian");
        copyOne.addIngredient(flour); copyOne.addIngredient(greenO);
        copyOne.addNutritionalValue("Calories", 422);
        copyOne.addNutritionalValue("Carbohydrates", 57);
        copyOne.addNutritionalValue("Protein", 24);
        copyOne.addNutritionalValue("Fat", 10);

        Recipe copyTwo = new Recipe(654959, "Pasta With Tuna",
                "https://img.spoonacular.com/recipes/654959-312x231.jpg", "Italian");
        copyTwo.addIngredient(flour); copyTwo.addIngredient(greenO);
        copyTwo.addNutritionalValue("Calories", 422);
        copyTwo.addNutritionalValue("Carbohydrates", 57);
        copyTwo.addNutritionalValue("Protein", 24);
        copyTwo.addNutritionalValue("Fat", 10);

        testUserTwo.saveRecipe(miniPastaTuna);
        testUserTwo.saveRecipe(copyOne);

        testUserThree.saveRecipe(miniPastaTuna);
        testUserThree.saveRecipe(copyOne);
        testUserThree.saveRecipe(copyTwo);

        users.put("test_1", testUserOne);
        users.put("test_2", testUserTwo);
        users.put("test_3", testUserThree);

    }

    @Override
    public String getCurrentUsername() {
        return currentUsername;
    }

    @Override
    public void logout() {

    }

    /**
     * Get the users map for accessing user data.
     * @return the map of users
     */
    public Map<String, User> getUsers() {
        return users;
    }

    @Override
    public String login(String username, String password) {
        if (!users.containsKey(username)) {
            return LoginUserDataAccessInterface.USER_DNE_ERROR;
        } else if (!users.get(username).getPassword().equals(password)) {
            return LoginUserDataAccessInterface.INCORRECT_PASSWORD_ERROR;
        }
        currentUsername = username;
        return LoginUserDataAccessInterface.SUCCESS;
    }

    @Override
    public String signupUser(String email, String password) {
        if (users.containsKey(email)) {
            return SignupUserDataAccessInterface.USER_EXISTS_ERROR;
        }
        User user = new User(email, password, new ArrayList<>());
        currentUsername = email;
        users.put(currentUsername, user);
        return SignupUserDataAccessInterface.SUCCESS;
    }
    
    public List<Recipe> getSavedRecipes() {
        return users.get(currentUsername).getSavedRecipes();
    }

    @Override
    public void saveMealPlan(MealPlan mealPlan) {
        users.get(currentUsername).saveMealPlan(mealPlan);
    }

    @Override
    public List<MealPlan> getMealPlans() {
        return users.get(currentUsername).getMealPlans();
    }
}
