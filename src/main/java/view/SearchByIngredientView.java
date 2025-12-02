package view;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import entities.Ingredient;
import entities.Recipe;
import interface_adapter.search_by_ingr.SearchByIngredientController;
import interface_adapter.search_by_ingr.SearchByIngredientState;
import interface_adapter.search_by_ingr.SearchByIngredientViewModel;

/**
 * Swing view for the Search By Ingredient feature.
 * Lets the user enter ingredients, search for recipes, and view details.
 */
public class SearchByIngredientView extends JPanel implements PropertyChangeListener {

    public static final String VIEWNAME = "Search By Ingredient";

    private final SearchByIngredientController controller;
    private final SearchByIngredientViewModel searchByIngredientViewModel;
    private interface_adapter.approve_recipe.ApproveRecipeController approveRecipeController;
    private interface_adapter.ViewManagerModel viewManagerModel;

    private final JTextField nameField = new JTextField();
    private final JTextField amountField = new JTextField();
    private final JSpinner maxMissingSpinner =
            new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));

    private final DefaultListModel<Ingredient> ingredientModel = new DefaultListModel<>();
    private final JList<Ingredient> ingredientList = new JList<>(ingredientModel);

    private final DefaultListModel<Recipe> recipeModel = new DefaultListModel<>();
    private final JList<Recipe> recipeList = new JList<>(recipeModel);

    private final JLabel statusLabel = new JLabel(" ");

    /**
     * Creates the search-by-ingredient view and wires up listeners and UI.
     *
     * @param viewModel the view model for this feature
     * @param controller the controller used to trigger the use case
     * @param viewManagerModel manages navigation between views
     */
    public SearchByIngredientView(SearchByIngredientViewModel viewModel,
                                  SearchByIngredientController controller,
                                  interface_adapter.ViewManagerModel viewManagerModel) {
        this.searchByIngredientViewModel = viewModel;
        this.controller = controller;
        this.viewManagerModel = viewManagerModel;

        this.searchByIngredientViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout(10, 10));
        JPanel inputRow = new JPanel(new GridLayout(2, 5, 5, 5));
        inputRow.add(new JLabel("Name:"));
        inputRow.add(new JLabel("Amount:"));
        inputRow.add(new JLabel("Unit:"));
        inputRow.add(new JLabel("Up to missing:"));
        inputRow.add(new JLabel(""));

        inputRow.add(nameField);
        inputRow.add(amountField);
        JTextField unitField = new JTextField();
        inputRow.add(unitField);
        inputRow.add(maxMissingSpinner);
        JButton addIngredientBtn = new JButton("Add ingredient");
        inputRow.add(addIngredientBtn);
        add(inputRow, BorderLayout.NORTH);

        ingredientList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ingredientList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                String text = "";
                if (value instanceof Ingredient) {
                    Ingredient ing = (Ingredient) value;
                    text = ing.getQuantity() + " " + ing.getUnit() + " " + ing.getName();
                }
                return super.getListCellRendererComponent(list, text, index,
                        isSelected, cellHasFocus);
            }
        });

        recipeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        recipeList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                String text = "";
                if (value instanceof Recipe) {
                    Recipe r = (Recipe) value;
                    text = r.getRecipeName();
                }
                return super.getListCellRendererComponent(list, text, index,
                        isSelected, cellHasFocus);
            }
        });

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(new TitledBorder("Your ingredients"));
        leftPanel.add(new JScrollPane(ingredientList), BorderLayout.CENTER);
        JButton clearBtn = new JButton("Clear");
        JButton deleteBtn = new JButton("Delete selected");

        JPanel leftBottomButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        leftBottomButtons.add(deleteBtn);
        leftBottomButtons.add(clearBtn);
        leftPanel.add(leftBottomButtons, BorderLayout.SOUTH);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(new TitledBorder("Matching recipes"));
        rightPanel.add(new JScrollPane(recipeList), BorderLayout.CENTER);

        JButton searchBtn = new JButton("Search recipes");
        JButton approveRecipesBtn = new JButton("Approve Recipes");
        JButton detailsBtn = new JButton("Show details");
        JPanel recipeButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        recipeButtonPanel.add(approveRecipesBtn);
        recipeButtonPanel.add(detailsBtn);
        JPanel rightBottom = new JPanel(new BorderLayout());
        rightBottom.add(searchBtn, BorderLayout.NORTH);
        rightBottom.add(recipeButtonPanel, BorderLayout.SOUTH);
        rightPanel.add(rightBottom, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        split.setResizeWeight(0.4);
        add(split, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        bottom.add(statusLabel, BorderLayout.WEST);
        add(bottom, BorderLayout.SOUTH);

        addIngredientBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String amountStr = amountField.getText().trim();

            // unitField is effectively final because we never reassign it
            String unit = unitField.getText().trim();

            if (name.isEmpty()) {
                statusLabel.setText("Ingredient name is required.");
                return;
            }

            double qty = 1;
            if (!amountStr.isEmpty()) {
                try {
                    qty = Double.parseDouble(amountStr);
                    if (qty <= 0) {
                        statusLabel.setText("Amount must be a positive number.");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    statusLabel.setText("Amount must be a number.");
                    return;
                }
            }

            Ingredient ing = new Ingredient(name, qty, unit);
            ingredientModel.addElement(ing);

            nameField.setText("");
            amountField.setText("");
            unitField.setText("");

            statusLabel.setText("Ingredient added.");
        });

        deleteBtn.addActionListener(eDel -> {
            int idx = ingredientList.getSelectedIndex();
            if (idx < 0) {
                statusLabel.setText("Select an ingredient to delete.");
                return;
            }
            ingredientModel.remove(idx);
            statusLabel.setText("Ingredient removed.");
        });

        clearBtn.addActionListener(e -> {
            ingredientModel.clear();
            statusLabel.setText("Ingredients cleared.");
        });

        searchBtn.addActionListener(e -> performSearch());

        approveRecipesBtn.addActionListener(e -> {
            if (recipeModel.isEmpty()) {
                statusLabel.setText("Search for recipes first.");
                return;
            }

            // Navigate to approve recipe view
            if (approveRecipeController != null) {
                approveRecipeController.loadRecipes();
                viewManagerModel.getState().viewName = interface_adapter.approve_recipe.ApproveRecipeViewModel.viewName;
                viewManagerModel.firePropertyChange();
            } else {
                statusLabel.setText("Approve recipe controller not initialized.");
            }
        });

        detailsBtn.addActionListener(e -> viewDetails());
    }

    /**
     * Collects ingredients from the list and triggers a search via the controller.
     */
    private void performSearch() {
        if (ingredientModel.isEmpty()) {
            statusLabel.setText("Add at least one ingredient first.");
            return;
        }

        List<Ingredient> list = new ArrayList<>();
        for (int i = 0; i < ingredientModel.size(); i++) {
            list.add(ingredientModel.get(i));
        }

        int maxMissing = (Integer) maxMissingSpinner.getValue();
        controller.search(list, maxMissing);
    }

    /**
     * Shows a dialog with recipe details, including image, ingredients, and steps.
     */
    private void viewDetails() {
        Recipe selected = recipeList.getSelectedValue();
        if (selected == null) {
            statusLabel.setText("Select a recipe first.");
            return;
        }

        StringBuilder ingText = new StringBuilder();
        for (Ingredient ing : selected.getIngredients()) {
            ingText.append("- ")
                    .append(ing.getQuantity())
                    .append(" ")
                    .append(ing.getUnit())
                    .append(" ")
                    .append(ing.getName())
                    .append("\n");
        }

        JTextArea ingArea = new JTextArea(ingText.toString());
        ingArea.setEditable(false);
        ingArea.setLineWrap(true);
        ingArea.setWrapStyleWord(true);

        JTextArea stepsArea = new JTextArea(selected.getSteps());
        stepsArea.setEditable(false);
        stepsArea.setLineWrap(true);
        stepsArea.setWrapStyleWord(true);

        JLabel imageLabel = new JLabel("Loading image...", SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(300, 200));

        try {
            String imageUrl = selected.getRecipeImage();
            URL url = new URL(imageUrl);
            ImageIcon icon = new ImageIcon(url);
            Image scaled = icon.getImage().getScaledInstance(280, 180, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaled));
            imageLabel.setText("");
        } catch (Exception e) {
            imageLabel.setText("Image not available");
        }

        JPanel top = new JPanel(new BorderLayout());
        top.add(new JLabel("Ingredients:"), BorderLayout.NORTH);
        top.add(new JScrollPane(ingArea), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(new JLabel("Steps:"), BorderLayout.NORTH);
        JScrollPane stepsScroll = new JScrollPane(stepsArea);
        stepsScroll.setPreferredSize(new Dimension(500, 350));
        bottom.add(stepsScroll, BorderLayout.CENTER);

        JSplitPane textSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, top, bottom);
        textSplit.setResizeWeight(0.15);

        JPanel content = new JPanel(new BorderLayout(5, 5));
        content.add(imageLabel, BorderLayout.NORTH);
        content.add(textSplit, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(
                this,
                content,
                selected.getRecipeName(),
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * @return the logical name of this view
     */
    public String getViewName() {
        return VIEWNAME;
    }

    /**
     * Sets the approve recipe controller used to navigate to the approve view.
     *
     * @param controller the approve recipe controller
     */
    public void setApproveRecipeController(interface_adapter.approve_recipe.ApproveRecipeController controller) {
        this.approveRecipeController = controller;
    }

    /**
     * Updates the recipe list and status label when the view model changes.
     *
     * @param evt the property change event
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        SearchByIngredientState state = searchByIngredientViewModel.getState();

        recipeModel.clear();
        if (state.getRecipes() != null) {
            for (Recipe r : state.getRecipes()) {
                recipeModel.addElement(r);
            }
        }

        if (state.getErrorMessage() != null) {
            statusLabel.setText(state.getErrorMessage());
        } else if (state.getStatusMessage() != null) {
            statusLabel.setText(state.getStatusMessage());
        } else {
            statusLabel.setText(" ");
        }
    }
}