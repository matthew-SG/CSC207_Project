package view;

import interface_adapter.nav_bar.NavbarManagerViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class NavBarManagerView implements PropertyChangeListener {
    private final CardLayout cardLayout;
    private final JPanel views;

    public NavBarManagerView(JPanel views, CardLayout cardLayout, NavbarManagerViewModel navbarManagerViewModel) {
        this.views = views;
        this.cardLayout = cardLayout;
        navbarManagerViewModel.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            final String newNavName = (String) evt.getNewValue();
            cardLayout.show(views, newNavName);
        }
    }

}
