package use_case.grocery_list.load;

/**
 * The Input Boundary interface for the "Load Grocery List" use case.
 * This interface is implemented by the use case interactor.
 * It defines the contract for initiating the process of loading the saved grocery list data.
 */
public interface LoadInputBoundary {
    /**
     * Executes the "Load Grocery List" use case logic.
     * Since loading typically doesn't require specific user input data,
     * this method takes no arguments.
     */
    void execute();
}
