package interface_adapter.grocery_list;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * The ViewModel for the Grocery List feature.
 * This class holds the mutable code GroceryState and provides mechanism
 * for views to register as listeners for state changes,
 * ensuring the UI updates automatically when the grocery list data changes.
 */
public class GroceryViewModel {
    private GroceryState state = new GroceryState();
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /**
     * Retrieves the current state of the grocery list view.
     *
     * @return The current GroceryState object.
     */
    public GroceryState getState() { return state; }

    /**
     * Sets a new state for the view model and notifies all registered listeners
     * about the change.
     *
     * @param newState The new GroceryState to set.
     */
    public void setState(GroceryState newState) {
        GroceryState old = this.state;
        this.state = newState;
        pcs.firePropertyChange("Grocery_List", old, newState);
    }

    /**
     * Registers a listener (typically the UI view) to be notified whenever the
     * GroceryState changes.
     *
     * @param l The PropertyChangeListener to add.
     */
    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }
}
