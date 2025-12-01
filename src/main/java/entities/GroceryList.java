package entities;

import java.util.List;

/**
 * Class representing the Grocery List entity.
 */
public final class GroceryList {
    private final List<Ingredient> items;

    public GroceryList(List<Ingredient> items) {
        this.items = items;
    }

    public List<Ingredient> getItems() {
        return items;
    }

}
