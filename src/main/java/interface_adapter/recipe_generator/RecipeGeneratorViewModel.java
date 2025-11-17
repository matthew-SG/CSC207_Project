package interface_adapter.recipe_generator;

import interface_adapter.ViewModel;

public class RecipeGeneratorViewModel extends ViewModel<RecipeGeneratorState> {
    public static final String viewName = "recipe generator";

    public RecipeGeneratorViewModel() {
        super(viewName);
        setState(new RecipeGeneratorState());
    }
}
