package use_case.grocery_list.add;

import use_case.grocery_list.GroceryRepository;
import entities.GroceryItem;
import java.util.List;

public class AddInteractor implements AddInputBoundary {

    private final GroceryRepository repo;
    private final AddOutputBoundary presenter;

    public AddInteractor(GroceryRepository repo, AddOutputBoundary presenter) {
        this.repo = repo;
        this.presenter = presenter;
    }

    @Override
    public void execute(AddInputData input) {
        List<GroceryItem> items = repo.load();
        GroceryItem g = new GroceryItem(input.item, input.qty, input.units);
        items.add(g);
        repo.save(items);
        presenter.present(new AddOutputData(items));
    }
}
