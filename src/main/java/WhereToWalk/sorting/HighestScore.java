package WhereToWalk.sorting;

import WhereToWalk.Hill;
import java.util.Comparator;
import WhereToWalk.Day;

public class HighestScore implements Comparator<Hill> {
    @Override
    public int compare(Hill h1, Hill h2) {
        int day = Day.getSelectedDay().getNumber();
        return (int) Math.floor(h2.getApproximateScore(day)-h1.getApproximateScore(day));
    }
}
