package interface_adapter.community;

import interface_adapter.ViewModel;

public class CommunityViewModel extends ViewModel<CommunityState> {
    public static final String VIEWING = "view";
    public static final String SELECTING_RECIPE = "select";
    public static final String WRITING_REVIEW = "write";
    public static final String PUB_SUCC = "succ_pub";
    public static final String viewName = "community";
    public CommunityViewModel() {
        super(viewName);
        setState(new CommunityState());
    }
}
