package use_case.search_by_ingr;

public interface SearchByIngredientOutputBoundary {
    void prepareSuccessView(SearchByIngredientOutputData outputData);
    void prepareFailView(String error);
}
