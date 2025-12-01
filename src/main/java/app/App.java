package app;

import data_access.FileDataAccessObject;
import data_access.SpoonacularApproveRecipeDataAccessObject;
import entities.UserFactory;
import use_case.approve_recipe.ApproveRecipeDataAccessInterface;

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
            // Composition root: instantiate DAOs here
            FileDataAccessObject fileDAO = new FileDataAccessObject(
                    "data/users.csv", new UserFactory());
            ApproveRecipeDataAccessInterface approveRecipeDAO = new SpoonacularApproveRecipeDataAccessObject(
                    fileDAO.getUsers());

            new AppBuilder(approveRecipeDAO)
                    .buildMealPlan()
                    .buildNavigation()
                    .buildErrorPopUp()
                    .buildApproveRecipeFeature()
                    .buildRecipeGeneratorFeature()
                    .buildCommunityFeature()
                    .buildGroceryList()
                    .buildAuthFeature()
                    .buildSearchByIngredient()
                    .build();
        });
    }
}