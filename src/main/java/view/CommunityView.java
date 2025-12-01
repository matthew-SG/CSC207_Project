package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.ViewManagerState;
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
    private final JButton refreshReviewButton;

    public CommunityView(CommunityViewModel communityViewModel, ViewManagerModel viewManagerModel) {
        this.communityViewModel = communityViewModel;
        this.viewManagerModel = viewManagerModel;
        this.communityViewModel.addPropertyChangeListener(this);;

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

        refreshReviewButton = new JButton("Refresh Review");
        refreshReviewButton.setPreferredSize(new Dimension(150, 40));
        refreshReviewButton.setBackground(new Color(70, 130, 180));
        refreshReviewButton.setForeground(Color.WHITE);
        refreshReviewButton.setFocusPainted(false);
        refreshReviewButton.addActionListener(e -> {
            if (communityController != null) {
                communityController.viewCommunity();
            }
        });





        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(postReviewButton);
        buttonPanel.add(refreshReviewButton);
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

    private void displayReviews(List<String> recipeNames, List<Integer> stars, List<String> comments,
                                List<String> recipeImages, List<String> usernames, List<Integer> ratingIds) {
        reviewListPanel.removeAll();

        if (recipeNames == null || recipeNames.isEmpty()) {
            displayEmptyState();
            return;
        }

        for (int i = 0; i < recipeNames.size(); i++) {
            String recipeName = recipeNames.get(i);
            int starRating = (stars != null && i < stars.size()) ? stars.get(i) : 0;
            String comment = (comments != null && i < comments.size()) ? comments.get(i) : "";
            String recipeImageUrl = (recipeImages != null && i < recipeImages.size()) ? recipeImages.get(i) : null;
            String username = (usernames != null && i < usernames.size()) ? usernames.get(i) : null;
            int ratingId = (ratingIds != null && i < ratingIds.size()) ? ratingIds.get(i) : -1;

            JPanel reviewCard = createReviewCard(recipeName, starRating, comment, recipeImageUrl, username, ratingId);
            reviewListPanel.add(reviewCard);
            reviewListPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacing between cards
        }

        reviewListPanel.revalidate();
        reviewListPanel.repaint();
    }

    private JPanel createReviewCard(String recipeName, int starRating, String comment,
                                    String recipeImageUrl, String username, int ratingId) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(15, 15, 15, 15)
        ));
        card.setBackground(Color.WHITE);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        // Image panel (leftmost)
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new BorderLayout());
        imagePanel.setBackground(Color.WHITE);
        imagePanel.setPreferredSize(new Dimension(120, 120));
        
        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(120, 120));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        
        if (recipeImageUrl != null && !recipeImageUrl.isEmpty()) {
            // Load image asynchronously
            loadImageAsync(recipeImageUrl, imageLabel, 120, 120);
        } else {
            // Placeholder if no image
            imageLabel.setText("No Image");
            imageLabel.setForeground(Color.GRAY);
        }
        
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        card.add(imagePanel, BorderLayout.WEST);

        // Middle panel: Recipe name, stars, and username
        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
        middlePanel.setBackground(Color.WHITE);

        JLabel recipeLabel = new JLabel(recipeName);
        recipeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        recipeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel starsLabel = new JLabel(getStarString(starRating));
        starsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        starsLabel.setForeground(new Color(255, 165, 0)); // Orange color for stars
        starsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Username label with fallback
        String displayUsername = (username != null && !username.trim().isEmpty()) ? username : "Anonymous";
        JLabel usernameLabel = new JLabel("by " + displayUsername);
        usernameLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        usernameLabel.setForeground(new Color(100, 100, 100)); // Gray color for username
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        middlePanel.add(recipeLabel);
        middlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        middlePanel.add(starsLabel);
        middlePanel.add(Box.createRigidArea(new Dimension(0, 3)));
        middlePanel.add(usernameLabel);

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

        // Create a panel to hold middle and right sections
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(middlePanel, BorderLayout.WEST);
        contentPanel.add(commentScroll, BorderLayout.CENTER);

        JButton addButton = buildAddToLikedButton(ratingId, recipeName);
        JPanel actionPanel = new JPanel();
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        actionPanel.add(addButton);
        actionPanel.add(Box.createVerticalGlue());
        contentPanel.add(actionPanel, BorderLayout.EAST);
        
        card.add(contentPanel, BorderLayout.CENTER);

        return card;
    }

    private JButton buildAddToLikedButton(int ratingId, String recipeName) {
        JButton addButton = new JButton("Add to Liked");
        addButton.setFocusPainted(false);
        addButton.setBackground(new Color(60, 179, 113));
        addButton.setForeground(Color.WHITE);
        addButton.setPreferredSize(new Dimension(140, 35));

        if (ratingId <= 0) {
            addButton.setEnabled(false);
            addButton.setToolTipText("Recipe details unavailable.");
        } else {
            addButton.addActionListener(e -> handleAddToLiked(ratingId, recipeName));
        }
        return addButton;
    }

    private void handleAddToLiked(int ratingId, String recipeName) {
        if (communityController == null) {
            return;
        }
        if (ratingId <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Recipe details are not available for this review yet.",
                    "Unavailable",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        ViewManagerState managerState = viewManagerModel.getState();
        if (managerState == null || !managerState.isLoggedIn) {
            JOptionPane.showMessageDialog(this,
                    "Log in to add recipes to your liked list.",
                    "Login Required",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String username = managerState.userName;
        if (username == null || username.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "User session missing. Please log in again.",
                    "Login Required",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        communityController.likeRecipe(ratingId, username);
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
                // Ensure UI updates happen on EDT
                SwingUtilities.invokeLater(() -> updateView(state));
            }
        }
    }

    private void updateView(CommunityState state) {
        List<String> recipeNames = state.getRecipeNames();
        List<Integer> stars = state.getStars();
        List<String> comments = state.getComments();
        List<String> recipeImages = state.getRecipeImages();
        List<String> usernames = state.getUsernames();
        List<Integer> ratingIds = state.getRatingIds();
        
        displayReviews(recipeNames, stars, comments, recipeImages, usernames, ratingIds);
        
        // Update title if there's a prompt
        if (state.getPrompt() != null && !state.getPrompt().isEmpty()) {
            titleLabel.setText(state.getPrompt());
        } else {
            titleLabel.setText("Community Reviews");
        }
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
                    java.net.URL url = java.net.URI.create(imageUrl).toURL();
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

    public String getViewName() {
        return viewName;
    }

    public void setCommunityController(CommunityController communityController) {
        this.communityController = communityController;
        this.communityController.viewCommunity();
    }
}
