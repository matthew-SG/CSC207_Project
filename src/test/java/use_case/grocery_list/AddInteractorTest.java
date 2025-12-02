package use_case.grocery_list;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import data_access.InMemoryUserDataAccessObject;
import entities.Ingredient;
import use_case.grocery_list.add.AddInputData;
import use_case.grocery_list.add.AddInteractor;
import use_case.grocery_list.add.AddOutputBoundary;
import use_case.grocery_list.add.AddOutputData;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for AddInteractor.
 * It verifies the business logic for adding a new ingredient to the user's grocery list.
 */
class AddInteractorTest {

    /**
     * Mock implementation of AddOutputBoundary to capture whether the presenter was successfully
     * called by the interactor.
     */
    private static class MockAddPresenter implements AddOutputBoundary {
        private boolean success = false;

        /**
         * Called by the interactor upon successful execution. Sets the success flag.
         *
         * @param outputData The output data from the interactor.
         */
        @Override
        public void present(AddOutputData outputData) {
            this.success = true;
        }

        /**
         * Checks if the presenter was called.
         *
         * @return {@code true} if present was called, false otherwise.
         */
        public boolean isSuccess() {
            return success;
        }
    }

    /**
     * Tests the scenario where a new item is successfully added to an empty grocery list.
     * It verifies the presenter is called, the item is persisted, and its details are correct.
     */
    @Test
    void testAddSuccessNewItem() {
        // Setup a logged-in user with an empty grocery list
        final InMemoryUserDataAccessObject dao = TestSetup.setupLoggedInUser(new ArrayList<>());
        final MockAddPresenter presenter = new MockAddPresenter();
        final AddInteractor interactor = new AddInteractor(dao, presenter);

        final AddInputData inputData = new AddInputData("Apple", "3", "pcs");

        interactor.execute(inputData);

        assertTrue(presenter.isSuccess(), "added");

        final List<Ingredient> items = dao.getUsers().get(TestSetup.TEST_USERNAME).getGroceryList().getItems();
        assertEquals(1, items.size());
        Ingredient addedItem = items.getFirst();
        assertEquals("Apple", addedItem.getName());
        assertEquals(3.0, addedItem.getQuantity());
    }
}
