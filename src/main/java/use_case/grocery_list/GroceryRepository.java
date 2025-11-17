package use_case.grocery_list;

import entities.GroceryItem;
import java.util.List;

public interface GroceryRepository {
    List<GroceryItem> load();
    void save(List<GroceryItem> list);
}
