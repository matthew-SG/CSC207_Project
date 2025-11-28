package API;

import entities.InstructionStep;
import java.util.List;

public class TestInstructions {
    public static void main(String[] args) {
        String apiKey = "5b07df6820b74cf1b2eae9c1b440f014";
        int recipeId = 716429;

        FindInstructionsSpoonacular api =
                new FindInstructionsSpoonacular();

        List<InstructionStep> steps = api.getAnalyzedInstructions(recipeId, apiKey);

        for (InstructionStep step : steps) {
            System.out.println(step.getNumber() + ". " + step.getStep());
        }
    }
}
