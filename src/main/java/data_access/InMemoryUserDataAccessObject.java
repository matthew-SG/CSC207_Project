package data_access;

import entities.*;
import use_case.login.LoginUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * In-memory implementation of the DAO for storing user data. This implementation does
 * NOT persist data between runs of the program.
 */
public class InMemoryUserDataAccessObject implements UserDataAccess {

    // Constants for testing Meal Plan Use Case
    private static final String PASSWORD = "password";
    private static final String CALORIES = "Calories";
    private static final String PROTEIN = "Protein";
    private static final String CARBOHYDRATES = "Carbohydrates";
    private static final String FAT = "Fats";
    private static final String RECIPE_NAME = "Pasta With Tuna";
    private static final String RECIPE_IMAGE = "https://img.spoonacular.com/recipes/654959-312x231.jpg";
    private static final String RECIPE_CUISINE = "Italian";

    private final Map<String, User> users = new HashMap<>();
    
    private String currentUsername;

    /**
     * Constructor for InMemoryUserDAO that constructs pregenerated Users for testing purposes (primarily for Meal
     *      Plan Use Case, other Use Cases may add to test users if they wish)
     */
    public InMemoryUserDataAccessObject() {
        User testUserOne = new User("test_1", PASSWORD, new ArrayList<>(), new ArrayList<>(),
                new GroceryList(new ArrayList<>()));
        User testUserTwo = new User("test_2", PASSWORD, new ArrayList<>(), new ArrayList<>(),
                new GroceryList(new ArrayList<>()));
        User testUserThree = new User("test_3", PASSWORD, new ArrayList<>(), new ArrayList<>(),
                new GroceryList(new ArrayList<>()));

        Ingredient flour = new Ingredient("flour", 2, "Tbsps");
        Ingredient greenO = new Ingredient("green onions", 100, "g");

        Recipe miniPastaTuna = new Recipe(654959, RECIPE_NAME,
                RECIPE_IMAGE, new ArrayList<>(), RECIPE_CUISINE, new HashMap<>());
        miniPastaTuna.addIngredient(flour); miniPastaTuna.addIngredient(greenO);
        miniPastaTuna.addNutritionalValue(CALORIES, 422);
        miniPastaTuna.addNutritionalValue(CARBOHYDRATES, 57);
        miniPastaTuna.addNutritionalValue(PROTEIN, 24);
        miniPastaTuna.addNutritionalValue(FAT, 10);

        Recipe copyOne = new Recipe(654959, RECIPE_NAME,
                RECIPE_IMAGE, new ArrayList<>(), RECIPE_CUISINE, new HashMap<>());
        copyOne.addIngredient(flour); copyOne.addIngredient(greenO);
        copyOne.addNutritionalValue(CALORIES, 422);
        copyOne.addNutritionalValue(CARBOHYDRATES, 57);
        copyOne.addNutritionalValue(PROTEIN, 24);
        copyOne.addNutritionalValue(FAT, 10);

        Recipe copyTwo = new Recipe(654959, RECIPE_NAME,
                RECIPE_IMAGE, new ArrayList<>(), RECIPE_CUISINE, new HashMap<>());
        copyTwo.addIngredient(flour); copyTwo.addIngredient(greenO);
        copyTwo.addNutritionalValue(CALORIES, 422);
        copyTwo.addNutritionalValue(CARBOHYDRATES, 57);
        copyTwo.addNutritionalValue(PROTEIN, 24);
        copyTwo.addNutritionalValue(FAT, 10);

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
        return this.currentUsername;
    }

    @Override
    public List<InstructionStep> getAnalyzedInstructions(int recipeId) {
        return List.of();
    }

    @Override
    public void logout() {
        // Unsure if we actually need it to run
    }

    /**
     * Get the users map for accessing user data.
     * @return the map of users
     */
    public Map<String, User> getUsers() {
        return this.users;
    }

