package interface_adapter.recipe_generator;

import interface_adapter.ViewManagerModel;
import use_case.recipe_generator.GenerateRecipeOutputData;
import use_case.recipe_generator.RecipeGeneratorOutputBoundary;

// class responsible for implementing the output boundary takes the output data from the interactor
// and updates the ViewModel state accordingly by firing property changes so the swing view refreshes

// Clean Architecture: this presenter adapts use-case output into the RecipeGeneratorViewModel
// and global ViewManagerModel, without depending on UI widgets or data-access.

public class RecipeGeneratorPresenter implements RecipeGeneratorOutputBoundary {
    private final RecipeGeneratorViewModel recipeViewModel;
    private final ViewManagerModel viewManagerModel;

    public RecipeGeneratorPresenter(RecipeGeneratorViewModel recipeViewModel, ViewManagerModel viewManagerModel) {
        this.recipeViewModel = recipeViewModel; //holds recipeGeneratorState (recipes + message) and fires property changes for the view
        this.viewManagerModel = viewManagerModel; // used for global UI actions such as error popups
    }

    //
    @Override
    public void prepareView(GenerateRecipeOutputData outputData) {
        RecipeGeneratorState state = recipeViewModel.getState(); // pull the current state object for this screen
        state.setRecipes(outputData.getRecipes()); // replace the current recipes in state with the new recipeSummary list (what will be shown in J list)
        state.setMessage(outputData.getMessage()); // updates state message field depending on situation (ex on success message = "")
        recipeViewModel.firePropertyChange();

        String msg = outputData.getMessage(); // store message here for checking
        if (msg != null && !msg.isEmpty()) {
            viewManagerModel.showsErrorMessage(msg);
        }

        viewManagerModel.getState().viewName = recipeViewModel.getViewName();
        viewManagerModel.firePropertyChange();
    }
    // this is the explicit error path
    @Override
    public void presentError(String errorMessage) {
        RecipeGeneratorState state = recipeViewModel.getState();
        state.setMessage(errorMessage);
        recipeViewModel.firePropertyChange();

        viewManagerModel.showsErrorMessage(errorMessage);
    }




}
