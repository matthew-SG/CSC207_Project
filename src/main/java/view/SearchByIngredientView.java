package view;

import API.SearchByIngredientSpoonacular;
import entities.*;
import interface_adapter.search_by_ingr.*;
import use_case.search_by_ingr.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class SearchByIngredientView extends JPanel {

    public static final String VIEWNAME = "search by ingredient";

    private final SearchByIngredientController controller;
    private final SearchByIngredientSpoonacular api;
    private final JTextField nameField = new JTextField();
    private final JTextField amountField = new JTextField();
    private final JTextField unitField = new JTextField();

    private final DefaultListModel<Ingredient> ingredientModel = new DefaultListModel<>();
    private final JList<Ingredient> ingredientList = new JList<>(ingredientModel);

    private final DefaultListModel<Recipe> recipeModel = new DefaultListModel<>();
    private final JList<Recipe> recipeList = new JList<>(recipeModel);

    private final JLabel statusLabel = new JLabel(" ");

    public SearchByIngredientView(SearchByIngredientController controller,
                                  SearchByIngredientSpoonacular api) {
        this.controller = controller;
        this.api = api;

        setLayout(new BorderLayout(10, 10));
        JPanel inputRow = new JPanel(new GridLayout(2, 4, 5, 5));
        inputRow.add(new JLabel("Name:"));
        inputRow.add(new JLabel("Amount:"));
        inputRow.add(new JLabel("Unit:"));
        inputRow.add(new JLabel(""));
        inputRow.add(nameField);
        inputRow.add(amountField);
        inputRow.add(unitField);
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
        leftPanel.add(clearBtn, BorderLayout.SOUTH);
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(new TitledBorder("Matching recipes"));
        rightPanel.add(new JScrollPane(recipeList), BorderLayout.CENTER);
        JButton searchBtn = new JButton("Search recipes");
        rightPanel.add(searchBtn, BorderLayout.SOUTH);
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        split.setResizeWeight(0.4);
        add(split, BorderLayout.CENTER);
        JPanel bottom = new JPanel(new BorderLayout());
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        bottom.add(statusLabel, BorderLayout.WEST);
        JButton detailsBtn = new JButton("View details");
        bottom.add(detailsBtn, BorderLayout.EAST);
        add(bottom, BorderLayout.SOUTH);
        addIngredientBtn.addActionListener(e -> addIngredient());
        clearBtn.addActionListener(e -> clearIngredients());
        searchBtn.addActionListener(e -> performSearch());
        detailsBtn.addActionListener(e -> viewDetails());
    }
    private void addIngredient() {
        String name = nameField.getText().trim();
        String amountStr = amountField.getText().trim();
        String unit = unitField.getText().trim();

        if (name.isEmpty()) {
            statusLabel.setText("Ingredient name is required.");
            return;
        }

        double qty = 0;
        if (!amountStr.isEmpty()) {
            try {
                qty = Double.parseDouble(amountStr);
            } catch (NumberFormatException e) {
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
    }

    private void clearIngredients() {
        ingredientModel.clear();
        statusLabel.setText("Ingredients cleared.");
    }

    private void performSearch() {
        if (ingredientModel.isEmpty()) {
            statusLabel.setText("Add at least one ingredient first.");
            return;
        }

        List<Ingredient> list = new ArrayList<>();
        for (int i = 0; i < ingredientModel.size(); i++) {
            list.add(ingredientModel.get(i));
        }
        SearchByIngredientOutputData out = controller.search(list);
        recipeModel.clear();
        for (Recipe r : out.getRecipes()) {
            recipeModel.addElement(r);
        }
        statusLabel.setText(out.getMsg());
    }

    private void viewDetails() {
        Recipe selected = recipeList.getSelectedValue();
        if (selected == null) {
            statusLabel.setText("Select a recipe first.");
            return;
        }
        api.populateRecipeDetails(selected);
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
        JPanel top = new JPanel(new BorderLayout());
        top.add(new JLabel("Ingredients:"), BorderLayout.NORTH);
        top.add(new JScrollPane(ingArea), BorderLayout.CENTER);
        JPanel bot = new JPanel(new BorderLayout());
        bot.add(new JLabel("Steps:"), BorderLayout.NORTH);
        bot.add(new JScrollPane(stepsArea), BorderLayout.CENTER);
        JSplitPane popup = new JSplitPane(JSplitPane.VERTICAL_SPLIT, top, bot);
        popup.setResizeWeight(0.3);
        popup.setPreferredSize(new Dimension(500, 400));
        JOptionPane.showMessageDialog(
                this,
                popup,
                selected.getRecipeName(),
                JOptionPane.INFORMATION_MESSAGE
        );
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            String apiKey = "";
            SearchByIngredientSpoonacular api = new SearchByIngredientSpoonacular(apiKey);
            SearchByIngredientInputBoundary interactor = new SearchByIngredientInteractor(api);
            SearchByIngredientController controller = new SearchByIngredientController(interactor);
            SearchByIngredientView view = new SearchByIngredientView(controller, api);
            JFrame frame = new JFrame("Search By Ingredient");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLayout(new BorderLayout());
            frame.add(view, BorderLayout.CENTER);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
