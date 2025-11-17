package view;

import interface_adapter.meal_plan.MealPlanController;
import interface_adapter.meal_plan.MealPlanGeneratorState;
import interface_adapter.meal_plan.MealPlanGeneratorViewModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The View for when the user wants to generate a Meal Plan for the day.
 */
public class MealPlanGeneratorView extends JPanel implements ActionListener, PropertyChangeListener {

    private static final String VIEW_NAME = "meal plan generator";
    private final MealPlanGeneratorViewModel mealPlanGeneratorViewModel;

    private final JTextField targetCaloriesInputField = new JTextField(15);

    private final JTextField targetProteinInputField = new JTextField(15);

    private final JTextField targetCarbsInputField = new JTextField(15);

    private final JTextField targetFatsInputField = new JTextField(15);

    private final JLabel insufficientRecipesErrorField = new JLabel();
    private final JLabel inputErrorField = new JLabel();

    private final JButton generate;
    private MealPlanController mealPlanController = null;

    public MealPlanGeneratorView(MealPlanGeneratorViewModel mealPlanGeneratorViewModel) {

        this.mealPlanGeneratorViewModel = mealPlanGeneratorViewModel;
        this.mealPlanGeneratorViewModel.addPropertyChangeListener(this);

        final JLabel title = new JLabel("Meal Plan Generator");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JPanel targetCaloriesPanel = new JPanel();
        targetCaloriesPanel.add(new JLabel("Input Target Calories:"));
        targetCaloriesPanel.add(targetCaloriesInputField);

        final JPanel targetProteinPanel = new JPanel();
        targetCaloriesPanel.add(new JLabel("Input Target Protein (in grams):"));
        targetCaloriesPanel.add(targetProteinInputField);

        final JPanel targetCarbsPanel = new JPanel();
        targetCaloriesPanel.add(new JLabel("Input Target Carbs (in grams):"));
        targetCaloriesPanel.add(targetCarbsInputField);

        final JPanel targetFatsPanel = new JPanel();
        targetCaloriesPanel.add(new JLabel("Input Target Fats (in grams):"));
        targetCaloriesPanel.add(targetFatsInputField);

        final JPanel buttons = new JPanel();
        generate = new JButton("Generate");
        buttons.add(generate);

        generate.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(generate)) {
                            final MealPlanGeneratorState currentState = mealPlanGeneratorViewModel.getState();

                            mealPlanController.execute(
                                    currentState.getTargetCalories(),
                                    currentState.getTargetProtein(),
                                    currentState.getTargetCarbs(),
                                    currentState.getTargetFats()
                            );
                        }
                    }
                }
        );

        targetCaloriesInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final MealPlanGeneratorState currentState = mealPlanGeneratorViewModel.getState();
                currentState.setTargetCalories(targetCaloriesInputField.getText());
                mealPlanGeneratorViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) { documentListenerHelper(); }

            @Override
            public void removeUpdate(DocumentEvent e) { documentListenerHelper(); }

            @Override
            public void changedUpdate(DocumentEvent e) { documentListenerHelper(); }
        });

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        targetProteinInputField.getDocument().addDocumentListener(new DocumentListener() {
            private void documentListenerHelper() {
                final MealPlanGeneratorState currentState = mealPlanGeneratorViewModel.getState();
                currentState.setTargetProtein(targetProteinInputField.getText());
                mealPlanGeneratorViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) { documentListenerHelper(); }

            @Override
            public void removeUpdate(DocumentEvent e) { documentListenerHelper(); }

            @Override
            public void changedUpdate(DocumentEvent e) { documentListenerHelper(); }
        });

        targetCarbsInputField.getDocument().addDocumentListener(new DocumentListener() {
            private void documentListenerHelper() {
                final MealPlanGeneratorState currentState = mealPlanGeneratorViewModel.getState();
                currentState.setTargetCarbs(targetCarbsInputField.getText());
                mealPlanGeneratorViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) { documentListenerHelper(); }

            @Override
            public void removeUpdate(DocumentEvent e) { documentListenerHelper(); }

            @Override
            public void changedUpdate(DocumentEvent e) { documentListenerHelper(); }
        });

        targetFatsInputField.getDocument().addDocumentListener(new DocumentListener() {
            private void documentListenerHelper() {
                final MealPlanGeneratorState currentState = mealPlanGeneratorViewModel.getState();
                currentState.setTargetFats(targetFatsInputField.getText());
                mealPlanGeneratorViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) { documentListenerHelper(); }

            @Override
            public void removeUpdate(DocumentEvent e) { documentListenerHelper(); }

            @Override
            public void changedUpdate(DocumentEvent e) { documentListenerHelper(); }
        });

        this.add(title);
        this.add(targetCaloriesPanel);
        this.add(targetProteinPanel);
        this.add(targetCarbsPanel);
        this.add(targetFatsPanel);
        this.add(insufficientRecipesErrorField);
        this.add(inputErrorField);
        this.add(buttons);
    }

    /**
     * React to a button click that results in evt.
     * @param evt the event to be processed
     */
    public void actionPerformed(ActionEvent evt) { System.out.println("Click" + evt.getActionCommand()); }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final MealPlanGeneratorState state = (MealPlanGeneratorState) evt.getNewValue();
        setFields(state);
        insufficientRecipesErrorField.setText(state.getInsufficientRecipesError());
        inputErrorField.setText(state.getInputsError());
    }

    private void setFields(MealPlanGeneratorState state) {
        targetCaloriesInputField.setText(state.getTargetCalories());
        targetProteinInputField.setText(state.getTargetProtein());
        targetCarbsInputField.setText(state.getTargetCarbs());
        targetFatsInputField.setText(state.getTargetFats());
    }

    public String getViewName() { return VIEW_NAME; }

    public void setMealPlanController(MealPlanController mealPlanController) {
        this.mealPlanController = mealPlanController;
    }
}
