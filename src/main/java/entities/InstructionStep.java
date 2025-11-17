package entities;
import java.util.List;

public class InstructionStep {
    private int number;
    private String step;
    private List<Ingredient> ingredients;
    private List<Equipment> equipment;

    public InstructionStep(int number, String step,
                           List<Ingredient> ingredients,
                           List<Equipment> equipment) {
        this.number = number;
        this.step = step;
        this.ingredients = ingredients;
        this.equipment = equipment;
    }

    public int getNumber() { return number; }
    public String getStep() { return step; }
    public List<Ingredient> getIngredients() { return ingredients; }
    public List<Equipment> getEquipment() { return equipment; }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setEquipment(List<Equipment> equipment) {
        this.equipment = equipment;
    }
}
