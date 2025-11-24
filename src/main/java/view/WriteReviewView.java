package view;

import interface_adapter.community.CommunityController;
import interface_adapter.community.CommunityState;
import interface_adapter.community.CommunityViewModel;
import interface_adapter.logged_in.LoggedInViewModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The View for writing a review for a selected recipe.
 * This view displays a form where users can enter their star rating
 * and review comment for a specific recipe.
 */
public class WriteReviewView extends JPanel implements PropertyChangeListener {
    private final String viewName = CommunityViewModel.WRITING_REVIEW;
    private final CommunityViewModel communityViewModel;
    private final LoggedInViewModel loggedInViewModel;
    private CommunityController communityController;

    private final JLabel recipeNameLabel;
    private final JTextArea reviewTextArea;
    private final ButtonGroup starButtonGroup;
    private final JRadioButton[] starButtons;
    private final JButton submitButton;
    private final JButton cancelButton;
    public WriteReviewView(CommunityViewModel communityViewModel, LoggedInViewModel loggedInViewModel) {
        this.communityViewModel = communityViewModel;
        this.loggedInViewModel = loggedInViewModel;
        this.communityViewModel.addPropertyChangeListener(this);

        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title section
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Write a Review", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        
        recipeNameLabel = new JLabel("Recipe: ", SwingConstants.CENTER);
        recipeNameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        recipeNameLabel.setForeground(new Color(70, 70, 70));
        
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(recipeNameLabel, BorderLayout.CENTER);
        this.add(topPanel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(new EmptyBorder(20, 50, 20, 50));

        // Star rating section
        JPanel starPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel starLabel = new JLabel("Rating: ");
        starLabel.setFont(new Font("Arial", Font.BOLD, 14));
        starPanel.add(starLabel);

        starButtonGroup = new ButtonGroup();
        starButtons = new JRadioButton[5];
        
        for (int i = 0; i < 5; i++) {
            final int rating = i + 1;
            starButtons[i] = new JRadioButton(rating + " Star" + (rating > 1 ? "s" : ""));
            starButtons[i].setFont(new Font("Arial", Font.PLAIN, 12));
            starButtonGroup.add(starButtons[i]);
            starPanel.add(starButtons[i]);
        }
        
        starPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(starPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Review text section
        JLabel reviewLabel = new JLabel("Your Review:");
        reviewLabel.setFont(new Font("Arial", Font.BOLD, 14));
        reviewLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(reviewLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        reviewTextArea = new JTextArea(10, 40);
        reviewTextArea.setWrapStyleWord(true);
        reviewTextArea.setLineWrap(true);
        reviewTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
        reviewTextArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                new EmptyBorder(5, 5, 5, 5)
        ));

        JScrollPane reviewScrollPane = new JScrollPane(reviewTextArea);
        reviewScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(reviewScrollPane);

        this.add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        submitButton = new JButton("Submit Review");
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.setPreferredSize(new Dimension(150, 40));
        submitButton.setBackground(new Color(70, 130, 180));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSubmit();
            }
        });

        cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.PLAIN, 14));
        cancelButton.setPreferredSize(new Dimension(150, 40));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCancel();
            }
        });

        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void handleSubmit() {
        // Validate star rating
        int selectedRating = getSelectedRating();
        if (selectedRating == 0) {
            JOptionPane.showMessageDialog(this, 
                "Please select a star rating.", 
                "Rating Required", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Get review text
        String reviewText = reviewTextArea.getText().trim();
        if (reviewText.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please write a review comment.", 
                "Review Required", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String recipeName = communityViewModel.getState().getSeletedRecipeName();
        String recipeImageUrl = communityViewModel.getState().getSeletedRecipeImageUrl();
        int recipeId = communityViewModel.getState().getSeletedRecipe();
        String username = loggedInViewModel.getState().getUsername();

        // Call controller to publish review
        if (communityController != null && recipeId != -1) {
            communityController.publish(selectedRating,
                    reviewText, recipeId,
                    username, recipeName,
                    recipeImageUrl);
            // Clear form
            clearForm();
        }
    }

    private void handleCancel() {
        // Clear form and navigate back
        clearForm();
        
        // Navigate back to community view
        if (communityController != null) {
            communityController.viewCommunity();
        }
    }

    private int getSelectedRating() {
        for (int i = 0; i < starButtons.length; i++) {
            if (starButtons[i].isSelected()) {
                return i + 1;
            }
        }
        return 0;
    }

    private void clearForm() {
        reviewTextArea.setText("");
        starButtonGroup.clearSelection();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            final CommunityState state = (CommunityState) evt.getNewValue();
            
            // Only update if this is the writing review state
            if (CommunityViewModel.WRITING_REVIEW.equals(state.getSubviewName())) {
                updateView(state);
            }
        }
    }

    private void updateView(CommunityState state) {
        int currentRecipeId = state.getSeletedRecipe();
        String currentRecipeName = state.getSeletedRecipeName();
        
        recipeNameLabel.setText("Reviewing: " + currentRecipeName);
        
        // Clear the form for a fresh review
        clearForm();
    }

    public String getViewName() {
        return viewName;
    }

    public void setCommunityController(CommunityController communityController) {
        this.communityController = communityController;
    }
}
