package use_case.grocery_list.delete;

/**
 * The Output Boundary interface for the "Delete Item from Grocery List" use case.
 * This interface is implemented by the presenter.
 * It defines the contract for passing the result of the delete operation back to the presentation layer.
 */
public interface DeleteOutputBoundary {
    /**
     * Prepares the view with the output data resulting from deleting an item.
     * This method is called by the interactor after attempting to update the list.
     *
     * @param output The DeleteOutputData containing the updated list of grocery items.
     */
    void present(DeleteOutputData output);
}
