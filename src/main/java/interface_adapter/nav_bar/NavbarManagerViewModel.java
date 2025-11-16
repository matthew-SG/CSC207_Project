package interface_adapter.nav_bar;

import interface_adapter.ViewModel;

public class NavbarManagerViewModel extends ViewModel<String> {
    public static final String UNLOGGED_IN = "UNLOGGED_IN";
    public static final String LOGGED_IN = "LOGGED_IN";
    public NavbarManagerViewModel() {
        super("nav bar manager");
        setState(LOGGED_IN);
    }
}
