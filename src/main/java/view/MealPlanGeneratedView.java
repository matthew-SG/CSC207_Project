package view;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.swing.*;

import interface_adapter.meal_plan.MealPlanGeneratedState;
import interface_adapter.meal_plan.MealPlanGeneratedViewModel;

/**
 * The view for the generated Meal Plan.
 */
public class MealPlanGeneratedView extends JPanel implements PropertyChangeListener {
    
    private static final String VIEW_NAME = "meal plan generated";
    private final transient MealPlanGeneratedViewModel mealPlanGeneratedViewModel;

    // --- Card Placeholders ---
    // These panels will hold the recipe details.
    private final JPanel firstMealCard = new JPanel();
    private final JPanel secondMealCard = new JPanel();
    private final JPanel thirdMealCard = new JPanel();

    public MealPlanGeneratedView(MealPlanGeneratedViewModel mealPlanGeneratedViewModel) {
        this.mealPlanGeneratedViewModel = mealPlanGeneratedViewModel;
        this.mealPlanGeneratedViewModel.addPropertyChangeListener(this);
        
        final int viewSize = 10;
        final int componentHeight = 20;
        
        // Set the layout for the MealPlanGeneratedView panel
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        // Add padding to the entire view
        this.setBorder(BorderFactory.createEmptyBorder(viewSize, viewSize, viewSize, viewSize));

        final JLabel title = new JLabel("Your Generated Meal Plan");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Spacing
        // Add Components to the View
        this.add(title);
        this.add(Box.createVerticalStrut(componentHeight));

        // First Meal Section
        final JLabel firstMealLabel = new JLabel("First Meal");
        firstMealLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(firstMealLabel);
        // Add the empty card panel
        this.add(firstMealCard);
        // Spacing
        this.add(Box.createVerticalStrut(componentHeight));

        // Second Meal Section
        final JLabel secondMealLabel = new JLabel("Second Meal");
        secondMealLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(secondMealLabel);
        // Add the empty card panel
        this.add(secondMealCard);
        // Spacing
        this.add(Box.createVerticalStrut(componentHeight));

        // Third Meal Section
        final JLabel thirdMealLabel = new JLabel("Third Meal");
        thirdMealLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(thirdMealLabel);
        // Add the empty card panel
        this.add(thirdMealCard);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final MealPlanGeneratedState state = (MealPlanGeneratedState) evt.getNewValue();
        final int mealPlanSize = 3;

        // Assuming your original data structure:
        final String[] recipeNames = state.getRecipeNames();
        final String[] recipeImages = state.getRecipeImages();
        final List<List<String[]>> recipeIngredients = state.getRecipeIngredients();
        final List<Map<String, Double>> recipeNutritionalValues = state.getRecipeNutritionalValues();

        // Build and update each recipe card
        // Check if data exists (to avoid IndexOutOfBoundsException)
        if (recipeNames != null && recipeNames.length >= mealPlanSize) {

            buildRecipeCard(firstMealCard, recipeNames[0], recipeImages[0],
                    recipeIngredients.get(0), recipeNutritionalValues.get(0));

            buildRecipeCard(secondMealCard, recipeNames[1], recipeImages[1],
                    recipeIngredients.get(1), recipeNutritionalValues.get(1));

            buildRecipeCard(thirdMealCard, recipeNames[2], recipeImages[2],
                    recipeIngredients.get(2), recipeNutritionalValues.get(2));
        }
    }

    /**
     * Helper method that builds/modifies a recipe card on the Meal Plan Generator View (upon a property change).
     * @param cardPanel the recipe panel to be modified
     * @param name the name of the recipe
     * @param imageUrl the url to the image of the recipe
     * @param ingredients the ingredients of the recipe
     * @param nutrition the nutritional components of the recipe
     */
    private void buildRecipeCard(JPanel cardPanel, String name, String imageUrl,
                                 List<String[]> ingredients, Map<String, Double> nutrition) {

        cardPanel.removeAll();

        final int cardGap = 10;
        final int imageDimensions = 150;

        // Gaps
        cardPanel.setLayout(new BorderLayout(cardGap, cardGap));
        cardPanel.setBorder(BorderFactory.createRaisedBevelBorder());

        final JLabel nameLabel = new JLabel(name, SwingConstants.CENTER);
        cardPanel.add(nameLabel, BorderLayout.NORTH);

        final JLabel imageLabel = new JLabel("[Loading...]", SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(imageDimensions, imageDimensions));
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        try {
            final URL url = new URL(imageUrl);
            final ImageIcon icon = new ImageIcon(url);
            final Image scaledImg = icon.getImage().getScaledInstance(imageDimensions, imageDimensions,
                    Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImg));
            imageLabel.setText(null);
        }
        catch (MalformedURLException ex) {
            imageLabel.setText("[Image Failed]");
        }

        cardPanel.add(imageLabel, BorderLayout.WEST);

        // Panel that holds the details of the related recipe
        final JPanel detailsPanel = new JPanel(new GridLayout(1, 2, 10, 0));

        // Left side of details: Scrollable panel that holds all the ingredients of the related recipe
        final JTextArea ingredientsArea = new JTextArea();
        ingredientsArea.setEditable(false);
        ingredientsArea.append("Ingredients:\n\n");
        for (String[] ingredient : ingredients) {
            ingredientsArea.append("• " + ingredient[1] + " " + ingredient[2] + " " + ingredient[0] + "\n");
        }
        final JScrollPane ingredientsScroll = new JScrollPane(ingredientsArea);

        // Right side of details: Scrollable panel that holds all the nutrients of the related recipe
        final JTextArea nutritionArea = new JTextArea();
        nutritionArea.setEditable(false);
        nutritionArea.append("Nutritional Information:\n\n");

        for (Map.Entry<String, Double> entry : nutrition.entrySet()) {
            nutritionArea.append(entry.getKey() + ": " + entry.getValue() + "\n");
        }

        final JScrollPane nutritionScroll = new JScrollPane(nutritionArea);

        // Add both sides
        detailsPanel.add(ingredientsScroll);
        detailsPanel.add(nutritionScroll);

        // Add combined panel to UI
        cardPanel.add(detailsPanel, BorderLayout.CENTER);

        cardPanel.revalidate();
        cardPanel.repaint();
    }

    public String getViewName() {
        return VIEW_NAME;
    }
}

