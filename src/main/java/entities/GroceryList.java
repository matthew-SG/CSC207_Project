package entities;

import java.util.ArrayList;
import java.util.List;

public class GroceryList {
    private final List<Ingredient> items;

    public GroceryList() {
        this.items = new ArrayList<>();
    }

    public List<Ingredient> getItems() {
        return items;
    }
}
