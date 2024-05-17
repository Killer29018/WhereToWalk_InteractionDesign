package WhereToWalk.predicates;

import WhereToWalk.Day;
import WhereToWalk.Hill;
import WhereToWalk.loc.LocationDistance;

import java.util.function.Predicate;

public class Predicates {
    protected static Predicate<Hill> distance(int from, int to) { // distance in km
        return h -> {
            double distance = LocationDistance.distanceFromUser(h);
            return from<=distance && distance<=to;
        };
    }

    protected static Predicate<Hill> altitude(int from, int to) { // altitude in m
        return h -> from<=h.getAltitude() && h.getAltitude()<=to;
    }

    protected static Predicate<Hill> score(int from, int to) { // score 0-100
        return h -> from<=h.getApproximateScore(Day.getSelectedDay().getNumber()) && h.getApproximateScore(Day.getSelectedDay().getNumber())<=to;
    }
}
