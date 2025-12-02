package use_case.grocery_list;

import entities.Ingredient;
import java.util.List;

/**
 * The Repository interface for the Grocery List feature.
 * This interface defines the contract for data access operations, abstracting
 * the source and persistence mechanism of the grocery list from the use case interactors.
 */
public interface GroceryRepository {
    /**
     * Retrieves the complete list of ingredients from the data source.
     *
     * @return A List of Ingredient objects representing the current grocery list.
     */
    List<Ingredient> load();

    /**
     * Persists the current state of the grocery list to the data source.
     *
     * @param list The List of Ingredient objects to be saved.
     */
    void save(List<Ingredient> list);
}
