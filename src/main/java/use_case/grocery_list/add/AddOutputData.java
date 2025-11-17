package use_case.grocery_list.add;

import entities.GroceryItem;
import java.util.List;

public class AddOutputData {
    public final List<GroceryItem> items;

    public AddOutputData(List<GroceryItem> items) {
        this.items = items;
    }
}
