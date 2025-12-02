package use_case.grocery_list.add;

/**
 * The Output Boundary interface for the "Add Item to Grocery List" use case.
 * This interface is implemented by the presenter.
 * It defines the contract for passing the result of the add operation back to the presentation layer.
 */
public interface AddOutputBoundary {
    /**
     * Prepares the view with the output data resulting from adding an item.
     * This method is called by the interactor after successfully updating the list.
     *
     * @param output The AddOutputData containing the updated list of grocery items.
     */
    void present(AddOutputData output);
}
