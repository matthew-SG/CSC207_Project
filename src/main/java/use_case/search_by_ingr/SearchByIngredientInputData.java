package use_case.search_by_ingr;
import entities.Ingredient;

import java.util.*;

public class SearchByIngredientInputData {
    private final List<Ingredient> INGREDIENTS;
    private final int AMOUNTMISSING;
    public SearchByIngredientInputData(List<Ingredient> ingredients, int amountMissing) {
        this.INGREDIENTS = ingredients;
        this.AMOUNTMISSING = amountMissing;
    }
    public List<Ingredient> getIngredients() {

        return INGREDIENTS;
    }
    public int getAmountMissing() {
        return AMOUNTMISSING;
    }
}
