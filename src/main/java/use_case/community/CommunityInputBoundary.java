package use_case.community;


import use_case.community.input_data.CommunityPoseSelectionInputData;
import use_case.community.input_data.CommunityPublishInputData;
import use_case.community.input_data.CommunityRecipeSelectionInputData;

/**
 * Input Boundary for actions which are related to logging in.
 */
public interface CommunityInputBoundary {
    void viewCommunity();

//    void likeRecipe();

    /**
     * Executes the view to view liked recipes.
     * @param data the input data
     */
    void viewToPost(CommunityPoseSelectionInputData data);

    /**
     * Executes the view to choose the recipe to pose use case.
     * @param data the input data
     */
    void selectRecipe(CommunityRecipeSelectionInputData data);

    void publish(CommunityPublishInputData data);
}