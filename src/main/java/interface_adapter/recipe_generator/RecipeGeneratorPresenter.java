package interface_adapter.recipe_generator;

import interface_adapter.ViewManagerModel;
import use_case.recipe_generator.GenerateRecipeOutputData;
import use_case.recipe_generator.RecipeGeneratorOutputBoundary;

public class RecipeGeneratorPresenter implements RecipeGeneratorOutputBoundary {
    private final RecipeGeneratorViewModel recipeViewModel;
    private final ViewManagerModel viewManagerModel;

    public RecipeGeneratorPresenter(RecipeGeneratorViewModel recipeViewModel, ViewManagerModel viewManagerModel) {
        this.recipeViewModel = recipeViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareView(GenerateRecipeOutputData outputData) {
        RecipeGeneratorState state = recipeViewModel.getState();
        state.setRecipes(outputData.getRecipes());
        state.setMessage(outputData.getMessage());
        recipeViewModel.firePropertyChange();

        String msg = outputData.getMessage();
        if (msg != null && !msg.isEmpty()) {
            viewManagerModel.showsErrorMessage(msg);
        }

        viewManagerModel.getState().viewName = recipeViewModel.getViewName();
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void presentError(String errorMessage) {
        RecipeGeneratorState state = recipeViewModel.getState();
        state.setMessage(errorMessage);
        recipeViewModel.firePropertyChange();

        viewManagerModel.showsErrorMessage(errorMessage);
    }




}
