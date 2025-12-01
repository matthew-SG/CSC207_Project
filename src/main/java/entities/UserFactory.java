package entities;

import java.util.List;

/**
 * Factory for creating CommonUser objects.
 */
public class UserFactory {

    /**
     * Creates a user object.
     * @param name name of the user
     * @param password password of the user
     * @param savedRecipes saved recipes of the user
     * @param mealPlans meal plans of the user
     * @param groceryList grocery list of the user
     * @return the new user
     */
    public User create(String name, String password, List<Recipe> savedRecipes, List<MealPlan> mealPlans,
                       GroceryList groceryList) {
        return new User(name, password, savedRecipes, mealPlans, groceryList);
    }
}
