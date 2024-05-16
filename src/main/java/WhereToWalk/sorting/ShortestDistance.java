package WhereToWalk.sorting;

import WhereToWalk.Hill;
import WhereToWalk.loc.LocationDistance;

import java.util.Comparator;

// Needs rework
public class ShortestDistance implements Comparator<Hill> {
    @Override
    public int compare(Hill h1, Hill h2) {
        return (int) Math.floor(LocationDistance.distanceFromUser(h1)-LocationDistance.distanceFromUser(h2));
    };

}
