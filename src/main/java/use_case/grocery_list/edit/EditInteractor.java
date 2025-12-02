package use_case.grocery_list.edit;

import entities.Ingredient;
import use_case.grocery_list.GroceryRepository;
import java.util.List;

/**
 * The Interactor for the "Edit Item in Grocery List" use case.
 * It implements the EditInputBoundary and contains the business logic
 * for modifying an item based on its index and new details, managing persistence
 * via the repository, and notifying the presenter of the outcome.
 */
public class EditInteractor implements EditInputBoundary {

    private final GroceryRepository repo;
    private final EditOutputBoundary presenter;

    /**
     * Constructs an EditInteractor.
     *
     * @param repo The data access object for reading and writing the grocery list.
     * @param presenter The output boundary used to notify the presentation layer of the result.
     */
    public EditInteractor(GroceryRepository repo, EditOutputBoundary presenter) {
        this.repo = repo;
        this.presenter = presenter;
    }

    /**
     * Executes the "Edit Item" use case.
     * 1. Loads the current list from the repository.
     * 2. Validates the index. If valid, retrieves the item.
     * 3. Updates the retrieved Ingredient entity's name, quantity, and units using the new input data.
     * 4. Saves the updated list back to the repository.
     * 5. Passes the final list to the presenter for display.
     *
     *
     * @param input The EditInputData object containing the item's index, new name, new quantity string, and new units.
     */
    @Override
    public void execute(EditInputData input) {
        List<Ingredient> items = repo.load();
        if (input.index >= 0 && input.index < items.size()) {
            Ingredient g = items.get(input.index);
            g.setName(input.newItem);
            g.setQuantity(Double.parseDouble(input.newQty));
            g.setUnit(input.newUnits);
            repo.save(items);
        }
        presenter.present(new EditOutputData(items));
    }
}
