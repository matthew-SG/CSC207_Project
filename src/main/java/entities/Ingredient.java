package entities;

// Entity that stores the information of a singular ingredient inside a recipe
public class Ingredient {
    private String name;
    private double quantity;
    private String unit;

    public Ingredient(String name, double quantity, String unit) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setName(String item) { this.name = item; }
    public void setQuantity(double qty) { this.quantity = qty; }
    public void setUnit(String units) { this.unit = units; }
}
