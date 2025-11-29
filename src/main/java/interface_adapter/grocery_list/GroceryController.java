package interface_adapter.grocery_list;

import use_case.grocery_list.add.AddInputBoundary;
import use_case.grocery_list.add.AddInputData;

import use_case.grocery_list.edit.EditInputBoundary;
import use_case.grocery_list.edit.EditInputData;

import use_case.grocery_list.delete.DeleteInputBoundary;
import use_case.grocery_list.delete.DeleteInputData;

import use_case.grocery_list.load.LoadInputBoundary;

public class GroceryController {

    private final AddInputBoundary addUC;
    private final EditInputBoundary editUC;
    private final DeleteInputBoundary deleteUC;
    private final LoadInputBoundary loadUC;

    public GroceryController(AddInputBoundary addUC,
                             EditInputBoundary editUC,
                             DeleteInputBoundary deleteUC,
                             LoadInputBoundary loadUC) {
        this.addUC = addUC;
        this.editUC = editUC;
        this.deleteUC = deleteUC;
        this.loadUC = loadUC;
    }

    public void load() {
        loadUC.execute();
    }

    public void add(String item, String qty, String units) {
        addUC.execute(new AddInputData(item, qty, units));
    }

    public void edit(int index, String newItem, String newQty, String newUnits) {
        editUC.execute(new EditInputData(index, newItem, newQty, newUnits));
    }

    public void delete(int index) {
        deleteUC.execute(new DeleteInputData(index));
    }
}
