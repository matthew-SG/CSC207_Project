package use_case.grocery_list.add;

import entities.Ingredient;

import java.util.List;

/**
 * Data structure that holds the output result of the "Add Item to Grocery List" use case.
 * It carries the updated state of the grocery list back to the presenter.
 */
public class AddOutputData {
    /**
     * The complete and updated list of ingredients after the new item was successfully added.
     */
    public final List<Ingredient> items;

    /**
     * Constructs an AddOutputData object.
     *
     * @param items The updated list of ingredients to be presented to the user.
     */
    public AddOutputData(List<Ingredient> items) {
        this.items = items;
    }
}
