package entities;

public class BasicRecipe {
    private int Recipe_id;
    private String recipe_name;

    public BasicRecipe(int recipe_id, String recipe_name) {
        this.Recipe_id = recipe_id;
        this.recipe_name = recipe_name;
    }

    public int getRecipe_id() { return Recipe_id; }
    public String getRecipe_name() { return recipe_name; }
}
