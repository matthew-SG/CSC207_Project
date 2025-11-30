package use_case.recipe_generator;

/**
 * interface the interactor uses to send results of the Generate Recipes use case to the presenter. Implementations update the view model / UI based on GenerateRecipeOutputData
 */

 public interface RecipeGeneratorOutputBoundary {
    void prepareView(GenerateRecipeOutputData outputData);
    void presentError(String errorMessage);
}
