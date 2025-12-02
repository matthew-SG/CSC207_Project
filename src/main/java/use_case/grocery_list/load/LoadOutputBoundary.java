package use_case.grocery_list.load;

/**
 * The Output Boundary interface for the "Load Grocery List" use case.
 * This interface is implemented by the presenter.
 * It defines the contract for passing the loaded grocery list data back to the presentation layer.
 */
public interface LoadOutputBoundary {
    /**
     * Prepares the view with the output data resulting from loading the grocery list.
     * This method is called by the interactor after successfully retrieving the list from the data source.
     *
     * @param output The LoadOutputData containing the retrieved list of grocery items.
     */
    void present(LoadOutputData output);
}
