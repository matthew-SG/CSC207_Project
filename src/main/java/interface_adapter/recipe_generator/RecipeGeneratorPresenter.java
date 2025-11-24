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
        viewManagerModel.getState().viewName = recipeViewModel.getViewName();
        viewManagerModel.firePropertyChange();
    }
}
