package use_case.grocery_list.edit;

import entities.GroceryItem;
import java.util.List;

public class EditOutputData {
    public final List<GroceryItem> items;

    public EditOutputData(List<GroceryItem> items) {
        this.items = items;
    }
}
