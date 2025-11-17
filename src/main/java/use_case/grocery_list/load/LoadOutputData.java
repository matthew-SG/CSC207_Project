package use_case.grocery_list.load;

import entities.GroceryItem;
import java.util.List;

public class LoadOutputData {
    public final List<GroceryItem> items;

    public LoadOutputData(List<GroceryItem> items) {
        this.items = items;
    }
}
