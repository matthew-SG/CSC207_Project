package use_case.grocery_list;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import data_access.InMemoryUserDataAccessObject;
import entities.Ingredient;
import use_case.grocery_list.delete.DeleteInputData;
import use_case.grocery_list.delete.DeleteInteractor;
import use_case.grocery_list.delete.DeleteOutputBoundary;
import use_case.grocery_list.delete.DeleteOutputData;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for DeleteInteractor.
 * It verifies the business logic for deleting an ingredient from the user's grocery list
 * based on its index.
 */
class DeleteInteractorTest {

    /**
     * Mock implementation of DeleteOutputBoundary to capture whether the presenter was successfully
     * called by the interactor.
     */
    private static class MockDeletePresenter implements DeleteOutputBoundary {
        private boolean success = false;

        /**
         * Called by the interactor upon execution completion. Sets the success flag.
         *
         * @param outputData The output data from the interactor.
         */
        @Override
        public void present(DeleteOutputData outputData) {
            this.success = true;
        }

        /**
         * Checks if the presenter was called.
         *
         * @return true if present was called, false otherwise.
         */
        public boolean isSuccess() {
            return success;
        }
    }

    /**
     * Tests the successful deletion of an item from the grocery list.
     * It verifies the presenter is called and the item is correctly removed and persisted.
     */
    @Test
    void testDeleteSuccess() {
        final Ingredient milk = new Ingredient("Milk", 1, "L");
        final Ingredient eggs = new Ingredient("Eggs", 12, "pcs");
        final List<Ingredient> initialList = new ArrayList<>(List.of(milk, eggs));

        final InMemoryUserDataAccessObject dao = TestSetup.setupLoggedInUser(initialList);
        final MockDeletePresenter presenter = new MockDeletePresenter();
        final DeleteInteractor interactor = new DeleteInteractor(dao, presenter);

        final DeleteInputData inputData = new DeleteInputData(0);

        interactor.execute(inputData);

        assertTrue(presenter.isSuccess(), "delete");
        final List<Ingredient> items = dao.getUsers().get(TestSetup.TEST_USERNAME).getGroceryList().getItems();

        assertEquals(1, items.size());
        assertEquals("Eggs", items.getFirst().getName());
    }

    /**
     * Tests the scenario where deletion is attempted with an invalid negative index.
     * The item should not be deleted, but the presenter must still be called with the unchanged list.
     */
    @Test
    void testDeleteFailure_NegativeIndex() {
        final Ingredient milk = new Ingredient("Milk", 1, "L");
        final List<Ingredient> initialList = new ArrayList<>(List.of(milk));

        final InMemoryUserDataAccessObject dao = TestSetup.setupLoggedInUser(initialList);
        final MockDeletePresenter presenter = new MockDeletePresenter();
        final DeleteInteractor interactor = new DeleteInteractor(dao, presenter);

        final DeleteInputData inputData = new DeleteInputData(-1);

        interactor.execute(inputData);

        assertTrue(presenter.isSuccess(), "Presenter should still be called");
        final List<Ingredient> items = dao.getUsers().get(TestSetup.TEST_USERNAME).getGroceryList().getItems();

        assertEquals(1, items.size());
        assertEquals("Milk", items.getFirst().getName(), "Item should not have been deleted");
    }

    /**
     * Tests the scenario where deletion is attempted with an index that is out of bounds (too large).
     * The item should not be deleted, but the presenter must still be called with the unchanged list.
     */
    @Test
    void testDeleteFailure_IndexTooLarge() {
        final Ingredient milk = new Ingredient("Milk", 1, "L");
        final List<Ingredient> initialList = new ArrayList<>(List.of(milk));

        final InMemoryUserDataAccessObject dao = TestSetup.setupLoggedInUser(initialList);
        final MockDeletePresenter presenter = new MockDeletePresenter();
        final DeleteInteractor interactor = new DeleteInteractor(dao, presenter);

        final DeleteInputData inputData = new DeleteInputData(1);

        interactor.execute(inputData);

        assertTrue(presenter.isSuccess(), "Presenter should still be called");
        final List<Ingredient> items = dao.getUsers().get(TestSetup.TEST_USERNAME).getGroceryList().getItems();

        assertEquals(1, items.size());
        assertEquals("Milk", items.getFirst().getName(), "Item should not have been deleted");
    }
}
