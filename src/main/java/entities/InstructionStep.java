package entities;
import java.util.List;

public class InstructionStep {
    private final int number;
    private final String step;
    private final List<Ingredient> ingredients;
    private final List<Equipment> equipment;

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
}
