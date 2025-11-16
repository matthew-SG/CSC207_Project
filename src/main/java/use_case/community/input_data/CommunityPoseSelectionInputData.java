package use_case.community.input_data;


public class CommunityPoseSelectionInputData {

    private final boolean isLoggedIn;
    private final String userName;
    public CommunityPoseSelectionInputData(boolean isLoggedIn, String userName){
        this.isLoggedIn = isLoggedIn;
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }
}
