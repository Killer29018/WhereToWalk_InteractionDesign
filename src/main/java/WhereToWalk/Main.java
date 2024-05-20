package WhereToWalk;

import WhereToWalk.loc.*;

public class Main {
    public static void main(String[] args) {
        // Load the location of the user
        LocationFinder.loadLocation();

        // Load the main window of the application
        Window.main(args);
    }
}
