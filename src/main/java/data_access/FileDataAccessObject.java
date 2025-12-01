package data_access;

import static data_access.Constants.*;

import java.io.*;
import java.util.*;
import java.util.Objects;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;

import entities.*;
import use_case.community.CommunityUserRecipeDataAccessInterface;
import use_case.likedRecipeList.LikedRecipeDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;
import use_case.approve_recipe.ApproveRecipeDataAccessInterface;

/**
 * DAO for all data, mainly user data, using a File to persist the data.
 */
public class FileDataAccessObject implements UserDataAccess, ApproveRecipeDataAccessInterface, CommunityUserRecipeDataAccessInterface, LikedRecipeDataAccessInterface {
    private final File usersCsv;
    private final Map<String, Integer> headers = new LinkedHashMap<>();
    private final Map<String, User> users = new HashMap<>();

    // Temporary storage for recipes waiting to be approved
    private List<Recipe> pendingApprovalRecipes = new ArrayList<>();

    private String currentUsername;

    private final FindInstructionsSpoonacular instructionsApi;
    private final String apiKey;

    /**
     * Construct this DAO for saving to and reading from local files.
     * @param csvPath the path of the file to save users to
     * @param userFactory factory for creating user objects
     * @param apiKey factory stores the api key
     * @throws RuntimeException if there is an IOException when accessing the file
     */
    public FileDataAccessObject(String csvPath, UserFactory userFactory, String apiKey) throws RuntimeException {

        usersCsv = new File(csvPath);
        this.instructionsApi = new FindInstructionsSpoonacular();
        this.apiKey = apiKey;
        headers.put("username", 0);
        headers.put("password", 1);

        ensureDirectoryExists(usersCsv);

        if (usersCsv.length() == 0) {
            save();
        }
        else {
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

                    final User user = userFactory.create(username, password, likedRecipes, mealPlans, groceryList);
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
     *      Changes to a user's grocery list.
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
        }
        catch (IOException ex) {
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
        final File file = new File(jsonPath);
        final int indentFactor = 4;
        ensureDirectoryExists(file);

        final List<Recipe> likedRecipes = user.getSavedRecipes();
        final JSONArray recipesArray = recipesToJson(likedRecipes);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(recipesArray.toString(indentFactor));
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Helper method that converts a list of recipes into the required JSONArray format.
     * @param likedRecipes the list of recipes to be converted
     * @return the JSONArray representation of the recipes
     */
    @NotNull
    private static JSONArray recipesToJson(List<Recipe> likedRecipes) {
        final JSONArray result = new JSONArray();

        for (Recipe recipe : likedRecipes) {
            final JSONObject recipeJson = new JSONObject();

            recipeJson.put(RECIPE_ID, recipe.getRecipeId());
            recipeJson.put(RECIPE_NAME, recipe.getRecipeName());
            recipeJson.put(RECIPE_IMAGE, recipe.getRecipeImage());

            final JSONArray ingredientsArray = new JSONArray();
            for (Ingredient ingredient : recipe.getIngredients()) {
                final JSONObject ingredientObject = new JSONObject();

                ingredientObject.put(NAME, ingredient.getName());
                ingredientObject.put(QUANTITY, ingredient.getQuantity());
                ingredientObject.put(UNIT, ingredient.getUnit());

                ingredientsArray.put(ingredientObject);
            }

            recipeJson.put(INGREDIENTS, ingredientsArray);
            recipeJson.put(MEAL_TYPE, recipe.getMealType());

            final JSONObject nutritionalValues = new JSONObject();
            for (String nutritionalValue : recipe.getNutritionalValues().keySet()) {
                nutritionalValues.put(nutritionalValue, recipe.getNutritionalValues().get(nutritionalValue));
            }
            recipeJson.put(NUTRITIONAL_VALUES, nutritionalValues);

            result.put(recipeJson);
        }
        return result;
    }

    /**
     * Helper method that saves changes of a user's meal plan to their associated local file.
     * @param user the user to save changes to
     * @param jsonPath the path to their associated meal plan persistent file
     * @throws RuntimeException when an IOException is raised when writing to the file
     */
    private static void saveMealPlans(User user, String jsonPath) throws RuntimeException {
        final File file = new File(jsonPath);
        final int indentFactor = 4;

        final List<MealPlan> mealPlans = user.getMealPlans();
        final JSONArray mealPlansArray = new JSONArray();
        ensureDirectoryExists(file);

        for (MealPlan mealPlan : mealPlans) {
            final JSONObject mealPlanObject = new JSONObject();

            final JSONArray recipes = recipesToJson(mealPlan.getRecipes());
            mealPlanObject.put(RECIPES, recipes);
            mealPlanObject.put(TARGET_CALORIES, mealPlan.getTargetCalories());
            mealPlanObject.put(TARGET_PROTEIN, mealPlan.getTargetProtein());
            mealPlanObject.put(TARGET_CARBS, mealPlan.getTargetCarbs());
            mealPlanObject.put(TARGET_FATS, mealPlan.getTargetFats());

            mealPlansArray.put(mealPlanObject);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(mealPlansArray.toString(indentFactor));
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Helper method that saves changes to their grocery list.
     * @param user the user to save the changes to
     * @param jsonPath the path to their associated grocery list persistent file
     * @throws RuntimeException when an IOException is raised when writing to the file
     */
    private static void saveGroceryList(User user, String jsonPath) throws RuntimeException {
        final File file = new File(jsonPath);
        final GroceryList groceryList = user.getGroceryList();
        final int indentFactor = 4;
        ensureDirectoryExists(file);

        final JSONArray groceryListArray = new JSONArray();
        for (Ingredient ingredient : groceryList.getItems()) {
            final JSONObject ingredientObject = new JSONObject();
            ingredientObject.put(NAME, ingredient.getName());
            ingredientObject.put(QUANTITY, ingredient.getQuantity());
            ingredientObject.put(UNIT, ingredient.getUnit());
            groceryListArray.put(ingredientObject);
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(groceryListArray.toString(indentFactor));
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Helper method that loads the liked recipes of a given user.
     * @param username the username of the user
     * @return the list of their liked recipes
     * @throws RuntimeException if the reader throws an IOException when reading the file
     */
    private static List<Recipe> loadLikedRecipes(String username) throws RuntimeException {
        final String filePath = String.format(USER_LIKED_RECIPES_PATH, username);
        final File file = new File(filePath);

        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            final String likedRecipesJson = reader.readAllAsString();
            final JSONArray likedRecipesArray = new JSONArray(likedRecipesJson);

            return jsonToRecipes(likedRecipesArray);

        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Helper method that converts a JSONArray that represents a list of recipes into a list of recipes.
     * @param recipeArray the JSONArray to be converted
     * @return the list representation of the JSONArray
     */
    private static List<Recipe> jsonToRecipes(JSONArray recipeArray) {
        final List<Recipe> result = new ArrayList<>();

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
            final JSONObject recipeObject = recipeArray.getJSONObject(i);
            recipeId = recipeObject.getInt(RECIPE_ID);
            recipeName = recipeObject.getString(RECIPE_NAME);
            recipeImage = recipeObject.getString(RECIPE_IMAGE);

            ingredients = new ArrayList<>();
            final JSONArray ingredientsJson = recipeObject.getJSONArray(INGREDIENTS);
            for (int j = 0; j < ingredientsJson.length(); j++) {
                final JSONObject ingredientObject = ingredientsJson.getJSONObject(j);
                ingredientName = ingredientObject.getString(NAME);
                quantity = ingredientObject.getDouble(QUANTITY);
                unit = ingredientObject.getString(UNIT);

                ingredients.add(new Ingredient(ingredientName, quantity, unit));
            }

            mealType = recipeObject.getString(MEAL_TYPE);

            final JSONObject nutritionalValuesObject = recipeObject.getJSONObject(NUTRITIONAL_VALUES);
            nutritionalValues = new HashMap<>();
            for (String nutritionalValue : nutritionalValuesObject.keySet()) {
                nutritionalValues.put(nutritionalValue, nutritionalValuesObject.getDouble(nutritionalValue));
            }

            final Recipe recipe = new Recipe(recipeId, recipeName, recipeImage, ingredients, mealType,
                    nutritionalValues);
            result.add(recipe);
        }

        return result;
    }

    /**
     * Helper method that loads the grocery list of a given user from their associated grocery list JSON.
     * @param username the username of the user to load for
     * @return the user's grocery list
     * @throws RuntimeException if reader throws an IOException when reading the file
     */
    private static GroceryList loadGroceryList(String username) throws RuntimeException {
        final List<Ingredient> items = new ArrayList<>();
        final String filePath = String.format(USER_GROCERY_LIST_PATH, username);
        final File file = new File(filePath);

        if (!file.exists() || file.length() == 0) {
            return new GroceryList(new ArrayList<>());
        }

        // Parameters for each ingredient
        String name;
        double quantity;
        String unit;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            final String groceryListJson = reader.readAllAsString();
            final JSONArray groceryList = new JSONArray(groceryListJson);

            for (int i = 0; i < groceryList.length(); i++) {
                final JSONObject ingredient = groceryList.getJSONObject(i);

                name = ingredient.getString(NAME);
                quantity = ingredient.getDouble(QUANTITY);
                unit = ingredient.getString(UNIT);

                items.add(new Ingredient(name, quantity, unit));
            }

            return new GroceryList(items);

        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Helper method that loads the meal plans of a user from their associated JSON file.
     * @param username the username of the user to load from
     * @return the user's list of meal plans
     * @throws RuntimeException if the reader throws an IOException when reading the file
     */
    private static List<MealPlan> loadMealPlans(String username) throws RuntimeException {
        final List<MealPlan> result = new ArrayList<>();
        final String filePath = String.format(USER_MEAL_PLANS_PATH, username);
        final File file = new File(filePath);

        if (!file.exists() || file.length() == 0) {
            return result;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            final String mealPlanJson = reader.readAllAsString();
            final JSONArray mealPlanArray = new JSONArray(mealPlanJson);

            for (int i = 0; i < mealPlanArray.length(); i++) {
                final JSONObject mealPlan = mealPlanArray.getJSONObject(i);

                final JSONArray recipesArray = mealPlan.getJSONArray(RECIPES);
                final List<Recipe> recipes = jsonToRecipes(recipesArray);

                final double targetCalories = mealPlan.getDouble(TARGET_CALORIES);
                final double targetProtein = mealPlan.getDouble(TARGET_PROTEIN);
                final double targetCarbs = mealPlan.getDouble(TARGET_CARBS);
                final double targetFats = mealPlan.getDouble(TARGET_FATS);

                result.add(new MealPlan(recipes, targetCalories, targetProtein, targetCarbs, targetFats));
            }

            return result;

        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Helper method that ensures that the parent directory of a user's persistent data file exists.
     * @param file the file to check the parent directory for
     * @return whether the parent directory previously existed
     */
    private static boolean ensureDirectoryExists(File file) {
        final File directory = file.getParentFile();
        if (!directory.exists()) {
            return !directory.mkdirs();
        }

        return true;
    }

    @Override
    public Map<String, User> getUsers() {
        return users;
    }

    /**
     * Helper method to create a defensive copy of a recipe (including ingredients and nutritional values)
     * to avoid exposing internal mutable state to callers.
     * @param source the recipe to copy
     * @return a deep copy of the recipe
     */
    private static Recipe copyRecipe(Recipe source) {
        if (source == null) {
            return null;
        }

        List<Ingredient> ingredientsCopy = new ArrayList<>();
        if (source.getIngredients() != null) {
            for (Ingredient ingredient : source.getIngredients()) {
                ingredientsCopy.add(new Ingredient(
                        ingredient.getName(),
                        ingredient.getQuantity(),
                        ingredient.getUnit()
                ));
            }
        }

        Map<String, Double> nutritionCopy = source.getNutritionalValues() != null
                ? new HashMap<>(source.getNutritionalValues())
                : new HashMap<>();

        String recipeName = Optional.ofNullable(source.getRecipeName()).orElse("");
        String recipeImage = Optional.ofNullable(source.getRecipeImage()).orElse("");
        String mealType = Optional.ofNullable(source.getMealType()).orElse("");

        Recipe copy = new Recipe(source.getRecipeId(), recipeName, recipeImage,
                ingredientsCopy, mealType, nutritionCopy);
        copy.setSteps(source.getSteps());
        return copy;
    }

    /**
     * Returns a defensive copy of the liked recipes for the specified username.
     * @param username the user whose liked recipes should be retrieved
     * @return a list of recipes; empty if the user does not exist or has none saved
     */
    @Override
    public List<Recipe> getLikedRecipesForUser(String username) {
        if (username == null || username.trim().isEmpty()) {
            return new ArrayList<>();
        }

        User user = users.get(username);
        if (user == null || user.getSavedRecipes() == null) {
            return new ArrayList<>();
        }

        List<Recipe> copies = new ArrayList<>();
        for (Recipe recipe : user.getSavedRecipes()) {
            Recipe copy = copyRecipe(recipe);
            if (copy != null) {
                copies.add(copy);
            }
        }
        return copies;
    }

    /**
     * Retrieves a liked recipe for the currently logged in user by recipe id.
     * @param recipeId the recipe identifier
     * @return an Optional containing a defensive copy of the recipe if found
     */
    @Override
    public Optional<Recipe> getCurrentUserLikedRecipe(int recipeId) {
        if (currentUsername == null || currentUsername.trim().isEmpty()) {
            return Optional.empty();
        }

        User user = users.get(currentUsername);
        if (user == null || user.getSavedRecipes() == null) {
            return Optional.empty();
        }

        return user.getSavedRecipes().stream()
                .filter(Objects::nonNull)
                .filter(recipe -> recipe.getRecipeId() == recipeId)
                .findFirst()
                .map(FileDataAccessObject::copyRecipe);
    }


    @Override
    public String login(String username, String password) {
        if (!users.containsKey(username)) {
            return LoginUserDataAccessInterface.USER_DNE_ERROR;
        }
        else if (!users.get(username).getPassword().equals(password)) {
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
        final String mealPlanPath = String.format(USER_MEAL_PLANS_PATH, currentUsername);
        saveMealPlans(users.get(currentUsername), mealPlanPath);
    }

    @Override
    public String signupUser(String username, String password) {
        if (users.containsKey(username)) {
            return SignupUserDataAccessInterface.USER_EXISTS_ERROR;
        }
        final User user = new User(username, password, new ArrayList<>(), new ArrayList<>(),
                new GroceryList(new ArrayList<>()));
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
        final User currentUser = users.get(currentUsername);
        currentUser.getMealPlans().remove(index);
        final String jsonPath = String.format(USER_MEAL_PLANS_PATH, currentUsername);
        saveMealPlans(currentUser, jsonPath);
    }

    // ApproveRecipeDataAccessInterface implementation

    /**
     * Get recipes that are pending approval.
     * @return list of recipes waiting to be approved
     */
    @Override
    public List<Recipe> getAvailableRecipes() {
        return new ArrayList<>(pendingApprovalRecipes);
    }

    /**
     * Set recipes that should be available for approval.
     * @param recipes the recipes from recipe generator or search
     */
    public void setAvailableRecipes(List<Recipe> recipes) {
        this.pendingApprovalRecipes = recipes != null ? new ArrayList<>(recipes) : new ArrayList<>();
    }

    @Override
    public void saveLikedRecipe(String username, Recipe recipe) {
        saveRecipeToUser(username, recipe);
    }

    @Override
    public void deleteLikedRecipe(String username, int recipeId) {
        final User user = users.get(username);
        if (user == null) {
            System.err.println("User not found: " + username);
            return;
        }

        user.getSavedRecipes().removeIf(recipe -> recipe.getRecipeId() == recipeId);
        final String jsonPath = String.format(USER_LIKED_RECIPES_PATH, username);
        saveLikedRecipes(users.get(currentUsername), jsonPath);
    }

    @Override
    public List<Recipe> getLikedRecipes(String username) {
        final User user = users.get(username);
        return user != null ? user.getSavedRecipes() : new ArrayList<>();
    }

    @Override
    public List<InstructionStep> getAnalyzedInstructions(int recipeId) {
        return instructionsApi.getAnalyzedInstructions(recipeId, apiKey);
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
     * Save an approved recipe to the user's saved recipes and persist to JSON.
     * @param username the username
     * @param recipe the recipe to save
     */
    @Override
    public void saveRecipeToUser(String username, Recipe recipe) {
        final User user = users.get(username);
        if (user == null) {
            System.err.println("User not found: " + username);
            return;
        }

        // Check if recipe already exists
        final boolean alreadySaved = user.getSavedRecipes().stream()
                .anyMatch(userRecipe -> userRecipe.getRecipeId() == userRecipe.getRecipeId());

        if (!alreadySaved) {
            user.getSavedRecipes().add(recipe);
            // Persist changes to JSON file
            final String jsonPath = String.format(USER_LIKED_RECIPES_PATH, currentUsername);
            saveLikedRecipes(user, jsonPath);
        }

        // Remove from pending approval list
        removeFromPendingApproval(recipe.getRecipeId());
    }

    /**
     * Remove a recipe from the pending approval list (after approve or decline).
     * @param recipeId the ID of the recipe to remove
     */
    public void removeFromPendingApproval(int recipeId) {
        pendingApprovalRecipes.removeIf(recipe -> recipe.getRecipeId() == recipeId);
    }

    @Override
    public void save(List<Ingredient> list) {
        final User user = users.get(currentUsername);

        if (user != null) {
            user.setGroceryList(new GroceryList(list));

            final String jsonPath = String.format(USER_GROCERY_LIST_PATH, currentUsername);
            
            saveGroceryList(user, jsonPath);
        }
    }

    @Override
    public List<Ingredient> load() {
        final User user = users.get(currentUsername);

        if (user != null && user.getGroceryList() != null) {
            return user.getGroceryList().getItems();
        }

        return new ArrayList<>();
    }

    @Override
    public void addIngredientsToGroceryList(String username, List<Ingredient> ingredients) {
        User user = users.get(username);
        if (user == null) {
            System.err.println("User not found: " + username);
            return;
        }

        GroceryList groceryList = user.getGroceryList();
        if (groceryList == null) {
            groceryList = new GroceryList(new ArrayList<>());
            user.setGroceryList(groceryList);
        }

        List<Ingredient> items = groceryList.getItems();


        for (Ingredient incoming : ingredients) {
            boolean merged = false;

            for (int i = 0; i < items.size(); i++) {
                Ingredient existing = items.get(i);

                if (existing.getName().equalsIgnoreCase(incoming.getName())
                        && existing.getUnit().equalsIgnoreCase(incoming.getUnit())) {

                    double newQty = existing.getQuantity() + incoming.getQuantity();
                    items.set(i, new Ingredient(existing.getName(), newQty, existing.getUnit()));
                    merged = true;
                    break;
                }
            }

            if (!merged) {
                items.add(new Ingredient(
                        incoming.getName(),
                        incoming.getQuantity(),
                        incoming.getUnit()
                ));
            }
        }
        String jsonPath = String.format(USER_GROCERY_LIST_PATH, username);
        saveGroceryList(user, jsonPath);
    }
}
