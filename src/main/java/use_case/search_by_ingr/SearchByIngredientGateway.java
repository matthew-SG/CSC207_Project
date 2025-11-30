package use_case.search_by_ingr;

import entities.Ingredient;
import org.json.JSONObject;

import java.util.List;

public interface SearchByIngredientGateway {

    JSONObject searchByIngredients(List<Ingredient> ingredients);

}