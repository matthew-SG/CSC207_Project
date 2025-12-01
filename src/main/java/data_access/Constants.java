package data_access;

public class Constants {
    // Constants for Community Use Case
    public static String WEBAPI_KEY = "AIzaSyDRtQtANy7zkSqpmO9CXQDiLFl7cU5uLeU";
    public static String AUTH_ENDPOINT = "https://identitytoolkit.googleapis.com/v1/"
            + "accounts:signInWithPassword?key=AIzaSyDRtQtANy7zkSqpmO9CXQDiLFl7cU5uLeU&";

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private Constants() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    // Constants for Spoonacular API
    public static final String SPOONACULAR_API_KEY = "5b07df6820b74cf1b2eae9c1b440f014";
    public static final String SPOONACULAR_BASE_URL = "https://api.spoonacular.com/recipes";
    public static final String SPOONACULAR_SEARCH_KEY = "c175c29f3c7a45fab7d5357d0f9ed25e";

    // Constants for File DAO
    public static final String HEADER = "username,password";
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
    public static final String USER_MEAL_PLANS_PATH = "data/%s/meal_plans.json";
    public static final String USER_LIKED_RECIPES_PATH = "data/%s/liked_recipes.json";
    public static final String USER_GROCERY_LIST_PATH = "data/%s/grocery_list.json";
}
