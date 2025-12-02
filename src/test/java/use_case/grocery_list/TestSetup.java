package use_case.grocery_list;

import data_access.InMemoryUserDataAccessObject;
import entities.User;
import entities.GroceryList;
import entities.Ingredient;

import java.util.List;
import java.util.Map;

/**
 * A utility class providing setup methods specifically for testing the
 * Grocery List use cases. It helps in creating a predefined state, such as a
 * logged-in user with a specific initial grocery list, for integration testing.
 */
public class TestSetup {

    /**
     * Constant representing the username used for testing purposes.
     */
    public static final String TEST_USERNAME = "test_grocery_user";
    /**
     * Constant representing the password used for testing purposes.
     */
    public static final String TEST_PASSWORD = "password";

    /**
     * Sets up a logged-in user with a specified initial grocery list state in the in-memory DAO.
     * This method simulates user registration and login, and then initializes their grocery list.
     *
     * @param initialGroceryList The list of Ingredient entities to pre-populate the user's grocery list with.
     * @return The configured InMemoryUserDataAccessObject instance ready for testing.
     */
    public static InMemoryUserDataAccessObject setupLoggedInUser(List<Ingredient> initialGroceryList) {

        // Sets up temporary DAO, api key does not matter
        final InMemoryUserDataAccessObject dao = new InMemoryUserDataAccessObject("a");

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
