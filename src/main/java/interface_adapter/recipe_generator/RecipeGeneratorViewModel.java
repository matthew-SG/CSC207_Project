package interface_adapter.recipe_generator;

import interface_adapter.ViewModel;

public class RecipeGeneratorViewModel extends ViewModel<RecipeGeneratorState> {
    public RecipeGeneratorViewModel() {
        super("recipe generator");
        setState(new RecipeGeneratorState());
    }
}
