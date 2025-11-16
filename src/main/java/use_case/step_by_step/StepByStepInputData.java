package use_case.step_by_step;

import entities.RecipeInstructions;

/**
 * The input data for the step_by_step Use Case.
 */
public record StepByStepInputData(RecipeInstructions instructions, int currentStepIndex) {

}
