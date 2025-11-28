package use_case.grocery_list.delete;

import use_case.grocery_list.GroceryRepository;
import java.util.List;

public class DeleteInteractor implements DeleteInputBoundary {

    private final GroceryRepository repo;
    private final DeleteOutputBoundary presenter;

    public DeleteInteractor(GroceryRepository repo, DeleteOutputBoundary presenter) {
        this.repo = repo;
        this.presenter = presenter;
    }

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
