package use_case.grocery_list;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import data_access.InMemoryUserDataAccessObject;
import entities.Ingredient;
import use_case.grocery_list.load.LoadInteractor;
import use_case.grocery_list.load.LoadOutputBoundary;
import use_case.grocery_list.load.LoadOutputData;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for LoadInteractor.
 * It verifies the business logic for loading the user's grocery list from the data repository.
 */
final class LoadInteractorTest {

    /**
     * Mock implementation of LoadOutputBoundary to capture the data received
     * by the presenter after the interactor executes.
     */
    private static class MockLoadPresenter implements LoadOutputBoundary {
        private List<Ingredient> receivedItems = null;

        /**
         * Captures the loaded list of items from the output data.
         *
         * @param outputData The output data containing the loaded list.
         */
        @Override
        public void present(LoadOutputData outputData) {
            this.receivedItems = outputData.items;
        }

        /**
         * Retrieves the list of items received during the test.
         *
         * @return The received list of Ingredient entities, or null if present was not called.
         */
        public List<Ingredient> getReceivedItems() {
            return receivedItems;
        }
    }

    /**
     * Tests the successful loading of a grocery list that contains one or more items.
     * Verifies that the presenter receives the correct list content.
     */
    @Test
    void testLoadSuccessNonEmpty() {

        final Ingredient banana = new Ingredient("Banana", 5, "pcs");
        final List<Ingredient> initialList = new ArrayList<>(List.of(banana));

        final InMemoryUserDataAccessObject dao = TestSetup.setupLoggedInUser(initialList);
        final MockLoadPresenter presenter = new MockLoadPresenter();
        final LoadInteractor interactor = new LoadInteractor(dao, presenter);

        interactor.execute();

        final List<Ingredient> loadedList = presenter.getReceivedItems();
        assertNotNull(loadedList);
        assertEquals(1, loadedList.size());
        assertEquals("Banana", loadedList.getFirst().getName());
    }

    /**
     * Tests the successful loading of a grocery list that is empty.
     * Verifies that the presenter receives an empty list.
     */
    @Test
    void testLoadSuccessEmptyList() {
        final List<Ingredient> initialList = new ArrayList<>();

        final InMemoryUserDataAccessObject dao = TestSetup.setupLoggedInUser(initialList);
        final MockLoadPresenter presenter = new MockLoadPresenter();
        final LoadInteractor interactor = new LoadInteractor(dao, presenter);

        interactor.execute();

        final List<Ingredient> loadedList = presenter.getReceivedItems();
        assertNotNull(loadedList);
        assertTrue(loadedList.isEmpty());
    }
}
