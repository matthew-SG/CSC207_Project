package use_case.grocery_list.edit;

import use_case.grocery_list.GroceryRepository;
import java.util.List;
import entities.GroceryItem;

public class EditInteractor implements EditInputBoundary {

    private final GroceryRepository repo;
    private final EditOutputBoundary presenter;

    public EditInteractor(GroceryRepository repo, EditOutputBoundary presenter) {
        this.repo = repo;
        this.presenter = presenter;
    }

    @Override
    public void execute(EditInputData input) {
        List<GroceryItem> items = repo.load();
        if (input.index >= 0 && input.index < items.size()) {
            GroceryItem g = items.get(input.index);
            g.setItem(input.newItem);
            g.setQty(input.newQty);
            g.setUnits(input.newUnits);
            repo.save(items);
        }
        presenter.present(new EditOutputData(items));
    }
}
