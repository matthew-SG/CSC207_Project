package interface_adapter.step_by_step;

import entities.RecipeInstructions;
import use_case.step_by_step.StepByStepInputBoundary;
import use_case.step_by_step.StepByStepInputData;

/**
 * Controller for the Step-by-Step use case.
 * Handles user navigation commands and delegates to the interactor.
 * Maintains the current step index for the active recipe instructions.
 */
public class StepByStepController {
    private final StepByStepInputBoundary interactor;

    // The complete set of instructions for the current recipe
    private final RecipeInstructions instructions;

    // Tracks which step the user is currently viewing (0-based index)
    private int currentStepIndex;

    /**
     * Constructs a new StepByStepController.
     * @param interactor the use case interactor that handles step navigation logic
     * @param instructions the recipe instructions to navigate through
     */
    public StepByStepController(StepByStepInputBoundary interactor,
                                RecipeInstructions instructions) {
        this.interactor = interactor;
        this.instructions = instructions;
    }

    /**
     * Starts the step-by-step navigation from the beginning.
     * Initializes the step index to 0 and displays the first step.
     */
    public void start() {
        // Begin at the first step
        currentStepIndex = 0;
        interactor.execute(new StepByStepInputData(instructions, currentStepIndex));
    }

    /**
     * Advances to the next instruction step.
     * Increments the step index and updates the view.
     */
    public void nextStep() {
        // Move forward one step
        currentStepIndex++;
        interactor.execute(new StepByStepInputData(instructions, currentStepIndex));
    }

    /**
     * Returns to the previous instruction step.
     * Decrements the step index and updates the view.
     */
    public void previousStep() {
        // Move back one step
        currentStepIndex--;
        interactor.execute(new StepByStepInputData(instructions, currentStepIndex));
    }
}
