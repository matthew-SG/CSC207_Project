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

        // Create the Navigation Panel
        JPanel navPanel = new JPanel();
        navPanel.setBackground(Color.LIGHT_GRAY);
        JButton communityButton = new JButton("community");
        JButton signUpButton = new JButton("sign up");
        JButton loginButton = new JButton("log in");
        JButton generateRecipe = new JButton("generate recipe");
        navPanel.add(communityButton);
        navPanel.add(signUpButton);
        navPanel.add(loginButton);
        navPanel.add(generateRecipe);


        add(navPanel, BorderLayout.NORTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(App::new);
    }
}