package entities;

/**
 * Unit converter entity.
 */
public class UnitConverter {

    /**
     * Converts tablespoons into the specified unit.
     * @param amount the amount in tablespoons to be converted
     * @param unitName the unit to be converted to
     * @return the amount in the converted unit
     */
    public static double fromTbsp(double amount, String unitName) {
        final int tspConversion = 3;
        final int mlConversion = 15;
        final int cupsConversion = 16;
        final String unit = unitName.toLowerCase();
        double result = amount;
        if (!unit.isEmpty()) {
            result = switch (unit) {
                case "tbsp", "tablespoon" -> amount;
                case "tsp", "teaspoon" -> amount * tspConversion;
                case "ml", "milliliter" -> amount * mlConversion;
                case "cup", "cups" -> amount / cupsConversion;
                default -> amount;
            };
        }

        return result;
    }

    /**
     * Converts a specified unit into tablespoons.
     * @param amount the amount of the original unit to be converted
     * @param unitName the original unit
     * @return the amount of the original unit in tablespoons
     */
    public static double toTbsp(double amount, String unitName) {
        final String unit = unitName.toLowerCase();
        final int tspConversion = 3;
        final int mlConversion = 15;
        final int cupsConversion = 16;

        double result = amount;
        if (!unit.isEmpty()) {
            result = switch (unit) {
                case "tbsp", "tablespoon" -> amount;
                case "tsp", "teaspoon" -> amount / tspConversion;
                case "ml", "milliliter" -> amount / mlConversion;
                case "cup", "cups" -> amount * cupsConversion;
                default -> amount;
            };
        }

        return result;
    }
}
