package data_access;

import entities.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.login.LoginUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

import java.io.*;
import java.util.*;

/**
 * DAO for all data, mainly user data, using a File to persist the data
 */
public class FileDataAccessObject implements UserDataAccess {

    // Constants for key values in the various arrays (also header for the users csv)
    private static final String HEADER = "username,password";
    public static final String RECIPE_ID = "recipeId";
    public static final String RECIPE_NAME = "recipeName";
    public static final String RECIPE_IMAGE = "recipeImage";
    public static final String NAME = "name";
    public static final String QUANTITY = "quantity";
    public static final String UNIT = "unit";
    public static final String INGREDIENTS = "ingredients";
    public static final String MEAL_TYPE = "mealType";
    public static final String NUTRITIONAL_VALUES = "nutritionalValues";
    public static final String RECIPES = "recipes";
    public static final String TARGET_CALORIES = "targetCalories";
    public static final String TARGET_PROTEIN = "targetProtein";
    public static final String TARGET_CARBS = "targetCarbs";
    public static final String TARGET_FATS = "targetFats";


    private final File usersCsv;
    private final Map<String, Integer> headers = new LinkedHashMap<>();
    private final Map<String, User> users = new HashMap<>();

    private String currentUsername;

    public FileDataAccessObject(String csvPath, UserFactory userFactory) {

        usersCsv = new File(csvPath);
        headers.put("username", 0);
        headers.put("password", 1);

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

    private void save() {
        final BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(usersCsv));
            writer.write(String.join(",", headers.keySet()));
            writer.newLine();

            for (User user : users.values()) {
                final String likedRecipesPath = String.format("data\\%s\\liked_recipes.json", user.getUsername());
                final String mealPlansPath = String.format("data\\%s\\meal_plans.json", user.getUsername());
                final String groceryListPath = String.format("data\\%s\\grocery_list.json", user.getUsername());

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

    private static void saveLikedRecipes(User user, String jsonPath) {
        File file = new File(jsonPath);
        ensureDirectoryExists(file);

        final List<Recipe> likedRecipes = user.getSavedRecipes();
        JSONArray recipesArray = recipesToJson(likedRecipes);

        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(recipesArray.toString(4));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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
            recipeJson.put(MEAL_TYPE,recipe.getMealType());

            JSONObject nutritionalValues = new JSONObject();
            for (String nutritionalValue : recipe.getNutritionalValues().keySet()) {
                nutritionalValues.put(nutritionalValue, recipe.getNutritionalValues().get(nutritionalValue));
            }
            recipeJson.put(NUTRITIONAL_VALUES, nutritionalValues);

            result.put(recipeJson);
        }
        return result;
    }

    private static void saveMealPlans(User user, String jsonPath) {
        File file = new File(jsonPath);

        final List<MealPlan> mealPlans = user.getMealPlans();
        JSONArray mealPlansArray = new JSONArray();
        ensureDirectoryExists(file);

        for (MealPlan mealPlan : mealPlans) {
            JSONObject mealPlanObject = new JSONObject();

            JSONArray recipes = recipesToJson(mealPlan.getRecipes());
            mealPlanObject.put(RECIPES,recipes);
            mealPlanObject.put(TARGET_CALORIES,mealPlan.getTargetCalories());
            mealPlanObject.put(TARGET_PROTEIN,mealPlan.getTargetProtein());
            mealPlanObject.put(TARGET_CARBS,mealPlan.getTargetCarbs());
            mealPlanObject.put(TARGET_FATS,mealPlan.getTargetFats());

            mealPlansArray.put(mealPlanObject);
        }

        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(mealPlansArray.toString(4));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void saveGroceryList(User user, String jsonPath) {
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
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(groceryListArray.toString(4));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Recipe> loadLikedRecipes(String username) {
        String filePath = String.format("data\\%s\\liked_recipes.json", username);
        File file = new File(filePath);

        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }

        try (final BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String likedRecipesJson = reader.readAllAsString();
            JSONArray likedRecipesArray = new JSONArray(likedRecipesJson);

            return jsonToRecipes(likedRecipesArray);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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

    private static GroceryList loadGroceryList(String username) {
        List<Ingredient> items = new ArrayList<>();
        String filePath = String.format("data\\%s\\grocery_list.json", username);
        File file = new File(filePath);

        if (!file.exists() || file.length() == 0) {
            return new GroceryList(new ArrayList<>());
        }

        // Parameters for each ingredient
        String name;
        double quantity;
        String unit;

        try (final BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String groceryListJson = reader.readAllAsString();
            JSONArray groceryList = new JSONArray(groceryListJson);

            for (int i = 0; i < groceryList.length(); i++) {
                JSONObject ingredient =  groceryList.getJSONObject(i);

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

    private static List<MealPlan> loadMealPlans(String username) {
        List<MealPlan> result = new ArrayList<>();
        String filePath = String.format("data\\%s\\meal_plans.json", username);
        File file = new File(filePath);

        if (!file.exists() || file.length() == 0) {
            return result;
        }

        try (final BufferedReader reader = new BufferedReader(new FileReader(file))) {
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

    }

    @Override
    public List<Recipe> getSavedRecipes() {
        return users.get(currentUsername).getSavedRecipes();
    }

    @Override
    public void saveMealPlan(MealPlan mealPlan) {
        users.get(currentUsername).saveMealPlan(mealPlan);
        save();
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
}
