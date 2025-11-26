package view;

import interface_adapter.load_meal_plan.LoadMealPlanController;
import interface_adapter.view_meal_plans.ViewMealPlansState;
import interface_adapter.view_meal_plans.ViewMealPlansViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * The View for when the user wants to view their saved meal plans
 */
public class ViewMealPlansView extends JPanel implements PropertyChangeListener {

    private static final String VIEW_NAME = "view meal plans";
    private final ViewMealPlansViewModel viewMealPlansViewModel;

    private static final int CARD_DIMENSIONS = 10;
    private static final int CARD_LABEL_SPACING = 5;
    private static final int CARD_SPACING = 10;

    private LoadMealPlanController loadMealPlanController = null;

    private JPanel listPanel;

    public ViewMealPlansView(ViewMealPlansViewModel viewMealPlansViewModel) {
        this.viewMealPlansViewModel = viewMealPlansViewModel;
        this.viewMealPlansViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(listPanel);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        ViewMealPlansState viewMealPlansState  = (ViewMealPlansState) evt.getNewValue();

        listPanel.removeAll();

        final List<String> firstRecipeNames = viewMealPlansState.getFirstRecipeNames();
        final List<Double> targetCalories = viewMealPlansState.getTargetCalories();
        final List<Double> targetProtein = viewMealPlansState.getTargetProtein();
        final List<Double> targetCarbs = viewMealPlansState.getTargetCarbs();
        final List<Double> targetFats = viewMealPlansState.getTargetFats();

        for (int i = 0; i < firstRecipeNames.size(); i++) {
            JPanel card = new JPanel();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBorder(BorderFactory.createEmptyBorder(CARD_DIMENSIONS, CARD_DIMENSIONS, CARD_DIMENSIONS,
                    CARD_DIMENSIONS));

            JLabel nameLabel = new JLabel(firstRecipeNames.get(i));
            nameLabel.setFont(new Font("Arial", Font.BOLD, 16));

            JLabel nutrientsLabel = new JLabel(
                    "Target Calories: " + targetCalories.get(i)
                    + " | Target Protein: " + targetProtein.get(i)
                    + " | Target Carbs: " + targetCarbs.get(i)
                    + " | Target Fats: " + targetFats.get(i)
            );

            JButton viewButton = new JButton("View Meal Plan");
            int index = i;
            viewButton.addActionListener(e -> loadMealPlanController.execute(index));

            card.add(nameLabel);
            card.add(Box.createVerticalStrut(CARD_LABEL_SPACING));
            card.add(nutrientsLabel);
            card.add(Box.createVerticalStrut(CARD_LABEL_SPACING));
            card.add(viewButton);

            listPanel.add(card);
            listPanel.add(Box.createVerticalStrut(CARD_SPACING));
        }

        listPanel.revalidate();
        listPanel.repaint();
    }

    public String getViewName() {
        return VIEW_NAME;
    }

    public void setLoadMealPlanController(LoadMealPlanController loadMealPlanController) {
        this.loadMealPlanController = loadMealPlanController;
    }
}
