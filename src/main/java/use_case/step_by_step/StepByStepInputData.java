package use_case.step_by_step;

import entities.RecipeInstructions;

/**
 * The input data for the step_by_step Use Case.
 * @param instructions set of instructions.
 * @param currentStepIndex stores current index of instruction in instruction list.
 */
public record StepByStepInputData(RecipeInstructions instructions, int currentStepIndex) {

}
