package view;

import interface_adapter.nav_bar.NavbarController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NavbarLoggedInView extends JPanel {
    private JButton communityButton;
    private JButton generateRecipe;
    private JButton approveRecipeButton;
    private JButton userProfileButton;
    private JButton groceryButton;
    private NavbarController navbarController = null;

    public NavbarLoggedInView(){
        setBackground(Color.PINK);
        communityButton = new JButton("community");
        generateRecipe = new JButton("generate recipe");
        approveRecipeButton = new JButton("approve recipes");
        userProfileButton = new JButton("user profile");
        groceryButton = new JButton("grocery list");
        add(communityButton);
        add(generateRecipe);
        add(approveRecipeButton);
        add(userProfileButton);
        add(groceryButton);

        communityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                navbarController.switchToCommunity();
            }
        });

        approveRecipeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                navbarController.switchToApproveRecipe();
            }
        });

        userProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                navbarController.switchToProfile();
            }
        });

        groceryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                navbarController.switchToGroceryList();
            }
        });
    }

    public void setNavbarController(NavbarController navbarController) {
        this.navbarController = navbarController;
    }
}
