package entities;

import java.util.ArrayList;
import java.util.List;

public class GroceryList {
    private List<GroceryItem> items;

    public GroceryList() {
        this.items = new ArrayList<>();
    }

    public List<GroceryItem> getItems() {
        return items;
    }
}
