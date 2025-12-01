package view;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import interface_adapter.meal_plan.MealPlanController;
import interface_adapter.meal_plan.MealPlanGeneratorState;
import interface_adapter.meal_plan.MealPlanGeneratorViewModel;
import interface_adapter.view_meal_plans.ViewMealPlansController;

/**
 * The View for when the user wants to generate a Meal Plan for the day.
 */
public class MealPlanGeneratorView extends JPanel implements PropertyChangeListener {

    private static final String VIEW_NAME = "meal plan generator";
    private final transient MealPlanGeneratorViewModel mealPlanGeneratorViewModel;

    private final JTextField targetCaloriesInputField = new JTextField(15);

    private final JTextField targetProteinInputField = new JTextField(15);

    private final JTextField targetCarbsInputField = new JTextField(15);

    private final JTextField targetFatsInputField = new JTextField(15);

    private final JLabel insufficientRecipesErrorField = new JLabel();
    private final JLabel inputErrorField = new JLabel();
    private final JLabel noMealPlansErrorField = new JLabel();

    private final JButton viewMealPlans;
    private final JButton generate;
    private MealPlanController mealPlanController;
    private ViewMealPlansController viewMealPlansController;

    public MealPlanGeneratorView(MealPlanGeneratorViewModel mealPlanGeneratorViewModel) {

        this.mealPlanGeneratorViewModel = mealPlanGeneratorViewModel;
        this.mealPlanGeneratorViewModel.addPropertyChangeListener(this);

        final JLabel title = new JLabel("Meal Plan Generator");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JPanel targetCaloriesPanel = new JPanel();
        targetCaloriesPanel.add(new JLabel("Input Target Calories:"));
        targetCaloriesPanel.add(targetCaloriesInputField);

        final JPanel targetProteinPanel = new JPanel();
        targetProteinPanel.add(new JLabel("Input Target Protein (in grams):"));
        targetProteinPanel.add(targetProteinInputField);

        final JPanel targetCarbsPanel = new JPanel();
        targetCarbsPanel.add(new JLabel("Input Target Carbs (in grams):"));
        targetCarbsPanel.add(targetCarbsInputField);

        final JPanel targetFatsPanel = new JPanel();
        targetFatsPanel.add(new JLabel("Input Target Fats (in grams):"));
        targetFatsPanel.add(targetFatsInputField);

        final JPanel buttons = new JPanel();
        viewMealPlans = new JButton("View Saved Meal Plans");
        buttons.add(viewMealPlans);
        generate = new JButton("Generate");
        buttons.add(generate);

        generate.addActionListener(
                evt -> {
                    final MealPlanGeneratorState currentState = mealPlanGeneratorViewModel.getState();

                    mealPlanController.execute(
                            currentState.getTargetCalories(),
                            currentState.getTargetProtein(),
                            currentState.getTargetCarbs(),
                            currentState.getTargetFats()
                    );
                }
        );

        viewMealPlans.addActionListener(
                evt -> viewMealPlansController.execute()
        );

        targetCaloriesInputField.getDocument().addDocumentListener(new DocumentListener() {
            private void documentListenerHelper() {
                final MealPlanGeneratorState currentState = mealPlanGeneratorViewModel.getState();
                currentState.setTargetCalories(targetCaloriesInputField.getText());
                mealPlanGeneratorViewModel.setState(currentState);
            }

            @Override public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
        });

        this.setLayout(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        targetCaloriesPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        targetProteinPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        targetCarbsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        targetFatsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        insufficientRecipesErrorField.setAlignmentX(Component.CENTER_ALIGNMENT);
        inputErrorField.setAlignmentX(Component.CENTER_ALIGNMENT);
        noMealPlansErrorField.setAlignmentX(Component.CENTER_ALIGNMENT);

        targetProteinInputField.getDocument().addDocumentListener(new DocumentListener() {
            private void documentListenerHelper() {
                final MealPlanGeneratorState currentState = mealPlanGeneratorViewModel.getState();
                currentState.setTargetProtein(targetProteinInputField.getText());
                mealPlanGeneratorViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
        });

        targetCarbsInputField.getDocument().addDocumentListener(new DocumentListener() {
            private void documentListenerHelper() {
                final MealPlanGeneratorState currentState = mealPlanGeneratorViewModel.getState();
                currentState.setTargetCarbs(targetCarbsInputField.getText());
                mealPlanGeneratorViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
        });

        targetFatsInputField.getDocument().addDocumentListener(new DocumentListener() {
            private void documentListenerHelper() {
                final MealPlanGeneratorState currentState = mealPlanGeneratorViewModel.getState();
                currentState.setTargetFats(targetFatsInputField.getText());
                mealPlanGeneratorViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
        });

        final JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.add(title);
        formPanel.add(targetCaloriesPanel);
        formPanel.add(targetProteinPanel);
        formPanel.add(targetCarbsPanel);
        formPanel.add(targetFatsPanel);
        formPanel.add(buttons);
        formPanel.add(insufficientRecipesErrorField);
        formPanel.add(inputErrorField);
        formPanel.add(noMealPlansErrorField);

        this.add(formPanel, gbc);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final MealPlanGeneratorState state = (MealPlanGeneratorState) evt.getNewValue();
        setFields(state);
        insufficientRecipesErrorField.setText(state.getInsufficientRecipesError());
        inputErrorField.setText(state.getInputsError());
        noMealPlansErrorField.setText(state.getNoMealPlansError());
    }

    private void setFields(MealPlanGeneratorState state) {
        targetCaloriesInputField.setText(state.getTargetCalories());
        targetProteinInputField.setText(state.getTargetProtein());
        targetCarbsInputField.setText(state.getTargetCarbs());
        targetFatsInputField.setText(state.getTargetFats());
    }

    public static String getViewName() {
        return VIEW_NAME;
    }

    public void setMealPlanController(MealPlanController mealPlanController) {
        this.mealPlanController = mealPlanController;
    }

    public void setViewMealPlansController(ViewMealPlansController viewMealPlansController) {
        this.viewMealPlansController = viewMealPlansController;
    }
}
