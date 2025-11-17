package use_case.grocery_list.add;

public class AddInputData {
    public final String item;
    public final String qty;
    public final String units;

    public AddInputData(String item, String qty, String units) {
        this.item = item;
        this.qty = qty;
        this.units = units;
    }
}
