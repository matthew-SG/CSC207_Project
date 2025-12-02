package interface_adapter.grocery_list;

import entities.Ingredient;

import java.util.List;

/**
 * Represents the mutable state of the Grocery List view model.
 * This class holds the data required to display the current state of the grocery list
 * in the user interface.
 */
public class GroceryState {
    /**
     * The list of ingredients (grocery items) currently in the grocery list.
     */
    public List<Ingredient> items;
}
