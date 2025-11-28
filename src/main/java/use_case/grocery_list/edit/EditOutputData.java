package use_case.grocery_list.edit;

import entities.Ingredient;

import java.util.List;

public class EditOutputData {
    public final List<Ingredient> items;

    public EditOutputData(List<Ingredient> items) {
        this.items = items;
    }
}
