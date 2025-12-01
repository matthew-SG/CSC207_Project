package interface_adapter.grocery_list;

public class InputValidator {
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
