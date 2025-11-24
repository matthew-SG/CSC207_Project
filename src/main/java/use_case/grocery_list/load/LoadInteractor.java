package use_case.grocery_list.load;

import use_case.grocery_list.GroceryRepository;
import java.util.List;

public class LoadInteractor implements LoadInputBoundary {

    private final GroceryRepository repo;
    private final LoadOutputBoundary presenter;

    public LoadInteractor(GroceryRepository repo, LoadOutputBoundary presenter) {
        this.repo = repo;
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        java.util.List<entities.Ingredient> items = repo.load();
        presenter.present(new LoadOutputData(items));
    }
}
