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
    private JButton mealPlanButton;
    private JButton searchByIngredientsButton;
    private JButton groceryButton;
    private NavbarController navbarController = null;
    private JButton likeRecipeButton;

    public NavbarLoggedInView(){
        setBackground(Color.PINK);
        communityButton = new JButton("community");
        generateRecipe = new JButton("generate recipe");
        userProfileButton = new JButton("user profile");
        groceryButton = new JButton("grocery list");
        mealPlanButton = new JButton("Meal Plan Generator");
        searchByIngredientsButton = new JButton("search by ingredients");
        likeRecipeButton = new JButton("Like Recipe List");
        add(communityButton);
        add(generateRecipe);
        add(userProfileButton);
        add(groceryButton);
        add(mealPlanButton);
        add(searchByIngredientsButton);
        add(likeRecipeButton);

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

        generateRecipe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                navbarController.switchToGenerateRecipe();
            }
        });

        mealPlanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) { navbarController.switchToMealPlan();}
        });

        searchByIngredientsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
              navbarController.switchToSearchByIngredients();
            }
            
        });
          
        groceryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                navbarController.switchToGroceryList();
            }
        });

        likeRecipeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                navbarController.switchToLikedRecipeList();
            }
        });
    }

    public void setNavbarController(NavbarController navbarController) {
        this.navbarController = navbarController;
    }
}
