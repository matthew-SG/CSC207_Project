package use_case.grocery_list.add;

/**
 * The Input Boundary interface for the "Add Item to Grocery List" use case.
 * This interface is implemented by the use case interactor.
 * It defines the contract for initiating the process of adding a new item.
 */
public interface AddInputBoundary {
    /**
     * Executes the "Add Item" use case logic.
     * The input data contains the details of the item to be added to the grocery list.
     *
     * @param input The AddInputData object containing the item name, quantity, and units.
     */
    void execute(AddInputData input);
}
