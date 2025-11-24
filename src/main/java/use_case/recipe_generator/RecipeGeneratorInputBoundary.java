package use_case.recipe_generator;

public interface RecipeGeneratorInputBoundary {
     void generateRecipes(GenerateRecipeInputData inputData);
}


// controllers call this interface rather than a concrete class (so we can make changes to the interactor class without having to change the controller code)
// future implementation (for example, RecipeGeneratorInteractor) take the users filters in GenerateRecipeInputData and return the list of matching recipes through GenerateRecipeOutputData.