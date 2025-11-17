package view;

import interface_adapter.community.CommunityState;
import interface_adapter.community.CommunityViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class CommunityManagerView implements PropertyChangeListener {
    private final CardLayout cardLayout;
    private final JPanel views;

    public CommunityManagerView(JPanel views, CardLayout cardLayout, CommunityViewModel communityViewModel) {
        this.views = views;
        this.cardLayout = cardLayout;
        communityViewModel.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            final CommunityState communityState = (CommunityState) evt.getNewValue();
            if (communityState.subviewName.equals(CommunityViewModel.PUB_SUCC)) {
                cardLayout.show(views, CommunityViewModel.VIEWING);
            }
            else {
                cardLayout.show(views, communityState.getSubviewName());
            }
        }
    }
}