package app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class App extends JFrame {

    private JPanel contentPanel;
    private CardLayout cardLayout;

    public App() {
        setTitle("Navigation Bar Example");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());

        // 1. Create the Navigation Panel
        JPanel navPanel = new JPanel();
        navPanel.setBackground(Color.LIGHT_GRAY);
        JButton homeButton = new JButton("Home");
        JButton settingsButton = new JButton("Settings");
        navPanel.add(homeButton);
        navPanel.add(settingsButton);

        // 2. Create the Content Panel with CardLayout
        contentPanel = new JPanel();
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);

        // Create individual content panels
        JPanel homeContent = new JPanel();
        homeContent.add(new JLabel("Welcome to the Home Page!"));
        JPanel settingsContent = new JPanel();
        settingsContent.add(new JLabel("Adjust your Settings here."));

        contentPanel.add(homeContent, "Home");
        contentPanel.add(settingsContent, "Settings");

        // Add action listeners to navigation buttons
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "Home");
            }
        });

        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "Settings");
            }
        });

        // 3. Add panels to the JFrame
        add(navPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(App::new);
    }
}