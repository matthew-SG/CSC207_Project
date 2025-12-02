package use_case.grocery_list.load;

import entities.Ingredient;

import java.util.List;

/**
 * Data structure that holds the output result of the "Load Grocery List" use case.
 * It carries the retrieved list of grocery items back to the presenter for display.
 */
public class LoadOutputData {
    /**
     * The complete list of ingredients retrieved from the data source.
     */
    public final List<Ingredient> items;

    /**
     * Constructs a LoadOutputData object.
     *
     * @param items The loaded list of ingredients to be presented to the user.
     */
    public LoadOutputData(List<Ingredient> items) {
        this.items = items;
    }
}
