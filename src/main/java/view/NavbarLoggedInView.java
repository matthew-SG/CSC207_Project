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
    private JButton mealPlanButton;
    private NavbarController navbarController = null;

    public NavbarLoggedInView(){
        setBackground(Color.PINK);
        communityButton = new JButton("community");
        generateRecipe = new JButton("generate recipe");
        approveRecipeButton = new JButton("approve recipes");
        userProfileButton = new JButton("user profile");
        mealPlanButton = new JButton("Meal Plan Generator");
        add(communityButton);
        add(generateRecipe);
        add(approveRecipeButton);
        add(userProfileButton);
        add(mealPlanButton);

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

        generateRecipe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {navbarController.switchToGenerateRecipe(); }
        mealPlanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) { navbarController.switchToMealPlan();}
        });
    }

    public void setNavbarController(NavbarController navbarController) {
        this.navbarController = navbarController;
    }
}
