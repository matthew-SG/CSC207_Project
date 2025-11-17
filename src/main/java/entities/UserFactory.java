package entities;

import java.util.List;

/**
 * Factory for creating CommonUser objects.
 */
public class UserFactory {

    public User create(String name, String password, List<MealPlan> mealPlans) {
        return new User(name, password, mealPlans);
    }
}
