package WhereToWalk.sorting;

import WhereToWalk.Hill;
import java.util.Comparator;
import WhereToWalk.Day;

public class HighestScore implements Comparator<Hill> {
    /*
     * Compare the scores between two hills and return whether
     * they are equal, or which has a higher/lower score
     */
    @Override
    public int compare(Hill h1, Hill h2) {
        int day = Day.getSelectedDay().getNumber();
        return (int) Math.floor(h2.getApproximateScore(day)-h1.getApproximateScore(day));
    }
}
