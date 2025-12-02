package use_case.step_by_step;

/**
 * The output data for the step_by_step Use Case.
 * @param stepText the text for this step in the recipe.
 * @param stepNumber the index number of this step.
 * @param hasNext tells us whether there is another step after this.
 * @param hasPrevious tells us whether there is a previous step before this.
 */

public record StepByStepOutputData(String stepText, int stepNumber, boolean hasNext, boolean hasPrevious) {

}
