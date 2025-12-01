package view;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.*;

import interface_adapter.delete_meal_plan.DeleteMealPlanController;
import interface_adapter.load_meal_plan.LoadMealPlanController;
import interface_adapter.view_meal_plans.ViewMealPlansState;
import interface_adapter.view_meal_plans.ViewMealPlansViewModel;

/**
 * The View for when the user wants to view their saved meal plans.
 */
public class ViewMealPlansView extends JPanel implements PropertyChangeListener {

    private static final String VIEW_NAME = "view meal plans";
    private static final int CARD_DIMENSIONS = 10;
    private static final int CARD_LABEL_SPACING = 5;
    private static final int CARD_SPACING = 10;
    private static final int FONT_SIZE = 16;
    private final transient ViewMealPlansViewModel viewMealPlansViewModel;

    private final JLabel onlyOneMealPlanErrorField = new JLabel();

    private transient LoadMealPlanController loadMealPlanController;
    private transient DeleteMealPlanController deleteMealPlanController;

    private JPanel listPanel;

    public ViewMealPlansView(ViewMealPlansViewModel viewMealPlansViewModel) {
        this.viewMealPlansViewModel = viewMealPlansViewModel;
        this.viewMealPlansViewModel.addPropertyChangeListener(this);

        onlyOneMealPlanErrorField.setText(" ");

        setLayout(new BorderLayout());

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        final JScrollPane scrollPane = new JScrollPane(listPanel);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(onlyOneMealPlanErrorField, BorderLayout.NORTH);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final ViewMealPlansState viewMealPlansState = (ViewMealPlansState) evt.getNewValue();

        listPanel.removeAll();

        final List<String> firstRecipeNames = viewMealPlansState.getFirstRecipeNames();
        final List<Double> targetCalories = viewMealPlansState.getTargetCalories();
        final List<Double> targetProtein = viewMealPlansState.getTargetProtein();
        final List<Double> targetCarbs = viewMealPlansState.getTargetCarbs();
        final List<Double> targetFats = viewMealPlansState.getTargetFats();

        for (int i = 0; i < firstRecipeNames.size(); i++) {
            final JPanel card = new JPanel();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBorder(BorderFactory.createEmptyBorder(CARD_DIMENSIONS, CARD_DIMENSIONS, CARD_DIMENSIONS,
                    CARD_DIMENSIONS));

            final JLabel nameLabel = new JLabel(firstRecipeNames.get(i));
            nameLabel.setFont(new Font("Arial", Font.BOLD, FONT_SIZE));

            final JLabel nutrientsLabel = new JLabel(
                    "Target Calories: " + targetCalories.get(i)
                    + " | Target Protein: " + targetProtein.get(i)
                    + " | Target Carbs: " + targetCarbs.get(i)
                    + " | Target Fats: " + targetFats.get(i)
            );

            final JPanel buttons = new JPanel();
            final JButton viewButton = new JButton("View Meal Plan");
            final JButton deleteButton = new JButton("Delete Meal Plan");
            final int index = i;
            viewButton.addActionListener(actionEvent -> loadMealPlanController.execute(index));
            deleteButton.addActionListener(actionEvent -> deleteMealPlanController.execute(index));
            buttons.add(viewButton);
            buttons.add(deleteButton);

            card.setAlignmentX(Component.LEFT_ALIGNMENT);
            nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            nutrientsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            buttons.setAlignmentX(Component.LEFT_ALIGNMENT);
            buttons.setLayout(new FlowLayout(FlowLayout.LEFT));

            card.add(nameLabel);
            card.add(Box.createVerticalStrut(CARD_LABEL_SPACING));
            card.add(nutrientsLabel);
            card.add(Box.createVerticalStrut(CARD_LABEL_SPACING));
            card.add(buttons);

            listPanel.add(card);
            listPanel.add(Box.createVerticalStrut(CARD_SPACING));
        }

        onlyOneMealPlanErrorField.setText(viewMealPlansState.getOneMealPlanError());

        revalidate();
        repaint();
    }

    public String getViewName() {
        return VIEW_NAME;
    }

    public void setLoadMealPlanController(LoadMealPlanController loadMealPlanController) {
        this.loadMealPlanController = loadMealPlanController;
    }

    public void setDeleteMealPlanController(DeleteMealPlanController deleteMealPlanController) {
        this.deleteMealPlanController = deleteMealPlanController;
    }
}
