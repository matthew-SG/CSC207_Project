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

    @Test
    void testEditFailure_NegativeIndex() {
        final Ingredient oldMilk = new Ingredient("Milk", 1, "L");
        final List<Ingredient> initialList = new ArrayList<>(List.of(oldMilk));

        final InMemoryUserDataAccessObject dao = TestSetup.setupLoggedInUser(initialList);
        final MockEditPresenter presenter = new MockEditPresenter();
        final EditInteractor interactor = new EditInteractor(dao, presenter);

        final EditInputData inputData = new EditInputData(-1, "Bread", "2", "Loaves");

        interactor.execute(inputData);

        assertTrue(presenter.isSuccess(), "Presenter should still be called");
        final List<Ingredient> items = dao.getUsers().get(TestSetup.TEST_USERNAME).getGroceryList().getItems();

        assertEquals(1, items.size());
        assertEquals("Milk", items.getFirst().getName(), "Item should not have been edited");
    }

    @Test
    void testEditFailure_IndexTooLarge() {
        final Ingredient oldMilk = new Ingredient("Milk", 1, "L");
        final List<Ingredient> initialList = new ArrayList<>(List.of(oldMilk));

        final InMemoryUserDataAccessObject dao = TestSetup.setupLoggedInUser(initialList);
        final MockEditPresenter presenter = new MockEditPresenter();
        final EditInteractor interactor = new EditInteractor(dao, presenter);

        final EditInputData inputData = new EditInputData(1, "Bread", "2", "Loaves");

        interactor.execute(inputData);

        assertTrue(presenter.isSuccess(), "Presenter should still be called");
        final List<Ingredient> items = dao.getUsers().get(TestSetup.TEST_USERNAME).getGroceryList().getItems();

        assertEquals(1, items.size());
        assertEquals("Milk", items.getFirst().getName(), "Item should not have been edited");
    }

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
