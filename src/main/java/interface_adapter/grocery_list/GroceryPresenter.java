package interface_adapter.grocery_list;


import use_case.grocery_list.add.AddOutputBoundary;
import use_case.grocery_list.add.AddOutputData;

import use_case.grocery_list.edit.EditOutputBoundary;
import use_case.grocery_list.edit.EditOutputData;

import use_case.grocery_list.delete.DeleteOutputBoundary;
import use_case.grocery_list.delete.DeleteOutputData;

import use_case.grocery_list.load.LoadOutputBoundary;
import use_case.grocery_list.load.LoadOutputData;

public class GroceryPresenter implements
        AddOutputBoundary,
        EditOutputBoundary,
        DeleteOutputBoundary,
        LoadOutputBoundary {

    private final GroceryViewModel viewModel;

    public GroceryPresenter(GroceryViewModel viewModel) {
        this.viewModel = viewModel;
    }

    private void updateState(java.util.List<entities.Ingredient> list) {
        GroceryState s = new GroceryState();
        s.items = list;
        viewModel.setState(s);
    }

    @Override
    public void present(AddOutputData output) {
        updateState(output.items);
    }

    @Override
    public void present(EditOutputData output) {
        updateState(output.items);
    }

    @Override
    public void present(DeleteOutputData output) {
        updateState(output.items);
    }

    @Override
    public void present(LoadOutputData output) {
        updateState(output.items);
    }
}
