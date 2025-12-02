package use_case.grocery_list.delete;

import use_case.grocery_list.GroceryRepository;
import java.util.List;

/**
 * The Interactor for the "Delete Item from Grocery List" use case.
 * It implements the DeleteInputBoundary and contains the business logic
 * for removing an item based on its index, managing persistence via the repository,
 * and notifying the presenter of the outcome.
 */
public class DeleteInteractor implements DeleteInputBoundary {

    private final GroceryRepository repo;
    private final DeleteOutputBoundary presenter;

    /**
     * Constructs a {@code DeleteInteractor}.
     *
     * @param repo The data access object for reading and writing the grocery list.
     * @param presenter The output boundary used to notify the presentation layer of the result.
     */
    public DeleteInteractor(GroceryRepository repo, DeleteOutputBoundary presenter) {
        this.repo = repo;
        this.presenter = presenter;
    }

    /**
     * Executes the "Delete Item" use case.
     * 1. Loads the current list from the repository.
     * 2. Validates the index and removes the item if the index is within bounds.
     * 3. Saves the updated list back to the repository.
     * 4. Passes the final list to the presenter for display.
     *
     * @param input The DeleteInputData object containing the zero-based index of the item to delete.
     */
    @Override
    public void execute(DeleteInputData input) {
        List<entities.Ingredient> items = repo.load();
        if (input.index >= 0 && input.index < items.size()) {
            items.remove(input.index);
            repo.save(items);
        }
        presenter.present(new DeleteOutputData(items));
    }
}
