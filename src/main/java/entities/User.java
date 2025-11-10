package entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user in the recipe management application.
 */
public class User {
    private String username;
    private String password;
    private String userId;

    private ArrayList<Recipe> savedRecipes;
    private GroceryList groceryList;
    private NutritionGoal nutritionGoal;

    // Constructors
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.userId = generateUserId();
        this.savedRecipes = new ArrayList<>();
        this.groceryList = new GroceryList();
        this.nutritionGoal = new NutritionGoal();
    }

    // Generate simple unique ID (this needs to be changed to generate a unique one)
    private String generateUserId() {
        return "USER-" + System.currentTimeMillis();
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public List<Recipe> getSavedRecipes() {
        return savedRecipes;
    }

    public GroceryList getGroceryList() {
        return groceryList;
    }

    public NutritionGoal getNutritionGoal() {
        return nutritionGoal;
    }


    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", savedRecipes=" + savedRecipes.size() +
                ", groceryListItems=" + groceryList.getItems().size() +
                '}';
    }
}
