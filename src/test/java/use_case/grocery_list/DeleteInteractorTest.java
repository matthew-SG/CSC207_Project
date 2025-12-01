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

class DeleteInteractorTest {

    private static class MockDeletePresenter implements DeleteOutputBoundary {
        private boolean success = false;

        @Override
        public void present(DeleteOutputData outputData) {
            this.success = true;
        }

        public boolean isSuccess() {
            return success;
        }
    }

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
}
