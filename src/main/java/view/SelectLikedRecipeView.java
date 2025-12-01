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

        // Top panel with title and cancel button
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));

        titleLabel = new JLabel("Select a Recipe to Review", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.PLAIN, 14));
        cancelButton.setPreferredSize(new Dimension(100, 35));
        cancelButton.addActionListener(e -> {
            if (communityController != null) {
                communityController.viewCommunity();
            }
        });
        JPanel cancelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cancelPanel.add(cancelButton);
        topPanel.add(cancelPanel, BorderLayout.WEST);

        this.add(topPanel, BorderLayout.NORTH);

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
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        // Image panel (leftmost)
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new BorderLayout());
        imagePanel.setBackground(Color.WHITE);
        imagePanel.setPreferredSize(new Dimension(100, 100));
        
        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(100, 100));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        
        if (recipeImageUrl != null && !recipeImageUrl.isEmpty()) {
            // Load image asynchronously
            loadImageAsync(recipeImageUrl, imageLabel, 100, 100);
        } else {
            // Placeholder if no image
            imageLabel.setText("No Image");
            imageLabel.setForeground(Color.GRAY);
        }
        
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        card.add(imagePanel, BorderLayout.WEST);

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
                    communityController.selectRecipe(recipeId,
                            recipeName, recipeImageUrl);
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

    /**
     * Loads an image asynchronously from a URL and sets it to the label.
     * Scales the image to fit within the specified dimensions.
     */
    private void loadImageAsync(String imageUrl, JLabel label, int width, int height) {
        // Use SwingWorker to load image in background
        SwingWorker<ImageIcon, Void> worker = new SwingWorker<ImageIcon, Void>() {
            @Override
            protected ImageIcon doInBackground() {
                try {
                    java.net.URL url = new java.net.URL(imageUrl);
                    ImageIcon icon = new ImageIcon(url);
                    
                    // Scale image to fit
                    Image img = icon.getImage();
                    Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                    return new ImageIcon(scaledImg);
                } catch (Exception e) {
                    System.err.println("Error loading image: " + imageUrl);
                    return null;
                }
            }

            @Override
            protected void done() {
                try {
                    ImageIcon icon = get();
                    if (icon != null) {
                        label.setIcon(icon);
                        label.setText("");
                    } else {
                        label.setText("Image Error");
                        label.setForeground(Color.RED);
                    }
                } catch (Exception e) {
                    label.setText("Image Error");
                    label.setForeground(Color.RED);
                }
            }
        };
        worker.execute();
    }
}
