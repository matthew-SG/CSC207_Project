package use_case.step_by_step;

import entities.InstructionStep;
import entities.RecipeInstructions;
import interface_adapter.speech.SpeechService;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StepByStepInteractorTests {

    private MockStepByStepPresenter mockPresenter;
    private MockSpeechService mockSpeechService;
    private StepByStepInteractor interactor;
    private List<InstructionStep> sampleSteps;

    @BeforeEach
    void setUp() {
        mockPresenter = new MockStepByStepPresenter();
        mockSpeechService = new MockSpeechService();
        interactor = new StepByStepInteractor(mockPresenter, mockSpeechService);

        // Create sample steps for testing
        sampleSteps = List.of(
                new InstructionStep(1, "Heat the oven to 350°F"),
                new InstructionStep(2, "Mix the ingredients"),
                new InstructionStep(3, "Pour into pan"),
                new InstructionStep(4, "Bake for 30 minutes")
        );
    }

    // ==================== EXECUTE (NAVIGATION) TESTS ====================

    @Test
    void testExecuteWithValidFirstStep() {
        // Arrange
        RecipeInstructions instructions = new RecipeInstructions(sampleSteps);
        StepByStepInputData inputData = new StepByStepInputData(instructions, 0);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isSuccessViewCalled(), "Success view should be called");
        assertFalse(mockPresenter.isFailViewCalled(), "Fail view should not be called");

        StepByStepOutputData output = mockPresenter.getLastOutputData();
        assertEquals("Heat the oven to 350°F", output.getStepText());
        assertEquals(1, output.getStepNumber());
        assertTrue(output.canGoNext());
        assertFalse(output.canGoPrevious());
    }

    @Test
    void testExecuteWithValidMiddleStep() {
        // Arrange
        RecipeInstructions instructions = new RecipeInstructions(sampleSteps);
        StepByStepInputData inputData = new StepByStepInputData(instructions, 2);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isSuccessViewCalled());

        StepByStepOutputData output = mockPresenter.getLastOutputData();
        assertEquals("Pour into pan", output.getStepText());
        assertEquals(3, output.getStepNumber());
        assertTrue(output.canGoNext());
        assertTrue(output.canGoPrevious());
    }

    @Test
    void testExecuteWithValidLastStep() {
        // Arrange
        RecipeInstructions instructions = new RecipeInstructions(sampleSteps);
        StepByStepInputData inputData = new StepByStepInputData(instructions, 3);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isSuccessViewCalled());

        StepByStepOutputData output = mockPresenter.getLastOutputData();
        assertEquals("Bake for 30 minutes", output.getStepText());
        assertEquals(4, output.getStepNumber());
        assertFalse(output.canGoNext());
        assertTrue(output.canGoPrevious());
    }

    @Test
    void testExecuteWithNegativeIndex() {
        // Arrange
        RecipeInstructions instructions = new RecipeInstructions(sampleSteps);
        StepByStepInputData inputData = new StepByStepInputData(instructions, -1);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailViewCalled());
        assertEquals("Invalid step index", mockPresenter.getErrorMessage());
        assertFalse(mockPresenter.isSuccessViewCalled());
    }

    @Test
    void testExecuteWithIndexTooLarge() {
        // Arrange
        RecipeInstructions instructions = new RecipeInstructions(sampleSteps);
        StepByStepInputData inputData = new StepByStepInputData(instructions, 10);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isFailViewCalled());
        assertEquals("Invalid step index", mockPresenter.getErrorMessage());
        assertFalse(mockPresenter.isSuccessViewCalled());
    }

    @Test
    void testExecuteWithSingleStep() {
        // Arrange
        List<InstructionStep> singleStep = List.of(
                new InstructionStep(1, "Only step")
        );
        RecipeInstructions instructions = new RecipeInstructions(singleStep);
        StepByStepInputData inputData = new StepByStepInputData(instructions, 0);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(mockPresenter.isSuccessViewCalled());

        StepByStepOutputData output = mockPresenter.getLastOutputData();
        assertEquals("Only step", output.getStepText());
        assertEquals(1, output.getStepNumber());
        assertFalse(output.canGoNext());
        assertFalse(output.canGoPrevious());
    }

    // ==================== EXECUTE SPEAK (TTS) TESTS ====================

    @Test
    void testExecuteSpeakWithValidStep() {
        // Arrange
        RecipeInstructions instructions = new RecipeInstructions(sampleSteps);
        StepByStepInputData inputData = new StepByStepInputData(instructions, 0);

        // Act
        interactor.executeSpeak(inputData);

        // Wait for background thread to complete
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            fail("Thread interrupted");
        }

        // Assert
        assertTrue(mockSpeechService.wasSynthesizeCalled());
        assertEquals("Heat the oven to 350°F", mockSpeechService.getLastTextSpoken());
        // No success view call - user can immediately speak again
        assertFalse(mockPresenter.isSpeakSuccessViewCalled());
    }

    @Test
    void testExecuteSpeakWithMiddleStep() {
        // Arrange
        RecipeInstructions instructions = new RecipeInstructions(sampleSteps);
        StepByStepInputData inputData = new StepByStepInputData(instructions, 2);

        // Act
        interactor.executeSpeak(inputData);

        // Wait for background thread
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            fail("Thread interrupted");
        }

        // Assert
        assertTrue(mockSpeechService.wasSynthesizeCalled());
        assertEquals("Pour into pan", mockSpeechService.getLastTextSpoken());
    }

    @Test
    void testExecuteSpeakWithInvalidIndex() {
        // Arrange
        RecipeInstructions instructions = new RecipeInstructions(sampleSteps);
        StepByStepInputData inputData = new StepByStepInputData(instructions, -1);

        // Act
        interactor.executeSpeak(inputData);

        // Assert - Validation happens before threading, no wait needed
        assertFalse(mockSpeechService.wasSynthesizeCalled());
        assertTrue(mockPresenter.isSpeakFailViewCalled());
        assertEquals("Invalid step index for TTS", mockPresenter.getSpeakErrorMessage());
    }

    @Test
    void testExecuteSpeakWithIndexTooLarge() {
        // Arrange
        RecipeInstructions instructions = new RecipeInstructions(sampleSteps);
        StepByStepInputData inputData = new StepByStepInputData(instructions, 10);

        // Act
        interactor.executeSpeak(inputData);

        // Assert - Validation is synchronous, happens before thread starts
        assertFalse(mockSpeechService.wasSynthesizeCalled(),
                "Speech service should not be called for invalid index");
        assertTrue(mockPresenter.isSpeakFailViewCalled(),
                "Speak fail view should be called for index >= steps.size()");
        assertEquals("Invalid step index for TTS", mockPresenter.getSpeakErrorMessage());
    }

    @Test
    void testExecuteSpeakWithTTSFailure() {
        // Arrange
        mockSpeechService.setShouldFail(true);
        RecipeInstructions instructions = new RecipeInstructions(sampleSteps);
        StepByStepInputData inputData = new StepByStepInputData(instructions, 0);

        // Act
        interactor.executeSpeak(inputData);

        // Wait for background thread
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            fail("Thread interrupted");
        }

        // Assert
        assertTrue(mockSpeechService.wasSynthesizeCalled());
        assertTrue(mockPresenter.isSpeakFailViewCalled());
        assertTrue(mockPresenter.getSpeakErrorMessage().contains("TTS error"));
    }

    // ==================== MOCK CLASSES ====================

    /**
     * Mock presenter for testing
     */
    private static class MockStepByStepPresenter implements StepByStepOutputBoundary {
        private boolean successViewCalled = false;
        private boolean failViewCalled = false;
        private boolean speakFailViewCalled = false;
        private String errorMessage = null;
        private String speakErrorMessage = null;
        private StepByStepOutputData lastOutputData = null;

        @Override
        public void prepareSuccessView(StepByStepOutputData outputData) {
            this.successViewCalled = true;
            this.lastOutputData = outputData;
        }

        @Override
        public void prepareFailView(String error) {
            this.failViewCalled = true;
            this.errorMessage = error;
        }

        @Override
        public void prepareSpeakFailView(String errorMessage) {
            this.speakFailViewCalled = true;
            this.speakErrorMessage = errorMessage;
        }

        public boolean isSuccessViewCalled() {
            return successViewCalled;
        }

        public boolean isFailViewCalled() {
            return failViewCalled;
        }

        public boolean isSpeakSuccessViewCalled() {
            // No longer used - removed from interface
            return false;
        }

        public boolean isSpeakFailViewCalled() {
            return speakFailViewCalled;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public String getSpeakErrorMessage() {
            return speakErrorMessage;
        }

        public StepByStepOutputData getLastOutputData() {
            return lastOutputData;
        }
    }

    /**
     * Mock speech service for testing
     */
    private static class MockSpeechService implements SpeechService {
        private boolean synthesizeCalled = false;
        private String lastTextSpoken = null;
        private boolean shouldFail = false;

        @Override
        public void synthesize(String text) throws Exception {
            this.synthesizeCalled = true;
            this.lastTextSpoken = text;

            if (shouldFail) {
                throw new Exception("Mock TTS failure");
            }
        }

        public boolean wasSynthesizeCalled() {
            return synthesizeCalled;
        }

        public String getLastTextSpoken() {
            return lastTextSpoken;
        }

        public void setShouldFail(boolean shouldFail) {
            this.shouldFail = shouldFail;
        }
    }
}