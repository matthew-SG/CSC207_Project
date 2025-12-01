package entities;
import java.util.List;

public class InstructionStep {
    private int number;
    private String step;

    public InstructionStep(int number, String step) {
        this.number = number;
        this.step = step;
    }

    public int getNumber() { return number; }
    public String getStep() { return step; }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setStep(String step) {
        this.step = step;
    }
}
