package use_case.grocery_list.edit;

import entities.Ingredient;

import java.util.List;

/**
 * Data structure that holds the output result of the "Edit Item in Grocery List" use case.
 * It carries the updated state of the grocery list back to the presenter after a successful edit operation.
 */
public class EditOutputData {
    /**
     * The complete and updated list of ingredients after the item was successfully edited.
     */
    public final List<Ingredient> items;

    /**
     * Constructs an EditOutputData object.
     *
     * @param items The updated list of ingredients to be presented to the user.
     */
    public EditOutputData(List<Ingredient> items) {
        this.items = items;
    }
}
