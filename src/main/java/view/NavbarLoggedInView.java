package view;

import interface_adapter.nav_bar.NavbarController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NavbarLoggedInView extends JPanel {
    private JButton communityButton;
    private JButton generateRecipe;
    private JButton userProfileButton;
    private NavbarController navbarController = null;

    public NavbarLoggedInView(){
        setBackground(Color.PINK);
        communityButton = new JButton("community");
        generateRecipe = new JButton("generate recipe");
        generateRecipe = new JButton("generate recipe");
        userProfileButton = new JButton("user profile");
        add(communityButton);
        add(generateRecipe);
        add(userProfileButton);

        communityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                navbarController.switchToCommunity();
            }
        });

        userProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                navbarController.switchToProfile();
            }
        });
    }

    public void setNavbarController(NavbarController navbarController) {
        this.navbarController = navbarController;
    }
}
