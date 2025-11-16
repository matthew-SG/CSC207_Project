package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.ViewManagerState;

import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Utility class to display a modal error message window using JOptionPane.
 * The message content is passed dynamically.
 */
public class ErrorMessageView implements PropertyChangeListener {
    ViewManagerModel viewManagerModel;
    public ErrorMessageView(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
        viewManagerModel.addPropertyChangeListener(this);
    }

    /**
     * Displays a standard Swing error message dialog.
     *
     * @param message The specific error message to display in the dialog body.
     */
    public static void showError(String message) {
        // Run the GUI creation on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            // The first argument (parentComponent) is set to null, 
            // which centers the dialog on the screen.
            String title = "Application Error";
            int messageType = JOptionPane.ERROR_MESSAGE;

            JOptionPane.showMessageDialog(
                    null,           // Parent component (null centers on screen)
                    message,        // The dynamic message content
                    title,          // The title bar text
                    messageType     // The message type (shows the standard error icon)
            );
        });
    }

    /**
     * Main method to demonstrate the showError functionality.
     */
    public static void main(String[] args) {
        // Example 1: A general file system error
        String error1 = "File access denied. Please check your user permissions for the specified directory.";
        showError(error1);

        // Example 2: A network connection issue
        // We use a separate thread/delay here just to show the second dialog after the first one is closed.
        try {
            Thread.sleep(1500); // Wait for a moment before showing the next error
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String error2 = "Connection timeout: The server did not respond within the allocated time.";
        showError(error2);
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (propertyChangeEvent.getPropertyName().equals("error")) {
            ErrorMessageView.showError(((ViewManagerState) propertyChangeEvent.getNewValue()).errorMessage);
        }
    }
}