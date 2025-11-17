package entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user in the recipe management application.
 */
public class User {
    private String username;
    private String password;

    private ArrayList<Recipe> savedRecipes;
    private GroceryList groceryList;
    private NutritionGoal nutritionGoal;
    private List<MealPlan> mealPlans;

    // Constructors
    public User(String username, String password, List<MealPlan> mealPlans) {
        this.username = username;
        this.password = password;
        this.savedRecipes = new ArrayList<>();
        this.groceryList = new GroceryList();
        this.nutritionGoal = new NutritionGoal();
        this.mealPlans = mealPlans;
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

    public String getName(){
        return this.username;
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

    public List<MealPlan> getMealPlans() { return mealPlans; }

    public void saveMealPlan(MealPlan mealPlan) { mealPlans.add(mealPlan); }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", savedRecipes=" + savedRecipes.size() +
                ", groceryListItems=" + groceryList.getItems().size() +
                '}';
    }
}
