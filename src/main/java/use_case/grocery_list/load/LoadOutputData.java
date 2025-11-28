package use_case.grocery_list.load;

import entities.Ingredient;

import java.util.List;

public class LoadOutputData {
    public final List<Ingredient> items;

    public LoadOutputData(List<Ingredient> items) {
        this.items = items;
    }
}
