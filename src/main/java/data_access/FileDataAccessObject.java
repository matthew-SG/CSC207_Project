package data_access;

import static data_access.Constants.*;

import java.io.*;
import java.util.*;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import entities.*;
import use_case.login.LoginUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

/**
 * DAO for all data, mainly user data, using a File to persist the data
 */
public class FileDataAccessObject implements UserDataAccess {

    private final File usersCsv;
    private final Map<String, Integer> headers = new LinkedHashMap<>();
    private final Map<String, User> users = new HashMap<>();

    // Temporary storage for recipes waiting to be approved
    private List<Recipe> pendingApprovalRecipes = new ArrayList<>();

    private String currentUsername;

    /**
     * Construct this DAO for saving to and reading from local files
     * @param csvPath the path of the file to save users to
     * @param userFactory factory for creating user objects
     * @throws RuntimeException if there is an IOException when accessing the file
     */
    public FileDataAccessObject(String csvPath, UserFactory userFactory) throws RuntimeException {

        usersCsv = new File(csvPath);
        headers.put("username", 0);
        headers.put("password", 1);

        ensureDirectoryExists(usersCsv);

        if (usersCsv.length() == 0) {
            save();
        } else {
            try (BufferedReader reader = new BufferedReader(new FileReader(usersCsv))) {
                final String header = reader.readLine();

                if (!header.equals(HEADER)) {
                    throw new RuntimeException(String.format("header should be%n: %s%n but was:%n%s", HEADER, header));
                }

                String row;
                while ((row = reader.readLine()) != null) {
                    final String[] col = row.split(",");
                    final String username = String.valueOf(col[headers.get("username")]);
                    final String password = String.valueOf(col[headers.get("password")]);
                    final List<Recipe> likedRecipes = loadLikedRecipes(username);
                    final List<MealPlan> mealPlans = loadMealPlans(username);
                    final GroceryList groceryList = loadGroceryList(username);

                    User user = userFactory.create(username, password, likedRecipes, mealPlans, groceryList);
                    users.put(username, user);
                }
            }
            catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

    }

    /**
     * Saves all changes made to any persistent data, which includes:
     *      New users
     *      New recipes in user's saved recipes
     *      New Meal Plans in user's saved meal plans
     *      Changes to a user's grocery list
     * @throws RuntimeException if there is an IOException when writing to the files
     */
    private void save() throws RuntimeException {
        final BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(usersCsv));
            writer.write(String.join(",", headers.keySet()));
            writer.newLine();

            for (User user : users.values()) {
                final String likedRecipesPath = String.format(USER_LIKED_RECIPES_PATH, user.getUsername());
                final String mealPlansPath = String.format(USER_MEAL_PLANS_PATH, user.getUsername());
                final String groceryListPath = String.format(USER_GROCERY_LIST_PATH, user.getUsername());

                final String line = String.format("%s,%s", user.getUsername(), user.getPassword());
                writer.write(line);
                writer.newLine();

                saveLikedRecipes(user, likedRecipesPath);
                saveMealPlans(user, mealPlansPath);
                saveGroceryList(user, groceryListPath);
            }

            writer.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }

