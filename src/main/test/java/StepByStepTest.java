import view.StepByStepView;

import javax.swing.*;

public class StepByStepTest {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Preview");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new StepByStepView().getPanel()); // your panel
        frame.pack();
        frame.setVisible(true);
    }
}
