package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.community.CommunityController;
import interface_adapter.community.CommunityState;
import interface_adapter.community.CommunityViewModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * The View for displaying the community feed of all public reviews.
 * This view shows all reviews for all recipes with recipe names, star ratings,
 * comments, and usernames in a scrollable list.
 */
public class CommunityView extends JPanel implements PropertyChangeListener {
    private final String viewName = CommunityViewModel.VIEWING;
    private final CommunityViewModel communityViewModel;
    private final ViewManagerModel viewManagerModel;
    private CommunityController communityController;

    private final JPanel reviewListPanel;
    private final JScrollPane scrollPane;
    private final JLabel titleLabel;
    private final JButton postReviewButton;

    public CommunityView(CommunityViewModel communityViewModel, ViewManagerModel viewManagerModel) {
        this.communityViewModel = communityViewModel;
        this.viewManagerModel = viewManagerModel;
        this.communityViewModel.addPropertyChangeListener(this);

        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Top panel with title and post button
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        
        titleLabel = new JLabel("Community Reviews", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Post Review button
        postReviewButton = new JButton("Post Review");
        postReviewButton.setFont(new Font("Arial", Font.BOLD, 14));
        postReviewButton.setPreferredSize(new Dimension(150, 40));
        postReviewButton.setBackground(new Color(70, 130, 180));
        postReviewButton.setForeground(Color.WHITE);
        postReviewButton.setFocusPainted(false);
        postReviewButton.addActionListener(e -> {
            if (communityController != null) {
                communityController.viewToPost(viewManagerModel.getState().userName, viewManagerModel.getState().isLoggedIn);
            }
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(postReviewButton);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        
        this.add(topPanel, BorderLayout.NORTH);

        // Reviews list panel
        reviewListPanel = new JPanel();
        reviewListPanel.setLayout(new BoxLayout(reviewListPanel, BoxLayout.Y_AXIS));
        reviewListPanel.setBackground(Color.WHITE);

        // Scroll pane for reviews
        scrollPane = new JScrollPane(reviewListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.add(scrollPane, BorderLayout.CENTER);

        // Initial empty state
        displayEmptyState();
    }

    private void displayEmptyState() {
        reviewListPanel.removeAll();
        JLabel emptyLabel = new JLabel("No reviews available yet.", SwingConstants.CENTER);
        emptyLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        emptyLabel.setForeground(Color.GRAY);
        emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        reviewListPanel.add(Box.createVerticalGlue());
        reviewListPanel.add(emptyLabel);
        reviewListPanel.add(Box.createVerticalGlue());
        reviewListPanel.revalidate();
        reviewListPanel.repaint();
    }

    private void displayReviews(List<String> recipeNames, List<Integer> stars, List<String> comments) {
        reviewListPanel.removeAll();

        if (recipeNames == null || recipeNames.isEmpty()) {
            displayEmptyState();
            return;
        }

        for (int i = 0; i < recipeNames.size(); i++) {
            String recipeName = recipeNames.get(i);
            int starRating = (stars != null && i < stars.size()) ? stars.get(i) : 0;
            String comment = (comments != null && i < comments.size()) ? comments.get(i) : "";

            JPanel reviewCard = createReviewCard(recipeName, starRating, comment);
            reviewListPanel.add(reviewCard);
            reviewListPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacing between cards
        }

        reviewListPanel.revalidate();
        reviewListPanel.repaint();
    }

    private JPanel createReviewCard(String recipeName, int starRating, String comment) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(15, 15, 15, 15)
        ));
        card.setBackground(Color.WHITE);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        // Left panel: Recipe name and stars
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);

        JLabel recipeLabel = new JLabel(recipeName);
        recipeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        recipeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel starsLabel = new JLabel(getStarString(starRating));
        starsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        starsLabel.setForeground(new Color(255, 165, 0)); // Orange color for stars
        starsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        leftPanel.add(recipeLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        leftPanel.add(starsLabel);

        // Right panel: Comment
        JTextArea commentArea = new JTextArea(comment);
        commentArea.setWrapStyleWord(true);
        commentArea.setLineWrap(true);
        commentArea.setEditable(false);
        commentArea.setFont(new Font("Arial", Font.PLAIN, 12));
        commentArea.setBackground(Color.WHITE);
        commentArea.setBorder(BorderFactory.createEmptyBorder());

        JScrollPane commentScroll = new JScrollPane(commentArea);
        commentScroll.setBorder(BorderFactory.createEmptyBorder());
        commentScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        commentScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        card.add(leftPanel, BorderLayout.WEST);
        card.add(commentScroll, BorderLayout.CENTER);

        return card;
    }

    private String getStarString(int rating) {
        if (rating < 0 || rating > 5) {
            rating = 0;
        }
        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            if (i < rating) {
                stars.append("★");
            } else {
                stars.append("☆");
            }
        }
        stars.append(" (").append(rating).append("/5)");
        return stars.toString();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            final CommunityState state = (CommunityState) evt.getNewValue();
            
            // Only update if this is the viewing state
            if (CommunityViewModel.VIEWING.equals(state.getSubviewName()) || 
                CommunityViewModel.PUB_SUCC.equals(state.getSubviewName())) {
                updateView(state);
            }
        }
    }

    private void updateView(CommunityState state) {
        List<String> recipeNames = state.getRecipeNames();
        List<Integer> stars = state.getStars();
        List<String> comments = state.getComments();
        
        displayReviews(recipeNames, stars, comments);
        
        // Update title if there's a prompt
        if (state.getPrompt() != null && !state.getPrompt().isEmpty()) {
            titleLabel.setText(state.getPrompt());
        } else {
            titleLabel.setText("Community Reviews");
        }
    }

    public String getViewName() {
        return viewName;
    }

    public void setCommunityController(CommunityController communityController) {
        this.communityController = communityController;
        this.communityController.viewCommunity();
    }
}
