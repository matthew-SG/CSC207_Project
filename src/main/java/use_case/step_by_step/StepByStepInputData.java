package use_case.step_by_step;

import entities.RecipeInstructions;

/**
 * Input data for the Step-by-Step use case.
 * Contains the instructions, current step index, and operation type.
 */
public class StepByStepInputData {
    private final RecipeInstructions instructions;
    private final int currentStepIndex;

    public StepByStepInputData(RecipeInstructions instructions, int currentStepIndex) {
        this.instructions = instructions;
        this.currentStepIndex = currentStepIndex;
    }

    public RecipeInstructions getInstructions() {
        return instructions;
    }

    public int getCurrentStepIndex() {
        return currentStepIndex;
    }
}