package interface_adapter.step_by_step;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * View Model for the Step-by-Step view.
 * Manages the state and notifies observers of changes.
 */
public class StepByStepViewModel {
    private StepByStepState state = new StepByStepState();
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public StepByStepState getState() {
        return state;
    }

    public void setState(StepByStepState state) {
        this.state = state;
    }

    /**
     * Fires a property change event to notify all listeners.
     * @param propertyName the name of the property that changed
     */
    public void firePropertyChanged(String propertyName) {
        support.firePropertyChange(propertyName, null, state);
    }

    /**
     * Adds a property change listener.
     * @param listener the listener to add
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    /**
     * Removes a property change listener.
     * @param listener the listener to remove
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
}