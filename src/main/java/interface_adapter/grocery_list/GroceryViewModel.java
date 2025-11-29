package interface_adapter.grocery_list;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class GroceryViewModel {
    private GroceryState state = new GroceryState();
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public GroceryState getState() { return state; }

    public void setState(GroceryState newState) {
        GroceryState old = this.state;
        this.state = newState;
        pcs.firePropertyChange("Grocery_List", old, newState);
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }
}
