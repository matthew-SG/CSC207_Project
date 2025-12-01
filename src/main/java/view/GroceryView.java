package view;

import entities.Ingredient;
import interface_adapter.grocery_list.GroceryController;
import interface_adapter.grocery_list.GroceryViewModel;
import interface_adapter.grocery_list.GroceryState;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class GroceryView extends JPanel implements PropertyChangeListener {

    private final transient GroceryController controller;
    private final transient GroceryViewModel viewModel;

    private final JPanel listPanel;
    private final JTextField nameField;
    private final JTextField qtyField;
    private final JTextField unitsField;

    private static final Color HEADER_BG = new Color(240, 240, 240);
    private static final Color ODD_ROW_BG = new Color(255, 255, 255);
    private static final Color EVEN_ROW_BG = new Color(248, 250, 252);
    private static final String MY_FONT = "SansSerif";

    private boolean isValidQuantity(String qty) {
        if (qty.trim().isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(qty.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public GroceryView(GroceryController controller, GroceryViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(HEADER_BG);
        headerPanel.setBorder(new MatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        headerPanel.setPreferredSize(new Dimension(0, 40));

        JPanel headerLeft = new JPanel(new GridLayout(1, 3, 10, 0));
        headerLeft.setOpaque(false);
        headerLeft.setBorder(new EmptyBorder(0, 10, 0, 0));
        headerLeft.add(createHeaderLabel("Item"));
        headerLeft.add(createHeaderLabel("Quantity"));
        headerLeft.add(createHeaderLabel("Units"));

        JLabel actionHeader = createHeaderLabel("Actions");
        actionHeader.setHorizontalAlignment(SwingConstants.CENTER);
        actionHeader.setPreferredSize(new Dimension(140, 0));

        headerPanel.add(headerLeft, BorderLayout.CENTER);
        headerPanel.add(actionHeader, BorderLayout.EAST);

        tableContainer.add(headerPanel, BorderLayout.NORTH);

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        tableContainer.add(scrollPane, BorderLayout.CENTER);

        add(tableContainer, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridBagLayout());
        bottomPanel.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(), "Add New Item"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        nameField = new JTextField(10);
        qtyField = new JTextField(5);
        unitsField = new JTextField(5);
        JButton addBtn = new JButton("Add Item");
        addBtn.setBackground(new Color(70, 130, 180));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        bottomPanel.add(new JLabel("Item:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.5;
        bottomPanel.add(nameField, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        bottomPanel.add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.2;
        bottomPanel.add(qtyField, gbc);

        gbc.gridx = 4; gbc.weightx = 0;
        bottomPanel.add(new JLabel("Units:"), gbc);
        gbc.gridx = 5; gbc.weightx = 0.2;
        bottomPanel.add(unitsField, gbc);

        gbc.gridx = 6; gbc.weightx = 0;
        bottomPanel.add(addBtn, gbc);

        addBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String qty = qtyField.getText().trim();
            String units = unitsField.getText().trim();

            if (name.isEmpty() || qty.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Item Name and Quantity cannot be empty.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!isValidQuantity(qty)) {
                JOptionPane.showMessageDialog(this,
                        "Quantity must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                qtyField.requestFocusInWindow();
                return;
            }

            controller.add(name, qty, units);
            nameField.setText("");
            qtyField.setText("");
            unitsField.setText("");
        });

        add(bottomPanel, BorderLayout.SOUTH);

        viewModel.addPropertyChangeListener(this);
        controller.load();
    }

    private JLabel createHeaderLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(MY_FONT, Font.BOLD, 14));
        label.setForeground(Color.DARK_GRAY);
        return label;
    }

    private void refresh() {
        listPanel.removeAll();

        GroceryState s = viewModel.getState();
        if (s.items != null) {
            for (int i = 0; i < s.items.size(); i++) {
                Ingredient g = s.items.get(i);
                listPanel.add(buildRow(i, g));
                listPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
            }
        }
        listPanel.revalidate();
        listPanel.repaint();
    }

    private JPanel buildRow(int index, Ingredient g) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBorder(new EmptyBorder(8, 0, 8, 0));

        Color bgColor = (index % 2 == 0) ? ODD_ROW_BG : EVEN_ROW_BG;
        row.setBackground(bgColor);

        JPanel left = new JPanel(new GridLayout(1, 3, 10, 0));
        left.setBackground(bgColor);
        left.setBorder(new EmptyBorder(0, 10, 0, 0));

        Font dataFont = new Font(MY_FONT, Font.PLAIN, 13);

        JLabel nameLbl = new JLabel(g.getName());
        JLabel qtyLbl = new JLabel(String.valueOf(g.getQuantity()));
        JLabel unitLbl = new JLabel(g.getUnit());

        nameLbl.setFont(dataFont);
        qtyLbl.setFont(dataFont);
        unitLbl.setFont(dataFont);

        left.add(nameLbl);
        left.add(qtyLbl);
        left.add(unitLbl);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        right.setBackground(bgColor);
        right.setPreferredSize(new Dimension(140, 30));

        JButton editBtn = new JButton("Edit");
        JButton delBtn = new JButton("Delete");

        editBtn.setPreferredSize(new Dimension(60, 24));
        delBtn.setPreferredSize(new Dimension(60, 24));
        editBtn.setFont(new Font(MY_FONT, Font.PLAIN, 11));
        delBtn.setFont(new Font(MY_FONT, Font.PLAIN, 11));

        editBtn.addActionListener(e -> {
            String newName = JOptionPane.showInputDialog(this, "Edit Item name", g.getName());
            if (newName == null) return;

            String newQty = JOptionPane.showInputDialog(this, "Edit Quantity",
                    String.valueOf(g.getQuantity()));
            if (newQty == null) return;

            if (!isValidQuantity(newQty)) {
                JOptionPane.showMessageDialog(this, "New Quantity must be a valid number.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String newUnits = JOptionPane.showInputDialog(this, "Edit units", g.getUnit());
            if (newUnits == null) return;

            controller.edit(index, newName, newQty, newUnits);
        });

        delBtn.addActionListener(e -> {
            int ok = JOptionPane.showConfirmDialog(this, "Delete item?", "Confirm",
                    JOptionPane.YES_NO_OPTION);
            if (ok == JOptionPane.YES_OPTION) {
                controller.delete(index);
            }
        });

        right.add(editBtn);
        right.add(delBtn);

        row.add(left, BorderLayout.CENTER);
        row.add(right, BorderLayout.EAST);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        return row;
    }

    public String getViewName() {
        return "Grocery_List";
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        SwingUtilities.invokeLater(this::refresh);
    }
}