package interface_adapter.step_by_step;

import use_case.step_by_step.StepByStepInputBoundary;
import use_case.step_by_step.StepByStepInputData;
import entities.RecipeInstructions;

public class StepByStepController {
    private final StepByStepInputBoundary interactor;
    private final RecipeInstructions instructions;

    private int currentStepIndex = 0;

    public StepByStepController(StepByStepInputBoundary interactor,
                                RecipeInstructions instructions) {
        this.interactor = interactor;
        this.instructions = instructions;
    }

    public void start() {
        currentStepIndex = 0;
        interactor.execute(new StepByStepInputData(instructions, currentStepIndex));
    }

    public void nextStep() {
        currentStepIndex++;
        interactor.execute(new StepByStepInputData(instructions, currentStepIndex));
    }

    public void previousStep() {
        currentStepIndex--;
        interactor.execute(new StepByStepInputData(instructions, currentStepIndex));
    }
}
