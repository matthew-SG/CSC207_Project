package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * View 3: Review (Frame 3, Bottom-Right)
 * Displays details for one item and allows writing a review.
 */
class CommunityReviewView extends JPanel {

    private JButton commentsButton;
    private JButton submitButton;
    private JTextArea reviewArea;
    private JTextField starsField;
    private JLabel titleLabel;

    public CommunityReviewView() {
        super(new BorderLayout(10, 10));
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        // --- Top ---
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        commentsButton = new JButton("Comments");
        topBar.add(commentsButton);
        this.add(topBar, BorderLayout.NORTH);

        // --- Center (Form) ---
        // Using GridBagLayout for a flexible form
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        titleLabel = new JLabel("Post Title");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span 2 columns
        centerPanel.add(titleLabel, gbc);

        // "Review:" Label
        JLabel reviewLabel = new JLabel("Review:");
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.NORTHEAST; // Align top-right
        gbc.fill = GridBagConstraints.NONE;
        centerPanel.add(reviewLabel, gbc);

        // Review Text Area
        reviewArea = new JTextArea();
        reviewArea.setWrapStyleWord(true);
        reviewArea.setLineWrap(true);
        JScrollPane reviewScrollPane = new JScrollPane(reviewArea);
        reviewScrollPane.setPreferredSize(new Dimension(100, 150)); // Give it a size
        gbc.gridx = 1;
        gbc.weightx = 1.0; // Take horizontal space
        gbc.weighty = 1.0; // Take vertical space
        gbc.fill = GridBagConstraints.BOTH;
        centerPanel.add(reviewScrollPane, gbc);

        // "Stars:" Label
        JLabel starsLabel = new JLabel("Stars:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        centerPanel.add(starsLabel, gbc);

        // Stars Text Field
        starsField = new JTextField();
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        centerPanel.add(starsField, gbc);

        // Submit Button
        submitButton = new JButton("Submit"); // "coding" button from drawing
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // Span 2 columns
        centerPanel.add(submitButton, gbc);

        this.add(centerPanel, BorderLayout.CENTER);
    }

    // --- PUBLIC API ---

    /**
     * API Method: Loads the details of a post into the view.
     * @param title The title of the post.
     * @param review The existing review text (if any).
     * @param stars The existing star rating (if any).
     */
    public void loadPostDetails(String title, String review, String stars) {
        titleLabel.setText(title);
        setReviewText(review);
        setStarsText(stars);
    }

    /**
     * API Method: Gets the 'Comments' button to add an action handler.
     * @return The Comments Button.
     */
    public JButton getCommentsButton() {
        return commentsButton;
    }

    /**
     * API Method: Gets the 'Submit' button to add an action handler.
     * @return The Submit Button.
     */
    public JButton getSubmitButton() {
        return submitButton;
    }

    /**
     * API Method: Gets the text from the review area.
     * @return The review text.
     */
    public String getReviewText() {
        return reviewArea.getText();
    }

    /**
     * API Method: Sets the text in the review area.
     * @param text The review text.
     */
    public void setReviewText(String text) {
        reviewArea.setText(text);
    }

    /**
     * API Method: Gets the text from the stars field.
     * @return The stars text.
     */
    public String getStarsText() {
        return starsField.getText();
    }

    /**
     * API Method: Sets the text in the stars field.
     * @param text The stars text.
     */
    public void setStarsText(String text) {
        starsField.setText(text);
    }
}