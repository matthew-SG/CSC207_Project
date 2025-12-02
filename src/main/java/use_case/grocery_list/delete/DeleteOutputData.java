package use_case.grocery_list.delete;

import entities.Ingredient;

import java.util.List;

/**
 * Data structure that holds the output result of the "Delete Item from Grocery List" use case.
 * It carries the updated state of the grocery list back to the presenter after a deletion attempt.
 */
public class DeleteOutputData {
    /**
     * The complete and updated list of ingredients after the delete operation was executed.
     * This list reflects the current state of the grocery list, whether the deletion was successful or not.
     */
    public final List<Ingredient> items;

    /**
     * Constructs a DeleteOutputData object.
     *
     * @param items The updated list of ingredients to be presented to the user.
     */
    public DeleteOutputData(List<Ingredient> items) {
        this.items = items;
    }
}
