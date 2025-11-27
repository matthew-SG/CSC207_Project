package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.recipe_generator.RecipeGeneratorViewModel;
import interface_adapter.recipe_generator.RecipeGeneratorController;
import interface_adapter.recipe_generator.RecipeGeneratorState;
import entities.Cuisine;
import entities.DietaryRestriction;
import entities.Intolerance;
import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JList;


public class RecipeGeneratorView extends JPanel implements PropertyChangeListener {

    private final RecipeGeneratorViewModel viewModel;
    private final RecipeGeneratorController controller;
    private final ViewManagerModel viewManagerModel;

    private JComboBox<DietaryRestriction> dietaryRestrictionBox;
    private JList<Intolerance> intolerancesList;
    private JComboBox<Cuisine> cuisineBox;
    private JTextField minCaloriesField;
    private JTextField maxCaloriesField;
    private JTextField minProteinField;
    private JTextField maxProteinField;
    private JButton generateButton;
    private DefaultListModel<String> recipesListModel;
    private JList<String> recipesList;

    private JLabel messageLabel;

    public RecipeGeneratorView(RecipeGeneratorViewModel viewModel,
                               RecipeGeneratorController controller,
                               ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.controller = controller;
        this.viewManagerModel = viewManagerModel;

        setLayout(new BorderLayout());


        this.viewModel.addPropertyChangeListener(this);

        initUI();
    }

    private void initUI() {
        // FILTERS PANEL
        JPanel filtersPanel = new JPanel();
        filtersPanel.setLayout(new GridLayout(3, 2, 5, 5)); // 3 rows, 2 cols, some spacing

        // dietary restriction dropdown
        dietaryRestrictionBox = new JComboBox<>(DietaryRestriction.values());
        filtersPanel.add(new JLabel("Dietary restriction:"));
        filtersPanel.add(dietaryRestrictionBox);

        // cuisine dropdown
        cuisineBox = new JComboBox<>(Cuisine.values());
        filtersPanel.add(new JLabel("Cuisine:"));
        filtersPanel.add(cuisineBox);

        // intolerances multi-select list
        intolerancesList = new JList<>(Intolerance.values());
        intolerancesList.setVisibleRowCount(3);
        intolerancesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane intolerancesScroll = new JScrollPane(intolerancesList);
        filtersPanel.add(new JLabel("Intolerances:"));
        filtersPanel.add(intolerancesScroll);

        // ----- MIDDLE: NUMERIC FILTERS + BUTTON -----
        JPanel numbersAndButtonPanel = new JPanel();
        numbersAndButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        minCaloriesField = new JTextField(6);
        maxCaloriesField = new JTextField(6);
        minProteinField = new JTextField(6);
        maxProteinField = new JTextField(6);

        numbersAndButtonPanel.add(new JLabel("Min Calories:"));
        numbersAndButtonPanel.add(minCaloriesField);
        numbersAndButtonPanel.add(new JLabel("Max calories:"));
        numbersAndButtonPanel.add(maxCaloriesField);
        numbersAndButtonPanel.add(new JLabel("Min protein:"));
        numbersAndButtonPanel.add(minProteinField);
        numbersAndButtonPanel.add(new JLabel("Max protein:"));
        numbersAndButtonPanel.add(maxProteinField);

        generateButton = new JButton("Generate recipes");
        numbersAndButtonPanel.add(generateButton);

        // ----- CENTER: RECIPES LIST -----
        recipesListModel = new DefaultListModel<>();
        recipesList = new JList<>(recipesListModel);
        recipesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane recipesScroll = new JScrollPane(recipesList);

        // ----- BOTTOM: MESSAGE LABEL -----
        messageLabel = new JLabel("");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // ----- ADD TO MAIN PANEL (this) -----
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(filtersPanel, BorderLayout.NORTH);
        topContainer.add(numbersAndButtonPanel, BorderLayout.SOUTH);

        this.add(topContainer, BorderLayout.NORTH);
        this.add(recipesScroll, BorderLayout.CENTER);
        this.add(messageLabel, BorderLayout.SOUTH);

        generateButton.addActionListener(e -> {
            // read current selections
            DietaryRestriction dr =
                    (DietaryRestriction) dietaryRestrictionBox.getSelectedItem();
            Cuisine cu =
                    (Cuisine) cuisineBox.getSelectedItem();
            List<Intolerance> intolerances =
                    intolerancesList.getSelectedValuesList();

            intolerancesList.setSelectionMode(
                    ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
            );


            String minCaloriesText = minCaloriesField.getText();
            String maxCaloriesText = maxCaloriesField.getText();
            String minProteinText = minProteinField.getText();
            String maxProteinText = maxProteinField.getText();

            // call the use case
            controller.generateRecipe(
                    dr,
                    intolerances,
                    cu,
                    minCaloriesText,
                    maxCaloriesText,
                    minProteinText,
                    maxProteinText
            );
        });

    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // We only care about changes to the view-model state
        if (!"state".equals(evt.getPropertyName())) {
            return;
        }

        RecipeGeneratorState state = (RecipeGeneratorState) evt.getNewValue();

        // 1) Update the message label
        String message = state.getMessage();
        if (message == null) {
            message = "";
        }
        messageLabel.setText(message);

        // 2) Update the recipes list
        recipesListModel.clear();
        java.util.List<use_case.recipe_generator.RecipeSummary> recipes = state.getRecipes();
        if (recipes != null) {
            for (use_case.recipe_generator.RecipeSummary r : recipes) {
                recipesListModel.addElement(r.getRecipeName());
            }
        }
    }


    public String getViewName() {
        return viewModel.getViewName();
    }
}
