package use_case.grocery_list;

import data_access.InMemoryUserDataAccessObject;
import entities.Ingredient;
import org.junit.jupiter.api.Test;
import use_case.grocery_list.delete.DeleteInputData;
import use_case.grocery_list.delete.DeleteInteractor;
import use_case.grocery_list.delete.DeleteOutputBoundary;
import use_case.grocery_list.delete.DeleteOutputData;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MockDeletePresenter implements DeleteOutputBoundary {
    private boolean success = false;

    @Override
    public void present(DeleteOutputData outputData) {
        this.success = true;
    }

    public boolean isSuccess() {
        return success;
    }
}

class DeleteInteractorTest {

    @Test
    void testDeleteSuccess() {
        Ingredient milk = new Ingredient("Milk", 1, "L");
        Ingredient eggs = new Ingredient("Eggs", 12, "pcs");
        List<Ingredient> initialList = new ArrayList<>(List.of(milk, eggs));

        InMemoryUserDataAccessObject dao = TestSetup.setupLoggedInUser(initialList);
        MockDeletePresenter presenter = new MockDeletePresenter();
        DeleteInteractor interactor = new DeleteInteractor(dao, presenter);

        DeleteInputData inputData = new DeleteInputData(0);

        interactor.execute(inputData);

        assertTrue(presenter.isSuccess(), "delete");
        List<Ingredient> items = dao.getUsers().get(TestSetup.TEST_USERNAME).getGroceryList().getItems();

        assertEquals(1, items.size());
        assertEquals("Eggs", items.getFirst().getName());
    }
}