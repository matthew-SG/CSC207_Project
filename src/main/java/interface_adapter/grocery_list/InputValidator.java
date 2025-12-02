package interface_adapter.grocery_list;

/**
 * A utility class for validating various inputs related to the grocery list feature.
 */
public class InputValidator {
    /**
     * Checks if the given quantity string is a valid positive number.
     * A quantity is considered valid if:
     * 1. It is not empty.
     * 2. It can be successfully parsed as a double.
     * 3. The parsed double value is greater than zero.
     *
     * @param qty The string representation of the quantity to validate.
     * @return true if the quantity is a valid positive number, false otherwise.
     */
    public static boolean isValidQuantity(String qty) {
        if (qty.trim().isEmpty()) {
            return false;
        }

        try {
            double quantity = Double.parseDouble(qty.trim());

            if (quantity <= 0) {
                return false;
            }
            return true;

        } catch (NumberFormatException e) {
            return false;
        }
    }
}
