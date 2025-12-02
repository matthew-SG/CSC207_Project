package interface_adapter.grocery_list;

import use_case.grocery_list.add.AddInputBoundary;
import use_case.grocery_list.add.AddInputData;

import use_case.grocery_list.edit.EditInputBoundary;
import use_case.grocery_list.edit.EditInputData;

import use_case.grocery_list.delete.DeleteInputBoundary;
import use_case.grocery_list.delete.DeleteInputData;

import use_case.grocery_list.load.LoadInputBoundary;

/**
 * The Controller for managing grocery list operations.
 */
public class GroceryController {

    private final AddInputBoundary addUC;
    private final EditInputBoundary editUC;
    private final DeleteInputBoundary deleteUC;
    private final LoadInputBoundary loadUC;

    /**
     * Constructs a GroceryController with necessary use case interactors.
     *
     * @param addUC    The input boundary for the Add Grocery Item use case.
     * @param editUC   The input boundary for the Edit Grocery Item use case.
     * @param deleteUC The input boundary for the Delete Grocery Item use case.
     * @param loadUC   The input boundary for the Load Grocery List use case.
     */
    public GroceryController(AddInputBoundary addUC,
                             EditInputBoundary editUC,
                             DeleteInputBoundary deleteUC,
                             LoadInputBoundary loadUC) {
        this.addUC = addUC;
        this.editUC = editUC;
        this.deleteUC = deleteUC;
        this.loadUC = loadUC;
    }

    /**
     * Executes the Load Grocery List use case.
     */
    public void load() {
        loadUC.execute();
    }

    /**
     * Executes the Add Grocery Item use case.
     *
     * @param item  The name of the item to add.
     * @param qty   The quantity of the item as a string (will be parsed in the use case).
     * @param units The units for the quantity (e.g., "g", "kg", "pcs").
     */
    public void add(String item, String qty, String units) {
        addUC.execute(new AddInputData(item, qty, units));
    }

    /**
     * Executes the Edit Grocery Item use case.
     *
     * @param index    The index of the item in the current list to be edited.
     * @param newItem  The new name of the item.
     * @param newQty   The new quantity of the item as a string.
     * @param newUnits The new units for the quantity.
     */
    public void edit(int index, String newItem, String newQty, String newUnits) {
        editUC.execute(new EditInputData(index, newItem, newQty, newUnits));
    }

    /**
     * Executes the Delete Grocery Item use case.
     *
     * @param index The index of the item in the current list to be deleted.
     */
    public void delete(int index) {
        deleteUC.execute(new DeleteInputData(index));
    }
}
