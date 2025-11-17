package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.approve_recipe.ApproveRecipeController;
import interface_adapter.approve_recipe.ApproveRecipeState;
import interface_adapter.approve_recipe.ApproveRecipeViewModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * View for approving or declining recipes.
 * Shows recipe images with like/dislike buttons.
 */
public class ApproveRecipeView extends JPanel implements PropertyChangeListener {
    private final String viewName = ApproveRecipeViewModel.viewName;
    private final ApproveRecipeViewModel approveRecipeViewModel;
    private final ViewManagerModel viewManagerModel;
    private ApproveRecipeController approveRecipeController;

    private final JLabel titleLabel;
    private final JLabel recipeNameLabel;
    private final JLabel recipeImageLabel;
    private final JButton likeButton;
    private final JButton dislikeButton;
    private final JLabel statusLabel;

    private int currentRecipeId = -1;

    public ApproveRecipeView(ApproveRecipeViewModel approveRecipeViewModel, ViewManagerModel viewManagerModel) {
        this.approveRecipeViewModel = approveRecipeViewModel;
        this.viewManagerModel = viewManagerModel;
        this.approveRecipeViewModel.addPropertyChangeListener(this);

        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title
        titleLabel = new JLabel("Recipe Approval", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        this.add(titleLabel, BorderLayout.NORTH);

        // Center panel with recipe info
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Recipe name
        recipeNameLabel = new JLabel("No recipe selected", SwingConstants.CENTER);
        recipeNameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        recipeNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(recipeNameLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Recipe image
        recipeImageLabel = new JLabel("Loading image...", SwingConstants.CENTER);
        recipeImageLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        recipeImageLabel.setPreferredSize(new Dimension(400, 300));
        recipeImageLabel.setMinimumSize(new Dimension(400, 300));
        recipeImageLabel.setMaximumSize(new Dimension(400, 300));
        recipeImageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        recipeImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        recipeImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        recipeImageLabel.setVerticalAlignment(SwingConstants.CENTER);
        recipeImageLabel.setBackground(Color.WHITE);
        recipeImageLabel.setOpaque(true);
        centerPanel.add(recipeImageLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Status label
        statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(statusLabel);

        this.add(centerPanel, BorderLayout.CENTER);

        // Bottom panel with buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // Dislike button
        dislikeButton = new JButton("✗ Dislike");
        dislikeButton.setFont(new Font("Arial", Font.BOLD, 18));
        dislikeButton.setPreferredSize(new Dimension(150, 60));
        dislikeButton.setBackground(new Color(220, 53, 69));
        dislikeButton.setForeground(Color.WHITE);
        dislikeButton.setFocusPainted(false);
        dislikeButton.setBorderPainted(false);
        dislikeButton.setOpaque(true);
        dislikeButton.addActionListener(e -> {
            if (approveRecipeController != null && currentRecipeId != -1) {
                String username = viewManagerModel.getState().userName;
                approveRecipeController.declineRecipe(currentRecipeId, username);
            }
        });
        buttonPanel.add(dislikeButton);

        // Like button
        likeButton = new JButton("✓ Like");
        likeButton.setFont(new Font("Arial", Font.BOLD, 18));
        likeButton.setPreferredSize(new Dimension(150, 60));
        likeButton.setBackground(new Color(40, 167, 69));
        likeButton.setForeground(Color.WHITE);
        likeButton.setFocusPainted(false);
        likeButton.setBorderPainted(false);
        likeButton.setOpaque(true);
        likeButton.addActionListener(e -> {
            if (approveRecipeController != null && currentRecipeId != -1) {
                String username = viewManagerModel.getState().userName;
                approveRecipeController.approveRecipe(currentRecipeId, username);
            }
        });
        buttonPanel.add(likeButton);

        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    public void setApproveRecipeController(ApproveRecipeController controller) {
        this.approveRecipeController = controller;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        ApproveRecipeState state = approveRecipeViewModel.getState();

        if (state.getErrorMessage() != null) {
            statusLabel.setText(state.getErrorMessage());
            statusLabel.setForeground(Color.RED);
            recipeNameLabel.setText("No recipes available");
            recipeImageLabel.setText("No image");
            recipeImageLabel.setIcon(null);
            likeButton.setEnabled(false);
            dislikeButton.setEnabled(false);
            return;
        }

        if (state.getRecipeIds() != null && !state.getRecipeIds().isEmpty()) {
            int index = state.getCurrentIndex();
            currentRecipeId = state.getRecipeIds().get(index);
            String recipeName = state.getRecipeNames().get(index);
            String imageUrl = state.getRecipeImages().get(index);

            recipeNameLabel.setText(recipeName);
            statusLabel.setText("Recipe " + (index + 1) + " of " + state.getRecipeIds().size());
            statusLabel.setForeground(Color.DARK_GRAY);

            // Load image from URL in background
            loadImageFromUrl(imageUrl);

            likeButton.setEnabled(true);
            dislikeButton.setEnabled(true);
        } else {
            recipeNameLabel.setText("No more recipes!");
            recipeImageLabel.setText("All done!");
            recipeImageLabel.setIcon(null);
            statusLabel.setText("You've reviewed all available recipes.");
            statusLabel.setForeground(new Color(70, 130, 180));
            likeButton.setEnabled(false);
            dislikeButton.setEnabled(false);
        }

        this.revalidate();
        this.repaint();
    }

    private void loadImageFromUrl(String imageUrl) {
        recipeImageLabel.setText("Loading image...");
        recipeImageLabel.setIcon(null);

        // Load image in a background thread
        new Thread(() -> {
            try {
                java.net.URL url = new java.net.URL(imageUrl);
                ImageIcon imageIcon = new ImageIcon(url);

                // Scale image to fit label
                Image image = imageIcon.getImage();
                Image scaledImage = image.getScaledInstance(380, 280, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);

                // Update label on UI thread
                SwingUtilities.invokeLater(() -> {
                    recipeImageLabel.setText("");
                    recipeImageLabel.setIcon(scaledIcon);
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    recipeImageLabel.setText("Image: " + imageUrl);
                    recipeImageLabel.setIcon(null);
                });
            }
        }).start();
    }

    public String getViewName() {
        return viewName;
    }
}
