package interface_adapter.step_by_step;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * ViewModel for the Step-by-Step view.
 * Manages the current state of the step-by-step navigation and notifies
 * listeners (the view) when the state changes using the Observer pattern.
 */
public class StepByStepViewModel {
    // Property change support for notifying observers when state changes
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    // The current state of the step-by-step view
    private StepByStepState state = new StepByStepState();

    /**
     * Updates the state and notifies all registered listeners of the change.
     * @param newState the new state to set
     */
    public void setState(StepByStepState newState) {
        final StepByStepState oldState = this.state;
        this.state = newState;

        // Notify listeners that the state has changed
        pcs.firePropertyChange("state", oldState, newState);
    }

    /**
     * Gets the name identifier for this view.
     * Used by the ViewManager to identify and switch to this view.
     * @return the view name "step by step"
     */
    public String getViewName() {
        return "step by step";
    }

    /**
     * Fires a generic property change event to force view refresh.
     * This is useful when the state object is modified directly
     * without calling setState().
     */
    public void firePropertyChange() {
        pcs.firePropertyChange(null, null, null);
    }

    /**
     * Gets the current state of the step-by-step view.
     * @return the current StepByStepState
     */
    public StepByStepState getState() {
        return state;
    }

    /**
     * Registers a PropertyChangeListener to be notified of state changes.
     * Typically called by the view to observe state updates.
     * @param listener the listener to add
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }
}
