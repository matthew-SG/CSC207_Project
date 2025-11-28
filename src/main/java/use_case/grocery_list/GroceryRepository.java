package use_case.grocery_list;

import entities.Ingredient;
import java.util.List;

public interface GroceryRepository {
    List<Ingredient> load();
    void save(List<Ingredient> list);
}
