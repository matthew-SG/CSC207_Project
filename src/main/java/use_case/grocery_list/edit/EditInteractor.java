package use_case.grocery_list.edit;

import entities.Ingredient;
import use_case.grocery_list.GroceryRepository;
import java.util.List;

public class EditInteractor implements EditInputBoundary {

    private final GroceryRepository repo;
    private final EditOutputBoundary presenter;

    public EditInteractor(GroceryRepository repo, EditOutputBoundary presenter) {
        this.repo = repo;
        this.presenter = presenter;
    }

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
