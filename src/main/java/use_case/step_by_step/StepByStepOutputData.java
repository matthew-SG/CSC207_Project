package use_case.step_by_step;

/**
 * The output data for the step_by_step Use Case.
 */

public record StepByStepOutputData(String stepText, int stepNumber, boolean hasNext, boolean hasPrevious){

}