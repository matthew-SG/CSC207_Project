package use_case.grocery_list;

import data_access.InMemoryUserDataAccessObject;
import entities.Ingredient;
import org.junit.jupiter.api.Test;
import use_case.grocery_list.add.AddInputData;
import use_case.grocery_list.add.AddInteractor;
import use_case.grocery_list.add.AddOutputBoundary;
import use_case.grocery_list.add.AddOutputData;
import use_case.grocery_list.TestSetup;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MockAddPresenter implements AddOutputBoundary {
    private boolean success = false;

    @Override
    public void present(AddOutputData outputData) {
        this.success = true;
    }

    public boolean isSuccess() { return success; }
}

public class AddInteractorTest {

    @Test
    void testAddSuccessNewItem() {
        InMemoryUserDataAccessObject dao = TestSetup.setupLoggedInUser(new ArrayList<>());
        MockAddPresenter presenter = new MockAddPresenter();
        AddInteractor interactor = new AddInteractor(dao, presenter);

        AddInputData inputData = new AddInputData("Apple", "3", "pcs");

        interactor.execute(inputData);

        assertTrue(presenter.isSuccess(), "added");

        List<Ingredient> items = dao.getUsers().get(TestSetup.TEST_USERNAME).getGroceryList().getItems();
        assertEquals(1, items.size());
        Ingredient addedItem = items.get(0);
        assertEquals("Apple", addedItem.getName());
        assertEquals(3.0, addedItem.getQuantity());
    }
}