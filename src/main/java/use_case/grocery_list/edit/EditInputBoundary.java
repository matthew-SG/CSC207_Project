package use_case.grocery_list.edit;

/**
 * The Input Boundary interface for the "Edit Item in Grocery List" use case.
 * This interface is implemented by the use case interactor.
 * It defines the contract for initiating the process of modifying an existing grocery item.
 */
public interface EditInputBoundary {
    /**
     * Executes the "Edit Item" use case logic.
     * The input data contains the index of the item to be edited and the new details.
     *
     * @param input The EditInputData object containing the item's index and new values.
     */
    void execute(EditInputData input);
}
