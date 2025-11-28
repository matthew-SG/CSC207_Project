package use_case.grocery_list.edit;

public class EditInputData {
    public final int index;
    public final String newItem;
    public final String newQty;
    public final String newUnits;

    public EditInputData(int index, String newItem, String newQty, String newUnits) {
        this.index = index;
        this.newItem = newItem;
        this.newQty = newQty;
        this.newUnits = newUnits;
    }
}
