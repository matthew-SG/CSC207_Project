package use_case.search_by_ingr;
import entities.Ingredient;

import java.util.*;

public class SearchByIngredientInputData {
    private final List<Ingredient> INGREDIENTS;
    public SearchByIngredientInputData(List<Ingredient> ingredients) {
        this.INGREDIENTS = ingredients;
    }
    public List<Ingredient> getIngredients() {
        return INGREDIENTS;
    }
}
