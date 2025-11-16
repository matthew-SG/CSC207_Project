package view;

import interface_adapter.community.CommunityController;
import interface_adapter.community.CommunityState;
import interface_adapter.community.CommunityViewModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * The View for selecting a liked recipe to review.
 * This view displays a list of the user's liked recipes and allows them
 * to select one to write a review for.
 */
public class SelectLikedRecipeView extends JPanel implements PropertyChangeListener {
    private final String viewName = CommunityViewModel.SELECTING_RECIPE;
    private final CommunityViewModel communityViewModel;
    private CommunityController communityController;

    private final JPanel recipeListPanel;
    private final JScrollPane scrollPane;
    private final JLabel titleLabel;

    public SelectLikedRecipeView(CommunityViewModel communityViewModel) {
        this.communityViewModel = communityViewModel;
        this.communityViewModel.addPropertyChangeListener(this);

        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title
        titleLabel = new JLabel("Select a Recipe to Review", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        this.add(titleLabel, BorderLayout.NORTH);

        // Recipe list panel
        recipeListPanel = new JPanel();
        recipeListPanel.setLayout(new BoxLayout(recipeListPanel, BoxLayout.Y_AXIS));
        recipeListPanel.setBackground(Color.WHITE);

        // Scroll pane for recipes
        scrollPane = new JScrollPane(recipeListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.add(scrollPane, BorderLayout.CENTER);

        // Initial empty state
        displayEmptyState();
    }

    private void displayEmptyState() {
        recipeListPanel.removeAll();
        JLabel emptyLabel = new JLabel("No liked recipes available.", SwingConstants.CENTER);
        emptyLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        emptyLabel.setForeground(Color.GRAY);
        emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        recipeListPanel.add(Box.createVerticalGlue());
        recipeListPanel.add(emptyLabel);
        recipeListPanel.add(Box.createVerticalGlue());
        recipeListPanel.revalidate();
        recipeListPanel.repaint();
    }

    private void displayRecipes(List<Integer> recipeIds, List<String> recipeNames, List<String> recipeImages) {
        recipeListPanel.removeAll();

        if (recipeNames == null || recipeNames.isEmpty()) {
            displayEmptyState();
            return;
        }

        for (int i = 0; i < recipeNames.size(); i++) {
            final int recipeId = (recipeIds != null && i < recipeIds.size()) ? recipeIds.get(i) : -1;
            String recipeName = recipeNames.get(i);
            String recipeImage = (recipeImages != null && i < recipeImages.size()) ? recipeImages.get(i) : null;

            JPanel recipeCard = createRecipeCard(recipeId, recipeName, recipeImage);
            recipeListPanel.add(recipeCard);
            recipeListPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacing between cards
        }

        recipeListPanel.revalidate();
        recipeListPanel.repaint();
    }

    private JPanel createRecipeCard(final int recipeId, String recipeName, String recipeImageUrl) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(15, 15, 15, 15)
        ));
        card.setBackground(Color.WHITE);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        // Recipe name label
        JLabel nameLabel = new JLabel(recipeName);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        card.add(nameLabel, BorderLayout.CENTER);

        // Review button
        JButton reviewButton = new JButton("Write Review");
        reviewButton.setFont(new Font("Arial", Font.PLAIN, 14));
        reviewButton.setPreferredSize(new Dimension(150, 35));
        reviewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (communityController != null) {
                    communityController.selectRecipe(recipeId);
                }
            }
        });
        card.add(reviewButton, BorderLayout.EAST);

        return card;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            final CommunityState state = (CommunityState) evt.getNewValue();
            
            // Only update if this is the recipe selection state
            if (CommunityViewModel.SELECTING_RECIPE.equals(state.getSubviewName())) {
                updateView(state);
            }
        }
    }

    private void updateView(CommunityState state) {
        List<Integer> recipeIds = state.getRecipeIds();
        List<String> recipeNames = state.getRecipeNames();
        List<String> recipeImages = state.getRecipeImages();
        
        displayRecipes(recipeIds, recipeNames, recipeImages);
    }

    public String getViewName() {
        return viewName;
    }

    public void setCommunityController(CommunityController communityController) {
        this.communityController = communityController;
    }
}
