package use_case.grocery_list;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import data_access.InMemoryUserDataAccessObject;
import entities.Ingredient;
import use_case.grocery_list.edit.EditInputData;
import use_case.grocery_list.edit.EditInteractor;
import use_case.grocery_list.edit.EditOutputBoundary;
import use_case.grocery_list.edit.EditOutputData;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for EditInteractor.
 * It verifies the business logic for modifying an existing ingredient in the user's grocery list
 * based on its index and new details.
 */
class EditInteractorTest {

    /**
     * Mock implementation of EditOutputBoundary to capture whether the presenter was successfully
     * called by the interactor.
     */
    private static class MockEditPresenter implements EditOutputBoundary {
        private boolean success = false;

        /**
         * Called by the interactor upon execution completion. Sets the success flag.
         *
         * @param outputData The output data from the interactor.
         */
        @Override
        public void present(EditOutputData outputData) {
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
     * Tests the successful editing of an item in the grocery list.
     * It verifies the presenter is called, the item's details are updated, and the list is persisted correctly.
     */
    @Test
    void testEditSuccess() {
        final Ingredient oldMilk = new Ingredient("Milk", 1, "L");
        final Ingredient eggs = new Ingredient("Eggs", 12, "pcs");
        final List<Ingredient> initialList = new ArrayList<>(List.of(oldMilk, eggs));

        final InMemoryUserDataAccessObject dao = TestSetup.setupLoggedInUser(initialList);
        final MockEditPresenter presenter = new MockEditPresenter();
        final EditInteractor interactor = new EditInteractor(dao, presenter);

        final EditInputData inputData = new EditInputData(0, "Bread", "2", "Loaves");

        interactor.execute(inputData);

        assertTrue(presenter.isSuccess(), "edit");
        final List<Ingredient> items = dao.getUsers().get(TestSetup.TEST_USERNAME).getGroceryList().getItems();

        Ingredient editedItem = items.getFirst();
        assertEquals("Bread", editedItem.getName());
        assertEquals(2.0, editedItem.getQuantity());
        assertEquals("Loaves", editedItem.getUnit());

        assertEquals(2, items.size());
        assertEquals("Eggs", items.get(1).getName());
    }

    /**
     * Tests the scenario where editing is attempted with an invalid negative index.
     * The item should not be changed, but the presenter must still be called with the unchanged list.
     */
    @Test
    void testEditFailure_NegativeIndex() {
        final Ingredient oldMilk = new Ingredient("Milk", 1, "L");
        final List<Ingredient> initialList = new ArrayList<>(List.of(oldMilk));

        final InMemoryUserDataAccessObject dao = TestSetup.setupLoggedInUser(initialList);
        final MockEditPresenter presenter = new MockEditPresenter();
        final EditInteractor interactor = new EditInteractor(dao, presenter);

        // Input data with invalid negative index
        final EditInputData inputData = new EditInputData(-1, "Bread", "2", "Loaves");

        interactor.execute(inputData);

        assertTrue(presenter.isSuccess(), "Presenter should still be called");
        final List<Ingredient> items = dao.getUsers().get(TestSetup.TEST_USERNAME).getGroceryList().getItems();

        // Assert the item remains unchanged
        assertEquals(1, items.size());
        assertEquals("Milk", items.getFirst().getName(), "Item should not have been edited");
    }

    /**
     * Tests the scenario where editing is attempted with an index that is out of bounds (too large).
     * The item should not be changed, but the presenter must still be called with the unchanged list.
     */
    @Test
    void testEditFailure_IndexTooLarge() {
        final Ingredient oldMilk = new Ingredient("Milk", 1, "L");
        final List<Ingredient> initialList = new ArrayList<>(List.of(oldMilk));

        final InMemoryUserDataAccessObject dao = TestSetup.setupLoggedInUser(initialList);
        final MockEditPresenter presenter = new MockEditPresenter();
        final EditInteractor interactor = new EditInteractor(dao, presenter);

        // Input data with index out of bounds (size is 1, so index 1 is invalid)
        final EditInputData inputData = new EditInputData(1, "Bread", "2", "Loaves");

        interactor.execute(inputData);

        assertTrue(presenter.isSuccess(), "Presenter should still be called");
        final List<Ingredient> items = dao.getUsers().get(TestSetup.TEST_USERNAME).getGroceryList().getItems();

        // Assert the item remains unchanged
        assertEquals(1, items.size());
        assertEquals("Milk", items.getFirst().getName(), "Item should not have been edited");
    }

    /**
     * Tests the scenario where editing is attempted with a quantity string that cannot be parsed into a number.
     * The interactor is expected to throw a NumberFormatException as it does not contain
     * logic to handle invalid quantity formats.
     */
    @Test
    void testEditFailure_InvalidQuantityFormat() {
        final Ingredient oldMilk = new Ingredient("Milk", 1, "L");
        final List<Ingredient> initialList = new ArrayList<>(List.of(oldMilk));

        final InMemoryUserDataAccessObject dao = TestSetup.setupLoggedInUser(initialList);
        final MockEditPresenter presenter = new MockEditPresenter();
        final EditInteractor interactor = new EditInteractor(dao, presenter);

        final EditInputData inputData = new EditInputData(0, "Bread", "ABC", "Loaves");

        org.junit.jupiter.api.Assertions.assertThrows(NumberFormatException.class, () -> interactor.execute(inputData),
                "Interactor should throw NFE without a Try-Catch block");
    }
}
