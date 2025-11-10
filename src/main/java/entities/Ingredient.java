package entities;

// Entity that stores the information of a singular ingredient inside a recipe
public class Ingredient {
    private final String name;
    private final double quantity;
    private final String unit;

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
}
