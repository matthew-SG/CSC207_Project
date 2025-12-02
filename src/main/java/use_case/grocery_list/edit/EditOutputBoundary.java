package use_case.grocery_list.edit;

/**
 * The Output Boundary interface for the "Edit Item in Grocery List" use case.
 * This interface is implemented by the presenter.
 * It defines the contract for passing the result of the edit operation back to the presentation layer.
 */
public interface EditOutputBoundary {
    /**
     * Prepares the view with the output data resulting from editing an item.
     * This method is called by the interactor after successfully updating the list.
     *
     * @param output The EditOutputData containing the updated list of grocery items.
     */
    void present(EditOutputData output);
}
