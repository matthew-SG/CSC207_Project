package use_case.grocery_list;

import data_access.InMemoryUserDataAccessObject;
import entities.User;
import entities.GroceryList;
import entities.Ingredient;

import java.util.List;
import java.util.Map;

public class TestSetup {

    public static final String TEST_USERNAME = "test_grocery_user";
    public static final String TEST_PASSWORD = "password";

    public static InMemoryUserDataAccessObject setupLoggedInUser(List<Ingredient> initialGroceryList) {

        InMemoryUserDataAccessObject dao = new InMemoryUserDataAccessObject();

        dao.signupUser(TEST_USERNAME, TEST_PASSWORD);
        dao.login(TEST_USERNAME, TEST_PASSWORD);

        Map<String, User> users = dao.getUsers();
        User currentUser = users.get(TEST_USERNAME);
        if (currentUser != null) {
            currentUser.setGroceryList(new GroceryList(initialGroceryList));
        }

        return dao;
    }
}