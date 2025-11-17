package use_case.grocery_list.delete;

import entities.GroceryItem;
import java.util.List;

public class DeleteOutputData {
    public final List<GroceryItem> items;

    public DeleteOutputData(List<GroceryItem> items) {
        this.items = items;
    }
}
