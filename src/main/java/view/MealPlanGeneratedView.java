package view;

import interface_adapter.meal_plan.MealPlanController;
import interface_adapter.meal_plan.MealPlanGeneratedState;
import interface_adapter.meal_plan.MealPlanGeneratedViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.Map;
import java.util.List;

/**
 * The view for the generated Meal Plan
 */
public class MealPlanGeneratedView extends JPanel implements PropertyChangeListener {

    private static final String VIEW_NAME = "meal plan generated view";
    private final MealPlanGeneratedViewModel mealPlanGeneratedViewModel;

    // --- Card Placeholders ---
    // These panels will hold the recipe details.
    private final JPanel firstMealCard = new JPanel();
    private final JPanel secondMealCard = new JPanel();
    private final JPanel thirdMealCard = new JPanel();

    public MealPlanGeneratedView(MealPlanGeneratedViewModel mealPlanGeneratedViewModel) {
        this.mealPlanGeneratedViewModel = mealPlanGeneratedViewModel;
        this.mealPlanGeneratedViewModel.addPropertyChangeListener(this);

        // --- Main Layout ---
        // Set the layout for the *entire* MealPlanGeneratedView panel
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        final JLabel title = new JLabel("Your Generated Meal Plan");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- Add Components to the View ---
        this.add(title);
        this.add(Box.createVerticalStrut(20)); // Spacing

        // First Meal Section
        JLabel firstMealLabel = new JLabel("First Meal");
        firstMealLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(firstMealLabel);
        this.add(firstMealCard); // Add the empty card panel
        this.add(Box.createVerticalStrut(20)); // Spacing

        // Second Meal Section
        JLabel secondMealLabel = new JLabel("Second Meal");
        secondMealLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(secondMealLabel);
        this.add(secondMealCard); // Add the empty card panel
        this.add(Box.createVerticalStrut(20)); // Spacing

        // Third Meal Section
        JLabel thirdMealLabel = new JLabel("Third Meal");
        thirdMealLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(thirdMealLabel);
        this.add(thirdMealCard); // Add the empty card panel
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        MealPlanGeneratedState state = (MealPlanGeneratedState) evt.getNewValue();

        // Assuming your original data structure:
        String[] recipeNames = state.getRecipeNames();
        String[] recipeImages = state.getRecipeImages();
        List<List<String[]>> recipeIngredients = state.getRecipeIngredients();
        List<Map<String, Double>> recipeNutritionalValues = state.getRecipeNutritionalValues();

        // --- Build/Update the Cards ---
        // Check if data exists (to avoid IndexOutOfBoundsException)
        if (recipeNames != null && recipeNames.length >= 3) {

            buildRecipeCard(firstMealCard, recipeNames[0], recipeImages[0],
                    recipeIngredients.get(0), recipeNutritionalValues.get(0));

            buildRecipeCard(secondMealCard, recipeNames[1], recipeImages[1],
                    recipeIngredients.get(1), recipeNutritionalValues.get(1));

            buildRecipeCard(thirdMealCard, recipeNames[2], recipeImages[2],
                    recipeIngredients.get(2), recipeNutritionalValues.get(2));
        }
    }

    /**
     * Helper method that builds/modifies a recipe card on the Meal Plan Generator View (upon a property change)
     * @param cardPanel the recipe panel to be modified
     * @param name the name of the recipe
     * @param imageUrl the url to the image of the recipe
     * @param ingredients the ingredients of the recipe
     * @param nutrition the nutritional components of the recipe
     */
    private void buildRecipeCard(JPanel cardPanel, String name, String imageUrl,
                                 List<String[]> ingredients, Map<String, Double> nutrition) {

        cardPanel.removeAll();

        cardPanel.setLayout(new BorderLayout(10, 10)); // Gaps
        cardPanel.setBorder(BorderFactory.createRaisedBevelBorder());

        JLabel nameLabel = new JLabel(name, SwingConstants.CENTER);
        cardPanel.add(nameLabel, BorderLayout.NORTH);

        JLabel imageLabel = new JLabel("[Loading...]", SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(150, 150));
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        try {
            URL url = new URL(imageUrl);
            ImageIcon icon = new ImageIcon(url);
            Image scaledImg = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImg));
            imageLabel.setText(null);
        } catch (Exception e) {
            imageLabel.setText("[Image Failed]");
        }

        cardPanel.add(imageLabel, BorderLayout.WEST);

        JPanel detailsPanel = new JPanel(new BorderLayout());

        JTextArea ingredientsArea = new JTextArea();
        ingredientsArea.setEditable(false);
        ingredientsArea.append("Ingredients:\n");
        for (String[] ingredient : ingredients) {
            ingredientsArea.append("• " + ingredient[1] + " " + ingredient[2] + " " + ingredient[0] + "\n");
        }
        detailsPanel.add(new JScrollPane(ingredientsArea), BorderLayout.CENTER);

        JPanel nutritionPanel = new JPanel(new FlowLayout());
        Double calories = nutrition.getOrDefault("Calories", 0.0);
        Double protein = nutrition.getOrDefault("Protein", 0.0);
        Double carbs = nutrition.getOrDefault("Carbohydrates", 0.0);
        Double fats = nutrition.getOrDefault("Fat", 0.0);
        nutritionPanel.add(new JLabel(String.format("Cals: %.1f", calories)));
        nutritionPanel.add(new JLabel(String.format("Protein: %.1f g", protein)));
        nutritionPanel.add(new JLabel(String.format("Carbs: %.1f g", carbs)));
        nutritionPanel.add(new JLabel(String.format("Fats: %.1f g", fats)));
        detailsPanel.add(nutritionPanel, BorderLayout.SOUTH);

        cardPanel.add(detailsPanel, BorderLayout.CENTER);

        cardPanel.revalidate();
        cardPanel.repaint();
    }

    public String getViewName() { return VIEW_NAME; }
}

