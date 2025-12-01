package interface_adapter.step_by_step;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class StepByStepViewModel {
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private StepByStepState state = new StepByStepState();

    public void setState(StepByStepState newState) {
        StepByStepState oldState = this.state;
        this.state = newState;

        pcs.firePropertyChange("state", oldState, newState);
    }

    public String getViewName() {
        return "step by step";
    }

    public void firePropertyChange() {
        pcs.firePropertyChange(null, null, null);
    }

    public StepByStepState getState() {
        return state;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }
}
