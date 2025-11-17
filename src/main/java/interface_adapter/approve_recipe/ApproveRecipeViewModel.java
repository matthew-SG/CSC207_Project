package interface_adapter.approve_recipe;

import interface_adapter.ViewModel;

/**
 * ViewModel for the approve recipe feature.
 */
public class ApproveRecipeViewModel extends ViewModel<ApproveRecipeState> {
    public static final String viewName = "approve recipe";

    public ApproveRecipeViewModel() {
        super(viewName);
        setState(new ApproveRecipeState());
    }
}