    @Override
    public String login(String username, String password) {
        if (!this.users.containsKey(username)) {
            return LoginUserDataAccessInterface.USER_DNE_ERROR;
        } else if (!this.users.get(username).getPassword().equals(password)) {
            return LoginUserDataAccessInterface.INCORRECT_PASSWORD_ERROR;
        }
        this.currentUsername = username;
        return LoginUserDataAccessInterface.SUCCESS;
    }

    @Override
    public String signupUser(String username, String password) {
        if (this.users.containsKey(username)) {
            return SignupUserDataAccessInterface.USER_EXISTS_ERROR;
        }
        User user = new User(username, password, new ArrayList<>(), new ArrayList<>(), new GroceryList(new ArrayList<>()));
        this.currentUsername = username;
        this.users.put(this.currentUsername, user);
        return SignupUserDataAccessInterface.SUCCESS;
    }

    @Override
    public List<Recipe> getSavedRecipes() {
        return this.users.get(this.currentUsername).getSavedRecipes();
    }

    @Override
    public void saveMealPlan(MealPlan mealPlan) {
        this.users.get(this.currentUsername).saveMealPlan(mealPlan);
    }

    @Override
    public List<MealPlan> getMealPlans() {
        return this.users.get(this.currentUsername).getMealPlans();
    }

    @Override
    public void deleteMealPlan(int index) {
        this.users.get(this.currentUsername).getMealPlans().remove(index);
    }

    @Override
    public List<Recipe> getAvailableRecipes() {
        return List.of();
    }

    @Override
    public void saveLikedRecipe(String username, Recipe recipe) {
        users.get(username).getSavedRecipes().add(recipe);
    }

    @Override
    public void deleteLikedRecipe(String username, int recipeId) {
        List<Recipe> likedRecipes = users.get(username).getSavedRecipes();
        for (Recipe recipe : likedRecipes) {
            if (recipe.getRecipeId() == recipeId) {
                users.get(username).getSavedRecipes().remove(recipe);
                return;
            }
        }
    }

    @Override
    public List<Recipe> getLikedRecipes(String username) {
        return users.get(username).getSavedRecipes();
    }

    @Override
    public Recipe getRecipeById(int recipeId) {
        List<Recipe> likedRecipes =  users.get(currentUsername).getSavedRecipes();
        for (Recipe recipe : likedRecipes) {
            if (recipe.getRecipeId() == recipeId) {
                return recipe;
            }
        }
        return null;
    }

    @Override
    public User getUser(String username) {
        return users.get(username);
    }

    @Override
    public void saveRecipeToUser(String username, Recipe recipe) {
        // In-memory implementation - not used for approve recipe in production
        // Just add to user's saved recipes if user exists
        User user = this.users.get(username);
        if (user != null) {
            boolean alreadySaved = user.getSavedRecipes().stream()
                    .anyMatch(r -> r.getRecipeId() == recipe.getRecipeId());
            if (!alreadySaved) {
                user.getSavedRecipes().add(recipe);
            }
        }
    }

    @Override
    public void removeFromPendingApproval(int recipeId) {
        // In-memory implementation - no pending approval list to maintain
        // This is a no-op since in-memory DAO doesn't manage pending recipes
    }

    @Override
    public void setAvailableRecipes(List<Recipe> recipes) {
        // In-memory implementation - no pending approval list to maintain
        // This is a no-op since in-memory DAO doesn't manage pending recipes
    }

    @Override
    public List<Ingredient> load() {
        if (this.currentUsername == null || !this.users.containsKey(this.currentUsername)) {
            return new ArrayList<>();
        }

        User currentUser = this.users.get(this.currentUsername);
        if (currentUser.getGroceryList() == null) {
            return new ArrayList<>();
        }
        return currentUser.getGroceryList().getItems();
    }


    @Override
    public void save(List<Ingredient> list) {
        if (this.currentUsername != null && this.users.containsKey(this.currentUsername)) {
            User currentUser = this.users.get(this.currentUsername);

            currentUser.setGroceryList(new GroceryList(list));
        }

    }
}
