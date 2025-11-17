package entities;

public class GroceryItem {
    private String item;
    private String qty;
    private String units;

    public GroceryItem(String item, String qty, String units) {
        this.item = item;
        this.qty = qty;
        this.units = units;
    }

    public String getItem() { return item; }
    public String getQty() { return qty; }
    public String getUnits() { return units; }

    public void setItem(String item) { this.item = item; }
    public void setQty(String qty) { this.qty = qty; }
    public void setUnits(String units) { this.units = units; }
}
