package interface_adapter.recipe_generator;

import interface_adapter.ViewModel;

// Clean Architecture: this view model holds RecipeGeneratorState and exposes it to the view while being updated by the presenter.
// this class is responsible for giving the presenter a way to set and update the state
// and also giving the view a way to observe these state changes through the general viewModel
// class which has property change support

public class RecipeGeneratorViewModel extends ViewModel<RecipeGeneratorState> {
    public static final String viewName = "recipe generator";

    public RecipeGeneratorViewModel() {
        super(viewName);
        setState(new RecipeGeneratorState());
    }
}
