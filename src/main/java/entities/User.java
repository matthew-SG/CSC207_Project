package entities;

import java.util.List;

/**
 * Represents a user in the recipe management application.
 */
public class User {
    private String username;
    private String password;
    private List<Recipe> savedRecipes;
    private GroceryList groceryList;
    private List<MealPlan> mealPlans;

    // Constructors
    public User(String username, String password, List<Recipe> savedRecipes, List<MealPlan> mealPlans,
                GroceryList groceryList) {
        this.username = username;
        this.password = password;
        this.savedRecipes = savedRecipes;
        this.mealPlans = mealPlans;
        this.groceryList = groceryList;
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

    public void saveRecipe(Recipe recipe) { savedRecipes.add(recipe); }

    public GroceryList getGroceryList() {
        return groceryList;
    }

    public List<MealPlan> getMealPlans() { return mealPlans; }

    public void saveMealPlan(MealPlan mealPlan) { mealPlans.add(mealPlan); }

    public void setGroceryList(GroceryList groceryList) {
        this.groceryList = groceryList;
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
