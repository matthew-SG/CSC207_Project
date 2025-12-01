package view;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import interface_adapter.ViewManagerModel;
import interface_adapter.likedRecipeList.LikedRecipeListController;
import interface_adapter.likedRecipeList.LikedRecipeListState;
import interface_adapter.likedRecipeList.LikedRecipeListViewModel;

/**
 * View for displaying the user's liked recipes.
 */
public class LikedRecipeListView extends JPanel {

    public static final String VIEWNAME = "Liked Recipes";

    private final LikedRecipeListViewModel viewModel;
    private final LikedRecipeListController controller;
    private final ViewManagerModel viewManagerModel;

    private final JPanel listPanel;

    private static final Color HEADER_BG = new Color(245, 247, 250);
    private static final Color ODD_ROW_BG = new Color(255, 255, 255);
    private static final Color EVEN_ROW_BG = new Color(248, 250, 252);
    private static final Color ROW_BORDER = new Color(220, 223, 230);
    private static final String MY_FONT = "SansSerif";

    public LikedRecipeListView(LikedRecipeListViewModel viewModel,
                               LikedRecipeListController controller,
                               ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.controller = controller;
        this.viewManagerModel = viewManagerModel;

        // Listen for liked-recipes state changes
        this.viewModel.addListener(this::refreshFromState);

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(new Color(242, 244, 248));

        // Top title
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Liked Recipes");
        titleLabel.setFont(new Font(MY_FONT, Font.BOLD, 20));
        titleLabel.setBorder(new EmptyBorder(0, 0, 4, 0));

        JLabel subtitleLabel = new JLabel("All the recipes you've saved in one place.");
        subtitleLabel.setFont(new Font(MY_FONT, Font.PLAIN, 12));
        subtitleLabel.setForeground(Color.DARK_GRAY);

        JPanel titleTextPanel = new JPanel();
        titleTextPanel.setLayout(new BoxLayout(titleTextPanel, BoxLayout.Y_AXIS));
        titleTextPanel.setOpaque(false);
        titleTextPanel.add(titleLabel);
        titleTextPanel.add(subtitleLabel);

        titlePanel.add(titleTextPanel, BorderLayout.WEST);
        add(titlePanel, BorderLayout.NORTH);

        // Container for header + list
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        tableContainer.setBackground(Color.WHITE);

        // Header row
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(HEADER_BG);
        headerPanel.setBorder(new MatteBorder(0, 0, 1, 0, new Color(210, 210, 210)));
        headerPanel.setPreferredSize(new Dimension(0, 36));

        JPanel headerLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 7));
        headerLeft.setOpaque(false);
        headerLeft.add(createHeaderLabel("Recipe"));

        JLabel actionHeader = createHeaderLabel("Actions");
        actionHeader.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 7));
        headerRight.setOpaque(false);
        headerRight.add(actionHeader);

        headerPanel.add(headerLeft, BorderLayout.CENTER);
        headerPanel.add(headerRight, BorderLayout.EAST);

        tableContainer.add(headerPanel, BorderLayout.NORTH);

        // List area
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(new Color(248, 250, 252));

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        tableContainer.add(scrollPane, BorderLayout.CENTER);

        add(tableContainer, BorderLayout.CENTER);

        // Initial load
        controller.loadLikedRecipes();

        // Refresh on entry
        viewManagerModel.addPropertyChangeListener(evt -> {
            SwingUtilities.invokeLater(() -> {
                if (this.isShowing()) {
                    controller.loadLikedRecipes();
                }
            });
        });
    }

    private JLabel createHeaderLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(MY_FONT, Font.BOLD, 13));
        label.setForeground(new Color(80, 80, 80));
        return label;
    }

    private void refreshFromState() {
        listPanel.removeAll();

        LikedRecipeListState state = viewModel.getState();
        List<Integer> ids = state.getRecipeIds();
        List<String> names = state.getRecipeNames();

        if (ids == null || names == null || ids.isEmpty()) {
            JPanel emptyPanel = new JPanel(new BorderLayout());
            emptyPanel.setOpaque(false);
            JLabel emptyLabel = new JLabel("No liked recipes yet. Go like some delicious stuff ✨");
            emptyLabel.setFont(new Font(MY_FONT, Font.ITALIC, 13));
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            emptyLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
            emptyPanel.add(emptyLabel, BorderLayout.CENTER);
            listPanel.add(emptyPanel);
        } else {
            for (int i = 0; i < ids.size(); i++) {
                int recipeId = ids.get(i);
                String recipeName = names.get(i);
                listPanel.add(buildRow(i, recipeId, recipeName));

                if (i < ids.size() - 1) {
                    listPanel.add(Box.createVerticalStrut(4));
                }
            }
        }

        listPanel.revalidate();
        listPanel.repaint();
    }

    private JPanel buildRow(int index, int recipeId, String recipeName) {
        JPanel rowOuter = new JPanel(new BorderLayout());
        rowOuter.setOpaque(false);
        rowOuter.setBorder(new EmptyBorder(4, 10, 4, 10));

        JPanel row = new JPanel(new BorderLayout());
        row.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 1, 1, 1, ROW_BORDER),
                new EmptyBorder(8, 10, 8, 10)
        ));

        Color bgColor = (index % 2 == 0) ? ODD_ROW_BG : EVEN_ROW_BG;
        row.setBackground(bgColor);

        // Left: recipe name
        JPanel left = new JPanel(new BorderLayout());
        left.setOpaque(false);

        JLabel nameLbl = new JLabel("🍽  " + recipeName);
        nameLbl.setFont(new Font(MY_FONT, Font.PLAIN, 14));
        nameLbl.setForeground(new Color(40, 40, 40));
        left.add(nameLbl, BorderLayout.WEST);

        // Right: buttons
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);
        right.setPreferredSize(new Dimension(360, 30));

        JButton stepBtn = createActionButton("Step by Step");
        JButton detailsBtn = createSecondaryButton("Show Details");
        JButton delBtn = createDangerButton("Delete");

        stepBtn.addActionListener(e -> controller.startHandsfree(recipeId, recipeName));

        detailsBtn.addActionListener(e -> showDetailsFor(recipeId, recipeName));

        delBtn.addActionListener(e -> {
            int ok = JOptionPane.showConfirmDialog(
                    this,
                    "Remove this recipe from your liked list?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );
            if (ok == JOptionPane.YES_OPTION) {
                controller.deleteLikedRecipe(recipeId, recipeName);
                // interactor.loadLikedRecipes() is called inside delete → presenter → refreshFromState()
            }
        });

        right.add(stepBtn);
        right.add(detailsBtn);
        right.add(delBtn);

        row.add(left, BorderLayout.CENTER);
        row.add(right, BorderLayout.EAST);

        rowOuter.add(row, BorderLayout.CENTER);
        rowOuter.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        return rowOuter;
    }

    private JButton createActionButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font(MY_FONT, Font.PLAIN, 11));
        button.setPreferredSize(new Dimension(115, 26));
        return button;
    }

    private JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font(MY_FONT, Font.PLAIN, 11));
        button.setPreferredSize(new Dimension(115, 26));
        return button;
    }

    private JButton createDangerButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font(MY_FONT, Font.PLAIN, 11));
        button.setPreferredSize(new Dimension(80, 26));
        button.setForeground(new Color(160, 0, 0));
        return button;
    }

    private void showDetailsFor(int recipeId, String recipeName) {
        LikedRecipeListState state = viewModel.getState();
        List<Integer> ids = state.getRecipeIds();

        if (ids == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "No details available.",
                    "Recipe Details",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        int index = ids.indexOf(recipeId);
        if (index < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not find details for this recipe.",
                    "Recipe Details",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        List<List<String[]>> allIngredients = state.getRecipeIngredients();
        List<Map<String, Double>> allNutrition = state.getRecipeNutrition();
        List<String> allImages = state.getRecipeImages();

        List<String[]> ingredients = (allIngredients != null && index < allIngredients.size())
                ? allIngredients.get(index)
                : new ArrayList<>();

        Map<String, Double> nutrition = (allNutrition != null && index < allNutrition.size())
                ? allNutrition.get(index)
                : null;

        String imageUrl = (allImages != null && index < allImages.size())
                ? allImages.get(index)
                : null;

        StringBuilder sb = new StringBuilder();
        sb.append("Recipe: ").append(recipeName).append("\n\n");

        sb.append("Ingredients:\n");
        if (ingredients.isEmpty()) {
            sb.append("  (none)\n");
        } else {
            for (String[] ing : ingredients) {
                sb.append("  - ")
                        .append(ing[1]).append(" ").append(ing[2]).append(" ")
                        .append(ing[0]).append("\n");
            }
        }

        sb.append("\nNutrition:\n");
        if (nutrition == null || nutrition.isEmpty()) {
            sb.append("  (no nutrition data)\n");
        } else {
            for (Map.Entry<String, Double> entry : nutrition.entrySet()) {
                sb.append("  - ")
                        .append(entry.getKey())
                        .append(": ")
                        .append(entry.getValue())
                        .append("\n");
            }
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(420, 320));

        JLabel imgLabel = null;
        if (imageUrl != null && !imageUrl.isEmpty()) {
            try {
                ImageIcon rawIcon;
                if (imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) {
                    rawIcon = new ImageIcon(new URL(imageUrl));
                } else {
                    rawIcon = new ImageIcon(imageUrl);
                }

                if (rawIcon.getIconWidth() > 0 && rawIcon.getIconHeight() > 0) {
                    int targetWidth = 260;
                    int targetHeight = 190;
                    Image scaled = rawIcon.getImage().getScaledInstance(
                            targetWidth, targetHeight, Image.SCALE_SMOOTH);
                    ImageIcon scaledIcon = new ImageIcon(scaled);

                    imgLabel = new JLabel(scaledIcon);
                    imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    imgLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
                }
            } catch (Exception ignored) {
            }
        }

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        if (imgLabel != null) {
            mainPanel.add(imgLabel, BorderLayout.NORTH);
        }
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(
                this,
                mainPanel,
                "Recipe Details",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public String getViewName() {
        return VIEWNAME;
    }
}
