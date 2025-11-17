package view;

import interface_adapter.nav_bar.NavbarController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NavbarUnloggedInView extends JPanel {
    private JButton communityButton;
    private JButton signUpButton;
    private JButton loginButton;
    private JButton generateRecipe;
    private NavbarController navbarController = null;

    public NavbarUnloggedInView(){
        setBackground(Color.PINK);
        communityButton = new JButton("community");
        signUpButton = new JButton("sign up");
        loginButton = new JButton("log in");
        generateRecipe = new JButton("generate recipe");
        generateRecipe = new JButton("generate recipe");
        add(communityButton);
        add(signUpButton);
        add(loginButton);
        add(generateRecipe);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                navbarController.switchToLogin();
            }
        });

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                navbarController.switchToSignUp();
            }
        });

        communityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                navbarController.switchToCommunity();
            }
        });
    }

    public void setNavbarController(NavbarController navbarController) {
        this.navbarController = navbarController;
    }
}
