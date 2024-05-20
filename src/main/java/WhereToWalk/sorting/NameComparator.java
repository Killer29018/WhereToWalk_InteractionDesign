package WhereToWalk.sorting;

import WhereToWalk.Hill;
import java.util.Comparator;
import WhereToWalk.Day;

public class NameComparator implements Comparator<Hill> {
    /*
     * Compare the scores between two hills and return whether
     * they are equal, or which has a higher/lower score
     */
    @Override
    public int compare(Hill h1, Hill h2) {
        return h1.getName().compareTo(h2.getName());
    }
}
