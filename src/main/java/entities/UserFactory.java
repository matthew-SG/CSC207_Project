package entities;

import java.util.List;

/**
 * Factory for creating CommonUser objects.
 */
public class UserFactory {

    public User create(String name, String password, List<Recipe> savedRecipes, List<MealPlan> mealPlans,
                       GroceryList groceryList) {
        return new User(name, password, savedRecipes, mealPlans, groceryList);
    }
}
