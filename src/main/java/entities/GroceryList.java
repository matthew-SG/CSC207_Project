package entities;

import java.util.ArrayList;
import java.util.List;

public final class GroceryList {
    private final ArrayList<Ingredient> items;

    public GroceryList(List<Ingredient> items) {
        this.items = items;
    }

    public List<Ingredient> getItems() {return items; }
    }
