package app;

import javax.swing.*;

/**
 * Main application entry point.
 * Delegates application construction to AppBuilder.
 */
public class App {

    /**
     * Application entry point.
     * Constructs and displays the main application window.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AppBuilder()
                    .buildNavigation()
                    .buildErrorPopUp()
                    .buildApproveRecipeFeature()
                    .buildRecipeGeneratorFeature()
                    .buildCommunityFeature()
                    .buildAuthFeature()
                    .buildMealPlan()
                    .build();
        });
    }
}