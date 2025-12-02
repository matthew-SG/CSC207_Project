package use_case.search_by_ingr;

/**
 * Output boundary for the Search-By-Ingredient use case.
 * Defines how success and failure results are passed to the presenter.
 */
public interface SearchByIngredientOutputBoundary {

    /**
     * Sends the successful search results to the presenter.
     *
     * @param outputData the data containing recipes and status message
     */
    void prepareSuccessView(SearchByIngredientOutputData outputData);

    /**
     * Sends an error message to the presenter when the search fails.
     *
     * @param error the error message to display
     */
    void prepareFailView(String error);
}
