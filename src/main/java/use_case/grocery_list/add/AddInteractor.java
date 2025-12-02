package use_case.grocery_list.add;

import entities.Ingredient;
import use_case.grocery_list.GroceryRepository;

import java.util.List;

/**
 * The Interactor for the "Add Item to Grocery List" use case.
 * It implements the code AddInputBoundary and contains the core business logic
 * for adding a new item, managing persistence via the repository, and
 * notifying the presenter of the successful outcome.
 */
public class AddInteractor implements AddInputBoundary {

    private final GroceryRepository repo;
    private final AddOutputBoundary presenter;

    /**
     * Constructs an AddInteractor.
     *
     * @param repo The data access object for reading and writing the grocery list.
     * @param presenter The output boundary used to notify the presentation layer of the result.
     */
    public AddInteractor(GroceryRepository repo, AddOutputBoundary presenter) {
        this.repo = repo;
        this.presenter = presenter;
    }

    /**
     * Executes the "Add Item" use case.
     * 1. Loads the current list from the repository.
     * 2. Creates a new Ingredient entity from the input data.
     * 3. Adds the new ingredient to the list.
     * 4. Saves the updated list back to the repository.
     * 5. Passes the updated list to the presenter for display.
     *
     * @param input The AddInputData object containing the item details.
     */
    @Override
    public void execute(AddInputData input) {
        List<Ingredient> items = repo.load();
        Ingredient g = new Ingredient(input.item, Double.parseDouble(input.qty), input.units);
        items.add(g);
        repo.save(items);
        presenter.present(new AddOutputData(items));
    }
}
