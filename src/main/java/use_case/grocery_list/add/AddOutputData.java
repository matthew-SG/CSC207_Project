package use_case.grocery_list.add;

import entities.Ingredient;

import java.util.List;

public class AddOutputData {
    public final List<Ingredient> items;

    public AddOutputData(List<Ingredient> items) {
        this.items = items;
    }
}