    /**
     * Helper method that saves a user's liked recipes to a local persistent file. Creates a new file for the user,
     *      and the parent directory for the file if they do not already exist i.e. the user is a new user
     * @param user the user to save the liked recipes for
     * @param jsonPath the path to their liked recipes JSON
     * @throws RuntimeException if there is an IOException when writing to the file
     */
    private static void saveLikedRecipes(User user, String jsonPath) throws RuntimeException {
        File file = new File(jsonPath);
        ensureDirectoryExists(file);

        final List<Recipe> likedRecipes = user.getSavedRecipes();
        JSONArray recipesArray = recipesToJson(likedRecipes);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(recipesArray.toString(4));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Helper method that converts a list of recipes into the required JSONArray format
     * @param likedRecipes the list of recipes to be converted
     * @return the JSONArray representation of the recipes
     */
    @NotNull
    private static JSONArray recipesToJson(List<Recipe> likedRecipes) {
        JSONArray result = new JSONArray();

        for (Recipe recipe : likedRecipes) {
            JSONObject recipeJson = new JSONObject();

            recipeJson.put(RECIPE_ID, recipe.getRecipeId());
            recipeJson.put(RECIPE_NAME, recipe.getRecipeName());
            recipeJson.put(RECIPE_IMAGE, recipe.getRecipeImage());

            JSONArray ingredientsArray = new JSONArray();
            for (Ingredient ingredient : recipe.getIngredients()) {
                JSONObject ingredientObject = new JSONObject();

                ingredientObject.put(NAME, ingredient.getName());
                ingredientObject.put(QUANTITY, ingredient.getQuantity());
                ingredientObject.put(UNIT, ingredient.getUnit());

                ingredientsArray.put(ingredientObject);
            }

            recipeJson.put(INGREDIENTS, ingredientsArray);
            recipeJson.put(MEAL_TYPE, recipe.getMealType());

            JSONObject nutritionalValues = new JSONObject();
            for (String nutritionalValue : recipe.getNutritionalValues().keySet()) {
                nutritionalValues.put(nutritionalValue, recipe.getNutritionalValues().get(nutritionalValue));
            }
            recipeJson.put(NUTRITIONAL_VALUES, nutritionalValues);

            result.put(recipeJson);
        }
        return result;
    }

    /**
     * Helper method that saves changes of a user's meal plan to their associated local file
     * @param user the user to save changes to
     * @param jsonPath the path to their associated meal plan persistent file
     * @throws RuntimeException when an IOException is raised when writing to the file
     */
    private static void saveMealPlans(User user, String jsonPath) throws RuntimeException {
        File file = new File(jsonPath);

        final List<MealPlan> mealPlans = user.getMealPlans();
        JSONArray mealPlansArray = new JSONArray();
        ensureDirectoryExists(file);

        for (MealPlan mealPlan : mealPlans) {
            JSONObject mealPlanObject = new JSONObject();

            JSONArray recipes = recipesToJson(mealPlan.getRecipes());
            mealPlanObject.put(RECIPES, recipes);
            mealPlanObject.put(TARGET_CALORIES, mealPlan.getTargetCalories());
            mealPlanObject.put(TARGET_PROTEIN, mealPlan.getTargetProtein());
            mealPlanObject.put(TARGET_CARBS, mealPlan.getTargetCarbs());
            mealPlanObject.put(TARGET_FATS, mealPlan.getTargetFats());

            mealPlansArray.put(mealPlanObject);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(mealPlansArray.toString(4));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Helper method that saves changes to their grocery list
     * @param user the user to save the changes to
     * @param jsonPath the path to their associated grocery list persistent file
     * @throws RuntimeException when an IOException is raised when writing to the file
     */
    private static void saveGroceryList(User user, String jsonPath) throws RuntimeException {
        File file = new File(jsonPath);
        GroceryList groceryList = user.getGroceryList();
        ensureDirectoryExists(file);

        JSONArray groceryListArray = new JSONArray();
        for (Ingredient ingredient : groceryList.getItems()) {
            JSONObject ingredientObject = new JSONObject();
            ingredientObject.put(NAME, ingredient.getName());
            ingredientObject.put(QUANTITY, ingredient.getQuantity());
            ingredientObject.put(UNIT, ingredient.getUnit());
            groceryListArray.put(ingredientObject);
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(groceryListArray.toString(4));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Helper method that loads the liked recipes of a given user
     * @param username the username of the user
     * @return the list of their liked recipes
     * @throws RuntimeException if the reader throws an IOException when reading the file
     */
    private static List<Recipe> loadLikedRecipes(String username) throws RuntimeException {
        String filePath = String.format(USER_LIKED_RECIPES_PATH, username);
        File file = new File(filePath);

        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String likedRecipesJson = reader.readAllAsString();
            JSONArray likedRecipesArray = new JSONArray(likedRecipesJson);

            return jsonToRecipes(likedRecipesArray);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Helper method that converts a JSONArray that represents a list of recipes into a list of recipes
     * @param recipeArray the JSONArray to be converted
     * @return the List<Recipe> representation of the JSONArray
     */
    private static List<Recipe> jsonToRecipes(JSONArray recipeArray) {
        List<Recipe> result = new ArrayList<>();

        // Parameters for each recipe
        int recipeId;
        String recipeName;
        String recipeImage;
        List<Ingredient> ingredients;
        String mealType;
        Map<String, Double> nutritionalValues;

        // Parameters for each ingredient
        String ingredientName;
        double quantity;
        String unit;

        for (int i = 0; i < recipeArray.length(); i++) {
            JSONObject recipeObject = recipeArray.getJSONObject(i);
            recipeId = recipeObject.getInt(RECIPE_ID);
            recipeName = recipeObject.getString(RECIPE_NAME);
            recipeImage = recipeObject.getString(RECIPE_IMAGE);

            ingredients = new ArrayList<>();
            JSONArray ingredientsJson = recipeObject.getJSONArray(INGREDIENTS);
            for (int j = 0; j < ingredientsJson.length(); j++) {
                JSONObject ingredientObject = ingredientsJson.getJSONObject(j);
                ingredientName = ingredientObject.getString(NAME);
                quantity = ingredientObject.getDouble(QUANTITY);
                unit = ingredientObject.getString(UNIT);

                ingredients.add(new Ingredient(ingredientName, quantity, unit));
            }

            mealType = recipeObject.getString(MEAL_TYPE);

            JSONObject nutritionalValuesObject = recipeObject.getJSONObject(NUTRITIONAL_VALUES);
            nutritionalValues = new HashMap<>();
            for (String nutritionalValue : nutritionalValuesObject.keySet()) {
                nutritionalValues.put(nutritionalValue, nutritionalValuesObject.getDouble(nutritionalValue));
            }

            Recipe recipe = new Recipe(recipeId, recipeName, recipeImage, ingredients, mealType, nutritionalValues);
            result.add(recipe);
        }

        return result;
    }

    /**
     * Helper method that loads the grocery list of a given user from their associated grocery list JSON
     * @param username the username of the user to load for
     * @return the user's grocery list
     * @throws RuntimeException if reader throws an IOException when reading the file
     */
    private static GroceryList loadGroceryList(String username) throws RuntimeException {
        List<Ingredient> items = new ArrayList<>();
        String filePath = String.format(USER_GROCERY_LIST_PATH, username);
        File file = new File(filePath);

        if (!file.exists() || file.length() == 0) {
            return new GroceryList(new ArrayList<>());
        }

        // Parameters for each ingredient
        String name;
        double quantity;
        String unit;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String groceryListJson = reader.readAllAsString();
            JSONArray groceryList = new JSONArray(groceryListJson);

            for (int i = 0; i < groceryList.length(); i++) {
                JSONObject ingredient = groceryList.getJSONObject(i);

                name = ingredient.getString(NAME);
                quantity = ingredient.getDouble(QUANTITY);
                unit = ingredient.getString(UNIT);

                items.add(new Ingredient(name, quantity, unit));
            }

            return new GroceryList(items);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Helper method that loads the meal plans of a user from their associated JSON file
     * @param username the username of the user to load from
     * @return the user's list of meal plans
     * @throws RuntimeException if the reader throws an IOException when reading the file
     */
    private static List<MealPlan> loadMealPlans(String username) throws RuntimeException {
        List<MealPlan> result = new ArrayList<>();
        String filePath = String.format(USER_MEAL_PLANS_PATH, username);
        File file = new File(filePath);

        if (!file.exists() || file.length() == 0) {
            return result;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String mealPlanJson = reader.readAllAsString();
            JSONArray mealPlanArray = new JSONArray(mealPlanJson);

            for (int i = 0; i < mealPlanArray.length(); i++) {
                JSONObject mealPlan = mealPlanArray.getJSONObject(i);

                JSONArray recipesArray = mealPlan.getJSONArray(RECIPES);
                List<Recipe> recipes = jsonToRecipes(recipesArray);

                double targetCalories = mealPlan.getDouble(TARGET_CALORIES);
                double targetProtein = mealPlan.getDouble(TARGET_PROTEIN);
                double targetCarbs = mealPlan.getDouble(TARGET_CARBS);
                double targetFats = mealPlan.getDouble(TARGET_FATS);

                result.add(new MealPlan(recipes, targetCalories, targetProtein, targetCarbs, targetFats));
            }

            return result;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Helper method that ensures that the parent directory of a user's persistent data file exists
     * @param file the file to check the parent directory for
     * @return whether the parent directory previously existed
     */
    private static boolean ensureDirectoryExists(File file) {
        File directory = file.getParentFile();
        if (!directory.exists()) {
            return !directory.mkdirs();
        }

        return true;
    }

    @Override
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
    public void logout() {
        // Method has no current impact on application performance
    }

    @Override
    public List<Recipe> getSavedRecipes() {
        return users.get(currentUsername).getSavedRecipes();
    }

    @Override
    public void saveMealPlan(MealPlan mealPlan) {
        users.get(currentUsername).saveMealPlan(mealPlan);
        String mealPlanPath = String.format(USER_MEAL_PLANS_PATH, currentUsername);
        saveMealPlans(users.get(currentUsername), mealPlanPath);
    }

    @Override
    public String signupUser(String username, String password) {
        if (users.containsKey(username)) {
            return SignupUserDataAccessInterface.USER_EXISTS_ERROR;
        }
        User user = new User(username, password, new ArrayList<>(), new ArrayList<>(), new GroceryList(new ArrayList<>()));
        currentUsername = username;
        users.put(currentUsername, user);
        save();
        return SignupUserDataAccessInterface.SUCCESS;
    }

    @Override
    public String getCurrentUsername() {
        return currentUsername;
    }

    @Override
    public List<MealPlan> getMealPlans() {
        return users.get(currentUsername).getMealPlans();
    }

    @Override
    public void deleteMealPlan(int index) {
        User currentUser = users.get(currentUsername);
        currentUser.getMealPlans().remove(index);
        saveMealPlans(currentUser, USER_MEAL_PLANS_PATH);
    }

    // ApproveRecipeDataAccessInterface implementation

    /**
     * Get recipes that are pending approval
     * @return list of recipes waiting to be approved
     */
    @Override
    public List<Recipe> getAvailableRecipes() {
        return new ArrayList<>(pendingApprovalRecipes);
    }

    /**
     * Set recipes that should be available for approval
     * @param recipes the recipes from recipe generator or search
     */
    public void setAvailableRecipes(List<Recipe> recipes) {
        this.pendingApprovalRecipes = recipes != null ? new ArrayList<>(recipes) : new ArrayList<>();
    }

    @Override
    public Recipe getRecipeById(int recipeId) {
        for (Recipe recipe : pendingApprovalRecipes) {
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

    /**
     * Save an approved recipe to the user's saved recipes and persist to JSON
     * @param username the username
     * @param recipe the recipe to save
     */
    @Override
    public void saveRecipeToUser(String username, Recipe recipe) {
        User user = users.get(username);
        if (user == null) {
            System.err.println("User not found: " + username);
            return;
        }

        // Check if recipe already exists
        boolean alreadySaved = user.getSavedRecipes().stream()
                .anyMatch(r -> r.getRecipeId() == recipe.getRecipeId());

        if (!alreadySaved) {
            user.getSavedRecipes().add(recipe);
            // Persist changes to JSON file
            save();
        }

        // Remove from pending approval list
        removeFromPendingApproval(recipe.getRecipeId());
    }

    /**
     * Remove a recipe from the pending approval list (after approve or decline)
     * @param recipeId the ID of the recipe to remove
     */
    public void removeFromPendingApproval(int recipeId) {
        pendingApprovalRecipes.removeIf(r -> r.getRecipeId() == recipeId);
    }

    @Override
    public void save(List<Ingredient> list) {
        User user = users.get(currentUsername);

        if (user != null) {
            user.setGroceryList(new GroceryList(list));

            String jsonPath = String.format(USER_GROCERY_LIST_PATH, currentUsername);
            
            saveGroceryList(user, jsonPath);
        }
    }

    @Override
    public List<Ingredient> load() {
        User user = users.get(currentUsername);

        if (user != null && user.getGroceryList() != null) {
            return user.getGroceryList().getItems();
        }

        return new ArrayList<>();
    }
}
