//package use_case.community;
//
/**
 * Input Boundary for actions related to community features (e.g., creating, joining, or interacting with a community).
 */
public interface CommunityInputBoundary {
    /**
     * Executes the community use case.
     * @param inputData the input data for the community action
     */
    void execute(CommunityInputData inputData);
}