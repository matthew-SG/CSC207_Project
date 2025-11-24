package use_case.grocery_list.delete;

import entities.Ingredient;

import java.util.List;

public class DeleteOutputData {
    public final List<Ingredient> items;

    public DeleteOutputData(List<Ingredient> items) {
        this.items = items;
    }
}
