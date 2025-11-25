package use_case.likedRecipeList;

// TODO Implement the interactor for the Liked Recipe List Use Case

/**
 * Interactor for the Liked Recipe List Use Case
 */
public class LikedRecipeInteractor implements LikedRecipeInputBoundary {
    private final LikedRecipeDataAccessInterface likedRecipeDataAccessInterface;
    private final LikedRecipeOutputBoundary likedRecipePresenter;

    public LikedRecipeInteractor(LikedRecipeDataAccessInterface likedRecipeDataAccessInterface,
                                 LikedRecipeOutputBoundary likedRecipePresenter) {
        this.likedRecipeDataAccessInterface = likedRecipeDataAccessInterface;
        this.likedRecipePresenter = likedRecipePresenter;
    }

    @Override
    public void executeAddLikedRecipe(AddLikedRecipeInputData addLikedRecipeInputData) {

    }

    @Override
    public void executeDeleteLikedRecipe(DeleteLikedRecipeInputData deleteLikedRecipeInputData) {

    }

    @Override
    public void executeHandsfree(HandsFreeRecipeInputData handsFreeRecipeInputData) {

    }
}
