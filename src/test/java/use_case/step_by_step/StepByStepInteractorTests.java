package use_case.step_by_step;

import entities.InstructionStep;
import entities.RecipeInstructions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StepByStepInteractorTest {

    private MockStepByStepPresenter mockPresenter;
    private StepByStepInteractor interactor;
    private List<InstructionStep> sampleSteps;

    @BeforeEach
    void setUp() {
        mockPresenter = new MockStepByStepPresenter();
        interactor = new StepByStepInteractor(mockPresenter);

        // Create sample steps for testing
        sampleSteps = List.of(
                new InstructionStep(1, "Heat the oven to 350°F", List.of(), List.of()),
                new InstructionStep(2, "Mix the ingredients", List.of(), List.of()),
                new InstructionStep(3, "Pour into pan", List.of(), List.of()),
                new InstructionStep(4, "Bake for 30 minutes", List.of(), List.of())
        );
    }

    // ==================== INITIALIZATION TESTS ====================

    @Test
    void testExecuteWithValidSteps() {
        // Arrange
        RecipeInstructions instructions = new RecipeInstructions(sampleSteps);
        StepByStepInputData inputData = new StepByStepInputData(instructions, 0);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isPresentCalled(), "Present should be called");
        assertFalse(mockPresenter.isFailViewCalled(), "Fail view should not be called");

        StepByStepOutputData output = mockPresenter.getLastOutputData();
        assertEquals("Heat the oven to 350°F", output.stepText());
        assertEquals(1, output.stepNumber());
        assertTrue(output.hasNext());
        assertFalse(output.hasPrevious());
    }

    @Test
    void testExecuteWithEmptySteps() {
        // Arrange
        RecipeInstructions emptyInstructions = new RecipeInstructions(Collections.emptyList());
        StepByStepInputData inputData = new StepByStepInputData(emptyInstructions, 0);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailViewCalled(), "Fail view should be called for empty steps");
        assertEquals("There are no steps", mockPresenter.getErrorMessage());
        assertFalse(mockPresenter.isPresentCalled(), "Present should not be called for empty steps");
    }

    @Test
    void testExecuteWithNullSteps() {
        // Arrange
        RecipeInstructions nullInstructions = new RecipeInstructions(null);
        StepByStepInputData inputData = new StepByStepInputData(nullInstructions, 0);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailViewCalled(), "Fail view should be called for null steps");
        assertEquals("There are no steps", mockPresenter.getErrorMessage());
        assertFalse(mockPresenter.isPresentCalled(), "Present should not be called for null steps");
    }

    @Test
    void testExecuteWithNegativeIndex() {
        // Arrange
        RecipeInstructions instructions = new RecipeInstructions(sampleSteps);
        StepByStepInputData inputData = new StepByStepInputData(instructions, -1);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailViewCalled(), "Fail view should be called for negative index");
        assertEquals("Invalid step index", mockPresenter.getErrorMessage());
        assertFalse(mockPresenter.isPresentCalled(), "Present should not be called for invalid index");
    }

    @Test
    void testExecuteWithIndexTooLarge() {
        // Arrange
        RecipeInstructions instructions = new RecipeInstructions(sampleSteps);
        StepByStepInputData inputData = new StepByStepInputData(instructions, 10);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailViewCalled(), "Fail view should be called for index too large");
        assertEquals("Invalid step index", mockPresenter.getErrorMessage());
        assertFalse(mockPresenter.isPresentCalled(), "Present should not be called for invalid index");
    }

    @Test
    void testExecuteStartingAtMiddleStep() {
        // Arrange
        RecipeInstructions instructions = new RecipeInstructions(sampleSteps);
        StepByStepInputData inputData = new StepByStepInputData(instructions, 2);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isPresentCalled(), "Present should be called");

        StepByStepOutputData output = mockPresenter.getLastOutputData();
        assertEquals("Pour into pan", output.stepText());
        assertEquals(3, output.stepNumber());
        assertTrue(output.hasNext());
        assertTrue(output.hasPrevious());
    }

    @Test
    void testExecuteStartingAtLastStep() {
        // Arrange
        RecipeInstructions instructions = new RecipeInstructions(sampleSteps);
        StepByStepInputData inputData = new StepByStepInputData(instructions, 3);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isPresentCalled(), "Present should be called");

        StepByStepOutputData output = mockPresenter.getLastOutputData();
        assertEquals("Bake for 30 minutes", output.stepText());
        assertEquals(4, output.stepNumber());
        assertFalse(output.hasNext());
        assertTrue(output.hasPrevious());
    }

    // ==================== NEXT STEP TESTS ====================

    @Test
    void testNextStepFromFirstStep() {
        // Arrange
        RecipeInstructions instructions = new RecipeInstructions(sampleSteps);
        StepByStepInputData inputData = new StepByStepInputData(instructions, 0);
        interactor.execute(inputData);
        mockPresenter.reset();

        // Act
        interactor.nextStep();

        // Assert
        assertTrue(mockPresenter.isPresentCalled(), "Present should be called");

        StepByStepOutputData output = mockPresenter.getLastOutputData();
        assertEquals("Mix the ingredients", output.stepText());
        assertEquals(2, output.stepNumber());
        assertTrue(output.hasNext());
        assertTrue(output.hasPrevious());
    }

    @Test
    void testNextStepToLastStep() {
        // Arrange
        RecipeInstructions instructions = new RecipeInstructions(sampleSteps);
        StepByStepInputData inputData = new StepByStepInputData(instructions, 2);
        interactor.execute(inputData);
        mockPresenter.reset();

        // Act
        interactor.nextStep();

        // Assert
        assertTrue(mockPresenter.isPresentCalled(), "Present should be called");

        StepByStepOutputData output = mockPresenter.getLastOutputData();
        assertEquals("Bake for 30 minutes", output.stepText());
        assertEquals(4, output.stepNumber());
        assertFalse(output.hasNext());
        assertTrue(output.hasPrevious());
    }

    @Test
    void testNextStepAtLastStep() {
        // Arrange
        RecipeInstructions instructions = new RecipeInstructions(sampleSteps);
        StepByStepInputData inputData = new StepByStepInputData(instructions, 3);
        interactor.execute(inputData);
        mockPresenter.reset();

        // Act
        interactor.nextStep();

        // Assert
        assertFalse(mockPresenter.isPresentCalled(), "Present should not be called when at last step");
    }

    @Test
    void testMultipleNextSteps() {
        // Arrange
        RecipeInstructions instructions = new RecipeInstructions(sampleSteps);
        StepByStepInputData inputData = new StepByStepInputData(instructions, 0);
        interactor.execute(inputData);

        // Act & Assert - Step 1 to 2
        mockPresenter.reset();
        interactor.nextStep();
        assertEquals(2, mockPresenter.getLastOutputData().stepNumber());

        // Act & Assert - Step 2 to 3
        mockPresenter.reset();
        interactor.nextStep();
        assertEquals(3, mockPresenter.getLastOutputData().stepNumber());

        // Act & Assert - Step 3 to 4
        mockPresenter.reset();
        interactor.nextStep();
        assertEquals(4, mockPresenter.getLastOutputData().stepNumber());
        assertFalse(mockPresenter.getLastOutputData().hasNext());
    }

    // ==================== PREVIOUS STEP TESTS ====================

    @Test
    void testPreviousStepFromLastStep() {
        // Arrange
        RecipeInstructions instructions = new RecipeInstructions(sampleSteps);
        StepByStepInputData inputData = new StepByStepInputData(instructions, 3);
        interactor.execute(inputData);
        mockPresenter.reset();

        // Act
        interactor.previousStep();

        // Assert
        assertTrue(mockPresenter.isPresentCalled(), "Present should be called");

        StepByStepOutputData output = mockPresenter.getLastOutputData();
        assertEquals("Pour into pan", output.stepText());
        assertEquals(3, output.stepNumber());
        assertTrue(output.hasNext());
        assertTrue(output.hasPrevious());
    }

    @Test
    void testPreviousStepToFirstStep() {
        // Arrange
        RecipeInstructions instructions = new RecipeInstructions(sampleSteps);
        StepByStepInputData inputData = new StepByStepInputData(instructions, 1);
        interactor.execute(inputData);
        mockPresenter.reset();

        // Act
        interactor.previousStep();

        // Assert
        assertTrue(mockPresenter.isPresentCalled(), "Present should be called");

        StepByStepOutputData output = mockPresenter.getLastOutputData();
        assertEquals("Heat the oven to 350°F", output.stepText());
        assertEquals(1, output.stepNumber());
        assertTrue(output.hasNext());
        assertFalse(output.hasPrevious());
    }

    @Test
    void testPreviousStepAtFirstStep() {
        // Arrange
        RecipeInstructions instructions = new RecipeInstructions(sampleSteps);
        StepByStepInputData inputData = new StepByStepInputData(instructions, 0);
        interactor.execute(inputData);
        mockPresenter.reset();

        // Act
        interactor.previousStep();

        // Assert
        assertFalse(mockPresenter.isPresentCalled(), "Present should not be called when at first step");
    }

    @Test
    void testMultiplePreviousSteps() {
        // Arrange
        RecipeInstructions instructions = new RecipeInstructions(sampleSteps);
        StepByStepInputData inputData = new StepByStepInputData(instructions, 3);
        interactor.execute(inputData);

        // Act & Assert - Step 4 to 3
        mockPresenter.reset();
        interactor.previousStep();
        assertEquals(3, mockPresenter.getLastOutputData().stepNumber());

        // Act & Assert - Step 3 to 2
        mockPresenter.reset();
        interactor.previousStep();
        assertEquals(2, mockPresenter.getLastOutputData().stepNumber());

        // Act & Assert - Step 2 to 1
        mockPresenter.reset();
        interactor.previousStep();
        assertEquals(1, mockPresenter.getLastOutputData().stepNumber());
        assertFalse(mockPresenter.getLastOutputData().hasPrevious());
    }

    // ==================== NAVIGATION COMBINATION TESTS ====================

    @Test
    void testNavigationBackAndForth() {
        // Arrange
        RecipeInstructions instructions = new RecipeInstructions(sampleSteps);
        StepByStepInputData inputData = new StepByStepInputData(instructions, 0);
        interactor.execute(inputData);

        // Act & Assert - Forward to step 2
        mockPresenter.reset();
        interactor.nextStep();
        assertEquals(2, mockPresenter.getLastOutputData().stepNumber());

        // Act & Assert - Forward to step 3
        mockPresenter.reset();
        interactor.nextStep();
        assertEquals(3, mockPresenter.getLastOutputData().stepNumber());

        // Act & Assert - Back to step 2
        mockPresenter.reset();
        interactor.previousStep();
        assertEquals(2, mockPresenter.getLastOutputData().stepNumber());

        // Act & Assert - Forward to step 3 again
        mockPresenter.reset();
        interactor.nextStep();
        assertEquals(3, mockPresenter.getLastOutputData().stepNumber());

        // Act & Assert - Forward to step 4
        mockPresenter.reset();
        interactor.nextStep();
        assertEquals(4, mockPresenter.getLastOutputData().stepNumber());

        // Act & Assert - Back to step 3
        mockPresenter.reset();
        interactor.previousStep();
        assertEquals(3, mockPresenter.getLastOutputData().stepNumber());
    }

    // ==================== SINGLE STEP RECIPE TESTS ====================

    @Test
    void testSingleStepRecipe() {
        // Arrange
        List<InstructionStep> singleStep = List.of(
                new InstructionStep(1, "Only step", List.of(), List.of())
        );
        RecipeInstructions instructions = new RecipeInstructions(singleStep);
        StepByStepInputData inputData = new StepByStepInputData(instructions, 0);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isPresentCalled(), "Present should be called");

        StepByStepOutputData output = mockPresenter.getLastOutputData();
        assertEquals("Only step", output.stepText());
        assertEquals(1, output.stepNumber());
        assertFalse(output.hasNext(), "Should not have next step");
        assertFalse(output.hasPrevious(), "Should not have previous step");
    }

    @Test
    void testSingleStepRecipeNextButton() {
        // Arrange
        List<InstructionStep> singleStep = List.of(
                new InstructionStep(1, "Only step", List.of(), List.of())
        );
        RecipeInstructions instructions = new RecipeInstructions(singleStep);
        StepByStepInputData inputData = new StepByStepInputData(instructions, 0);
        interactor.execute(inputData);
        mockPresenter.reset();

        // Act
        interactor.nextStep();

        // Assert
        assertFalse(mockPresenter.isPresentCalled(), "Present should not be called - no next step available");
    }

    @Test
    void testSingleStepRecipePreviousButton() {
        // Arrange
        List<InstructionStep> singleStep = List.of(
                new InstructionStep(1, "Only step", List.of(), List.of())
        );
        RecipeInstructions instructions = new RecipeInstructions(singleStep);
        StepByStepInputData inputData = new StepByStepInputData(instructions, 0);
        interactor.execute(inputData);
        mockPresenter.reset();

        // Act
        interactor.previousStep();

        // Assert
        assertFalse(mockPresenter.isPresentCalled(), "Present should not be called - no previous step available");
    }

    // ==================== TWO STEP RECIPE TESTS ====================

    @Test
    void testTwoStepRecipeNavigation() {
        // Arrange
        List<InstructionStep> twoSteps = List.of(
                new InstructionStep(1, "First step", List.of(), List.of()),
                new InstructionStep(2, "Second step", List.of(), List.of())
        );
        RecipeInstructions instructions = new RecipeInstructions(twoSteps);
        StepByStepInputData inputData = new StepByStepInputData(instructions, 0);
        interactor.execute(inputData);

        // Assert initial state - Step 1
        StepByStepOutputData output = mockPresenter.getLastOutputData();
        assertEquals(1, output.stepNumber());
        assertTrue(output.hasNext());
        assertFalse(output.hasPrevious());

        // Act - Move to step 2
        mockPresenter.reset();
        interactor.nextStep();

        // Assert - Step 2
        output = mockPresenter.getLastOutputData();
        assertEquals(2, output.stepNumber());
        assertFalse(output.hasNext());
        assertTrue(output.hasPrevious());

        // Act - Move back to step 1
        mockPresenter.reset();
        interactor.previousStep();

        // Assert - Back to step 1
        output = mockPresenter.getLastOutputData();
        assertEquals(1, output.stepNumber());
        assertTrue(output.hasNext());
        assertFalse(output.hasPrevious());
    }

    // ==================== MOCK PRESENTER ====================

    /**
     * Mock presenter for testing that tracks all interactions
     */
    private static class MockStepByStepPresenter implements StepByStepOutputBoundary {
        private boolean presentCalled = false;
        private boolean failViewCalled = false;
        private String errorMessage = null;
        private StepByStepOutputData lastOutputData = null;

        @Override
        public void present(StepByStepOutputData outputData) {
            this.presentCalled = true;
            this.lastOutputData = outputData;
        }

        @Override
        public void prepareFailView(String error) {
            this.failViewCalled = true;
            this.errorMessage = error;
        }

        public boolean isPresentCalled() {
            return presentCalled;
        }

        public boolean isFailViewCalled() {
            return failViewCalled;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public StepByStepOutputData getLastOutputData() {
            return lastOutputData;
        }

        public void reset() {
            this.presentCalled = false;
            this.failViewCalled = false;
            this.errorMessage = null;
            this.lastOutputData = null;
        }
    }
}
