package use_case.grocery_list.add;

import entities.Ingredient;
import use_case.grocery_list.GroceryRepository;

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
        List<Ingredient> items = repo.load();
        Ingredient g = new Ingredient(input.item, Double.parseDouble(input.qty), input.units);
        items.add(g);
        repo.save(items);
        presenter.present(new AddOutputData(items));
    }
}
