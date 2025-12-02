package use_case.grocery_list.delete;

/**
 * Data structure that holds the input required to delete an item from the grocery list.
 * This class packages the index of the item selected for deletion from the user interface
 * for the Delete use case interactor.
 */
public class DeleteInputData {
    /**
     * The zero-based index of the item within the current grocery list that is targeted for deletion.
     */
    public final int index;

    /**
     * Constructs a DeleteInputData object.
     *
     * @param index The index of the item to be deleted.
     */
    public DeleteInputData(int index) {
        this.index = index;
    }
}
