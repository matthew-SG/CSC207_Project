package use_case.grocery_list.load;

import use_case.grocery_list.GroceryRepository;
import java.util.List;

/**
 * The Interactor for the "Load Grocery List" use case.
 * It implements the LoadInputBoundary and contains the core business logic
 * for fetching the existing grocery list from the repository and notifying the presenter
 * of the outcome.
 */
public class LoadInteractor implements LoadInputBoundary {

    private final GroceryRepository repo;
    private final LoadOutputBoundary presenter;

    /**
     * Constructs a LoadInteractor.
     *
     * @param repo The data access object responsible for retrieving the grocery list data.
     * @param presenter The output boundary used to notify the presentation layer of the loaded data.
     */
    public LoadInteractor(GroceryRepository repo, LoadOutputBoundary presenter) {
        this.repo = repo;
        this.presenter = presenter;
    }

    /**
     * Executes the "Load Grocery List" use case.
     * 1. Fetches the complete list of ingredients from the repository using repo.load().
     * 2. Passes the retrieved list to the presenter via LoadOutputData for display.
     */
    @Override
    public void execute() {
        java.util.List<entities.Ingredient> items = repo.load();
        presenter.present(new LoadOutputData(items));
    }
}
