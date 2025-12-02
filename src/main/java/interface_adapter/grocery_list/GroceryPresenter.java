package interface_adapter.grocery_list;

import interface_adapter.grocery_list.GroceryViewModel;
import interface_adapter.grocery_list.GroceryState;

import use_case.grocery_list.add.AddOutputBoundary;
import use_case.grocery_list.add.AddOutputData;

import use_case.grocery_list.edit.EditOutputBoundary;
import use_case.grocery_list.edit.EditOutputData;

import use_case.grocery_list.delete.DeleteOutputBoundary;
import use_case.grocery_list.delete.DeleteOutputData;

import use_case.grocery_list.load.LoadOutputBoundary;
import use_case.grocery_list.load.LoadOutputData;

/**
 * The Presenter for the Grocery List feature.
 * It implements all necessary Output Boundary interfaces and is responsible for
 * taking the output data from the use cases and formatting it for the ViewModel,
 * ultimately updating the UI state.
 */
public class GroceryPresenter implements
        AddOutputBoundary,
        EditOutputBoundary,
        DeleteOutputBoundary,
        LoadOutputBoundary {

    private final GroceryViewModel viewModel;

    /**
     * Constructs a GroceryPresenter.
     *
     * @param viewModel The ViewModel associated with the grocery list UI, which will be updated.
     */
    public GroceryPresenter(GroceryViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /**
     * A private helper method to update the GroceryState in the ViewModel.
     *
     * @param list The new list of ingredients (grocery items) to be displayed.
     */
    private void updateState(java.util.List<entities.Ingredient> list) {
        GroceryState s = new GroceryState();
        s.items = list;
        viewModel.setState(s);
    }

    /**
     * Presents the result of the Add Item use case.
     * It extracts the updated list from the output data and updates the ViewModel state.
     *
     * @param output The output data containing the new state of the grocery list after adding an item.
     */
    @Override
    public void present(AddOutputData output) {
        updateState(output.items);
    }

    /**
     * Presents the result of the Edit Item use case.
     * It extracts the updated list from the output data and updates the ViewModel state.
     *
     * @param output The output data containing the new state of the grocery list after editing an item.
     */
    @Override
    public void present(EditOutputData output) {
        updateState(output.items);
    }

    /**
     * Presents the result of the Delete Item use case.
     * It extracts the updated list from the output data and updates the ViewModel state.
     *
     * @param output The output data containing the new state of the grocery list after deleting an item.
     */
    @Override
    public void present(DeleteOutputData output) {
        updateState(output.items);
    }

    /**
     * Presents the result of the Load List use case.
     * It extracts the loaded list from the output data and updates the ViewModel state.
     *
     * @param output The output data containing the loaded grocery list.
     */
    @Override
    public void present(LoadOutputData output) {
        updateState(output.items);
    }
}
