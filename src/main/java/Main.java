import javax.swing.*;

import data_access.JsonGroceryRepository;

import interface_adapter.grocery_list.GroceryViewModel;
import interface_adapter.grocery_list.GroceryPresenter;
import interface_adapter.grocery_list.GroceryController;

import use_case.grocery_list.add.AddInteractor;
import use_case.grocery_list.edit.EditInteractor;
import use_case.grocery_list.delete.DeleteInteractor;
import use_case.grocery_list.load.LoadInteractor;

import view.GroceryView;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String jsonPath = "grocery_list.json";
            JsonGroceryRepository repo = new JsonGroceryRepository(jsonPath);

            GroceryViewModel vm = new GroceryViewModel();
            GroceryPresenter presenter = new GroceryPresenter(vm);

            AddInteractor addUC = new AddInteractor(repo, presenter);
            EditInteractor editUC = new EditInteractor(repo, presenter);
            DeleteInteractor deleteUC = new DeleteInteractor(repo, presenter);
            LoadInteractor loadUC = new LoadInteractor(repo, presenter);

            GroceryController controller = new GroceryController(addUC, editUC, deleteUC, loadUC);

            JFrame frame = new JFrame("Grocery List");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 450);
            frame.add(new GroceryView(controller, vm));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
