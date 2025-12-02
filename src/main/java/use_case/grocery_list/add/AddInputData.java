package use_case.grocery_list.add;

/**
 * Data structure that holds the input required to add a new item to the grocery list.
 * This class packages the raw input data from the user interface for the Add use case interactor.
 */
public class AddInputData {
    /**
     * The name of the item to be added to the grocery list.
     */
    public final String item;
    /**
     * The quantity of the item, represented as a string, which needs parsing and validation
     * within the use case.
     */
    public final String qty;
    /**
     * The units associated with the quantity.
     */
    public final String units;

    /**
     * Constructs an AddInputData object.
     *
     * @param item  The name of the item.
     * @param qty   The quantity of the item as a string.
     * @param units The units for the quantity.
     */
    public AddInputData(String item, String qty, String units) {
        this.item = item;
        this.qty = qty;
        this.units = units;
    }
}
