package use_case.likedRecipeList;

import entities.Recipe;
import java.util.ArrayList;

public record LikedRecipeOutputData(ArrayList<Recipe> recipes, String message, boolean success) {

    public ArrayList<Recipe> getUpdatedList() {return recipes;}
    public String getMessage() { return message; }
    public boolean isSuccess() { return success; }
}
