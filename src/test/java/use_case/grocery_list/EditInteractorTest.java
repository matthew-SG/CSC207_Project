package use_case.grocery_list;

import data_access.InMemoryUserDataAccessObject;
import entities.Ingredient;
import org.junit.jupiter.api.Test;
import use_case.grocery_list.edit.EditInputData;
import use_case.grocery_list.edit.EditInteractor;
import use_case.grocery_list.edit.EditOutputBoundary;
import use_case.grocery_list.edit.EditOutputData;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EditInteractorTest {

    private static class MockEditPresenter implements EditOutputBoundary {
        private boolean success = false;

        @Override
        public void present(EditOutputData outputData) {
            this.success = true;
        }

        public boolean isSuccess() {
            return success;
        }
    }

    @Test
    void testEditSuccess() {
        Ingredient oldMilk = new Ingredient("Milk", 1, "L");
        Ingredient eggs = new Ingredient("Eggs", 12, "pcs");
        List<Ingredient> initialList = new ArrayList<>(List.of(oldMilk, eggs));

        InMemoryUserDataAccessObject dao = TestSetup.setupLoggedInUser(initialList);
        MockEditPresenter presenter = new MockEditPresenter();
        EditInteractor interactor = new EditInteractor(dao, presenter);

        EditInputData inputData = new EditInputData(0, "Bread", "2", "Loaves");

        interactor.execute(inputData);

        assertTrue(presenter.isSuccess(), "edit");
        List<Ingredient> items = dao.getUsers().get(TestSetup.TEST_USERNAME).getGroceryList().getItems();

        Ingredient editedItem = items.getFirst();
        assertEquals("Bread", editedItem.getName());
        assertEquals(2.0, editedItem.getQuantity());
        assertEquals("Loaves", editedItem.getUnit());

        assertEquals(2, items.size());
        assertEquals("Eggs", items.get(1).getName());
    }
}