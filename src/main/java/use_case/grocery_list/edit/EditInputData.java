package use_case.grocery_list.edit;

/**
 * Data structure that holds the input required to edit an existing item in the grocery list.
 * This class packages the index of the item to be modified, along with its new details,
 * from the user interface for the Edit use case interactor.
 */
public class EditInputData {
    /**
     * The zero-based index of the item in the current grocery list that is targeted for editing.
     */
    public final int index;
    /**
     * The new name for the grocery item.
     */
    public final String newItem;
    /**
     * The new quantity of the item, represented as a string, which needs parsing and validation
     * within the use case.
     */
    public final String newQty;
    /**
     * The new units associated with the quantity.
     */
    public final String newUnits;

    /**
     * Constructs an EditInputData object.
     *
     * @param index    The index of the item to be edited.
     * @param newItem  The new name of the item.
     * @param newQty   The new quantity of the item as a string.
     * @param newUnits The new units for the quantity.
     */
    public EditInputData(int index, String newItem, String newQty, String newUnits) {
        this.index = index;
        this.newItem = newItem;
        this.newQty = newQty;
        this.newUnits = newUnits;
    }
}
