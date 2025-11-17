package view;

import interface_adapter.grocery_list.GroceryController;
import interface_adapter.grocery_list.GroceryViewModel;
import interface_adapter.grocery_list.GroceryState;
import entities.GroceryItem;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class GroceryView extends JPanel implements PropertyChangeListener {

    private final GroceryController controller;
    private final GroceryViewModel viewModel;

    private final JPanel listPanel;
    private final JTextField nameField;
    private final JTextField qtyField;
    private final JTextField unitsField;

    public GroceryView(GroceryController controller, GroceryViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;

        setLayout(new BorderLayout());

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        add(new JScrollPane(listPanel), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        nameField = new JTextField(12);
        qtyField = new JTextField(6);
        unitsField = new JTextField(6);
        JButton addBtn = new JButton("Add");

        addBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String qty = qtyField.getText().trim();
            String units = unitsField.getText().trim();
            if (name.isEmpty() || qty.isEmpty()) return;
            controller.add(name, qty, units);
            nameField.setText("");
            qtyField.setText("");
            unitsField.setText("");
        });

        bottom.add(new JLabel("Name:"));
        bottom.add(nameField);
        bottom.add(new JLabel("Qty:"));
        bottom.add(qtyField);
        bottom.add(new JLabel("Units:"));
        bottom.add(unitsField);
        bottom.add(addBtn);

        add(bottom, BorderLayout.SOUTH);

        viewModel.addPropertyChangeListener(this);

        controller.load();
    }

    private void refresh() {
        listPanel.removeAll();

        JPanel header = new JPanel(new GridLayout(1,3,6,0));
        header.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
        header.add(new JLabel("Name"));
        header.add(new JLabel("Qty"));
        header.add(new JLabel("Units"));
        listPanel.add(header);

        GroceryState s = viewModel.getState();
        if (s.items != null) {
            for (int i = 0; i < s.items.size(); i++) {
                GroceryItem g = s.items.get(i);
                listPanel.add(buildRow(i, g));
            }
        }
        listPanel.revalidate();
        listPanel.repaint();
    }

    private JPanel buildRow(int index, GroceryItem g) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));

        JPanel left = new JPanel(new GridLayout(1,3,6,0));
        left.add(new JLabel(g.getItem()));
        left.add(new JLabel(g.getQty()));
        left.add(new JLabel(g.getUnits()));

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton editBtn = new JButton("Edit");
        JButton delBtn = new JButton("Delete");

        editBtn.addActionListener(e -> {
            String newName = JOptionPane.showInputDialog(this, "Edit name", g.getItem());
            if (newName == null) return;
            String newQty = JOptionPane.showInputDialog(this, "Edit qty", g.getQty());
            if (newQty == null) return;
            String newUnits = JOptionPane.showInputDialog(this, "Edit units", g.getUnits());
            if (newUnits == null) return;
            controller.edit(index, newName, newQty, newUnits);
        });

        delBtn.addActionListener(e -> {
            int ok = JOptionPane.showConfirmDialog(this, "Delete item?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (ok == JOptionPane.YES_OPTION) {
                controller.delete(index);
            }
        });

        right.add(editBtn);
        right.add(delBtn);

        row.add(left, BorderLayout.CENTER);
        row.add(right, BorderLayout.EAST);
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
