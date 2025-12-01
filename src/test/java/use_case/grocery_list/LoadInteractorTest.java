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

class LoadInteractorTest {

    private static class MockLoadPresenter implements LoadOutputBoundary {
        private List<Ingredient> receivedItems = null;

        @Override
        public void present(LoadOutputData outputData) {
            this.receivedItems = outputData.items;
        }

        public List<Ingredient> getReceivedItems() {
            return receivedItems;
        }
    }

    @Test
    void testLoadSuccessNonEmpty() {

        Ingredient banana = new Ingredient("Banana", 5, "pcs");
        List<Ingredient> initialList = new ArrayList<>(List.of(banana));

        InMemoryUserDataAccessObject dao = TestSetup.setupLoggedInUser(initialList);
        MockLoadPresenter presenter = new MockLoadPresenter();
        LoadInteractor interactor = new LoadInteractor(dao, presenter);

        interactor.execute();

        List<Ingredient> loadedList = presenter.getReceivedItems();
        assertNotNull(loadedList);
        assertEquals(1, loadedList.size());
        assertEquals("Banana", loadedList.getFirst().getName());
    }

    @Test
    void testLoadSuccessEmptyList() {
        List<Ingredient> initialList = new ArrayList<>();

        InMemoryUserDataAccessObject dao = TestSetup.setupLoggedInUser(initialList);
        MockLoadPresenter presenter = new MockLoadPresenter();
        LoadInteractor interactor = new LoadInteractor(dao, presenter);

        interactor.execute();

        List<Ingredient> loadedList = presenter.getReceivedItems();
        assertNotNull(loadedList);
        assertTrue(loadedList.isEmpty());
    }
}