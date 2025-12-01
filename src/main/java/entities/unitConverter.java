package entities;

public class unitConverter {
    public static double fromTbsp(double amount,String unitName) {
        unitName = unitName.toLowerCase();
        if (unitName==null || unitName.equals("")) return amount;
        return switch (unitName) {
            case "tbsp", "tablespoon" -> amount;
            case "tsp", "teaspoon" -> amount * 3;
            case "ml", "milliliter" -> amount * 15;
            case "cup", "cups" -> amount / 16;
            default -> amount;
        };
    }
    public static double toTbsp(double amount,String unitName) {
        unitName = unitName.toLowerCase();
        if (unitName==null || unitName.equals("")) return amount;
        return switch (unitName) {
            case "tbsp", "tablespoon" -> amount;
            case "tsp", "teaspoon" -> amount / 3;
            case "ml", "milliliter" -> amount / 15;
            case "cup", "cups" -> amount * 16;
            default -> amount;
        };
    }
}
