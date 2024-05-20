package WhereToWalk.sorting;

import WhereToWalk.Hill;
import WhereToWalk.loc.LocationDistance;

import java.util.Comparator;

public class ShortestDistance implements Comparator<Hill> {
    /*
     * Compare the distance between two hils and return wether the two hills are equal, or if
     * h1 is closer/further from h2
     */
    @Override
    public int compare(Hill h1, Hill h2) {
        return (int) Math.floor(LocationDistance.distanceFromUser(h1)-LocationDistance.distanceFromUser(h2));
    };

}
