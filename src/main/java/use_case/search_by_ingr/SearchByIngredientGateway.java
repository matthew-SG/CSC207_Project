package use_case.search_by_ingr;

import java.util.List;

import org.json.JSONObject;

import entities.Ingredient;


public interface SearchByIngredientGateway {

    JSONObject searchByIngredients(List<Ingredient> ingredients);

}