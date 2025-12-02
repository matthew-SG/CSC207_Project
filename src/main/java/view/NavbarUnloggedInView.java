package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import interface_adapter.nav_bar.NavbarController;

public class NavbarUnloggedInView extends JPanel {
    private JButton communityButton;
    private JButton signUpButton;
    private JButton loginButton;
    private NavbarController navbarController;

    public NavbarUnloggedInView() {
        setBackground(Color.PINK);
        communityButton = new JButton("Community");
        signUpButton = new JButton("Sign Up");
        loginButton = new JButton("Log In");
        add(communityButton);
        add(signUpButton);
        add(loginButton);

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
