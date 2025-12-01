package use_case.recipe_generator;


 //interface the interactor uses to send results of the Generate Recipes use case to the presenter. Implementations update the view model / UI based on GenerateRecipeOutputData
 // Clean Architecture: This is the outbound port from the use case layer to the presenter, keeping the use case layer independent of how UI is represented


 public interface RecipeGeneratorOutputBoundary {
    void prepareView(GenerateRecipeOutputData outputData);
    void presentError(String errorMessage);
}
