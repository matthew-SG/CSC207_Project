package use_case.search_by_ingr;

/**
 * Input boundary for the Search-By-Ingredient use case.
 * The controller calls this to start the use case.
 */
public interface SearchByIngredientInputBoundary {

    /**
     * Executes the use case using the provided input data.
     *
     * @param inputData the user input packaged for the interactor
     */
    void execute(SearchByIngredientInputData inputData);
}
