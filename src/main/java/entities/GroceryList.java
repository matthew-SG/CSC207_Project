package entities;
import java.util.ArrayList;
import java.util.List;

public final class GroceryList {
    private final List<Ingredient> items;

    public GroceryList(List<Ingredient> items) {
        this.items = new ArrayList<>(items);
    }

    public List<Ingredient> getItems() {return items; }
    }