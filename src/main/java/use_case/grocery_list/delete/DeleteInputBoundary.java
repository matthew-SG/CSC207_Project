package use_case.grocery_list.delete;

/**
 * The Input Boundary interface for the "Delete Item from Grocery List" use case.
 * This interface is implemented by the use case interactor.
 * It defines the contract for initiating the process of removing an item.
 */
public interface DeleteInputBoundary {
    /**
     * Executes the "Delete Item" use case logic.
     * The input data contains the necessary identifier of the item to be deleted.
     *
     * @param input The DeleteInputData object containing the item's index in the list.
     */
    void execute(DeleteInputData input);
}
