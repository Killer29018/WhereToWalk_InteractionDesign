package WhereToWalk;

import java.io.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import WhereToWalk.loc.LocationDistance;
import WhereToWalk.loc.LocationFinder;
import WhereToWalk.sorting.ShortestDistance;
import WhereToWalk.weather.WeatherForecast;
import org.json.*;

public class Hills {
    public static class NoSuchHillException extends Exception {};

    private final List<Hill> hills = new ArrayList<>();
    private static List<Hill> hillsResults = new ArrayList<>();
    private static List<Hill> displayedHills = new ArrayList<>();
    private static int pointer;
    private static Hills instance = null;
    private static Comparator<Hill> sorter;

    /*
     * Hills is a singleton
     * If it has not been created yet then create a new instance
     * Otherwise return the current instance
     */
    public static Hills getInstance() {
        if (instance == null) {
            instance = new Hills();
        }
        return instance;
    }

    /*
     * Return the current list of hills
     */
    public List<Hill> getHills() {
        return hillsResults;
    }

    /*
     * Private constructor for the Hills class to ensure that only one instance
     * can be created
     * Loads all the hills and retrieves
     * the weather for nearby hills based on group id's
     */
    private Hills() {
        pointer=0;
        Map<Integer, WeatherForecast> weathers = new HashMap<>();
        try {
            // Read from the groupcoords json a list of group ids and the
            // associated latitude and longitude to retrieve that weather
            // at that location
            InputStream is = new FileInputStream("./src/main/resources/WhereToWalk/groupcoords.json");
            JSONObject tmp = JsonReader.readJsonFromInputStream(is);
            for (int i = 0; i < tmp.length(); i++) {
                JSONObject obj = tmp.getJSONObject(String.valueOf(i));
                double glat = obj.getDouble("lat");
                double glon = obj.getDouble("lon");

                // Get the current forecast of the weather at the
                // specified latitude and longitude
                WeatherForecast forecast = new WeatherForecast(glat, glon);
                weathers.put(i, forecast);
            }
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
        }

        // Load the information about the hills including the name,
        // latitude and longitude, id, group id, altitude and county
        try {
            InputStream is = new FileInputStream("./src/main/resources/WhereToWalk/hills.json");
            JSONObject tmp = JsonReader.readJsonFromInputStream(is);
            JSONArray arr = tmp.getJSONArray("hills");

            // Retrieve the current location of the user based on their public ip
            double ulat = LocationFinder.getLatitude();
            double ulon = LocationFinder.getLongitude();

            double min_dist = Double.MAX_VALUE;

            // Go through the list of hills and get relative distance to the
            // closest hill to the user
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                double lat = obj.getDouble("lat");
                double lon = obj.getDouble("lon");
                min_dist = Math.min(min_dist, LocationDistance.distance(lat, ulat, lon, ulon, 0, 0));
            }

            // Go through the list of hills and retrieve the information about
            // that hill and create new objects for each hill
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                double lat = obj.getDouble("lat");
                double lon = obj.getDouble("lon");
                int gid = obj.getInt("groupid");
                int id = obj.getInt("id");
                String name = obj.getString("name");
                String county = obj.getString("county");
                double altitude = obj.getDouble("alt");

                // Create a score for the distance of the hill, the further the hill, the smaller
                // the score
                double distscore = min_dist / LocationDistance.distance(lat, ulat, lon, ulon, 0, 0);
                double score = weathers.get(gid).getScore();
                hills.add(new Hill(id, name, lat, lon, county, altitude, distscore, score));
            }
            hillsResults = hills;
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
        }
    }

    // Sort the hills based on the stored sorter comparator
    private void sortHills() {
        hillsResults.sort(sorter);
        pointer = 0;
    }

    /* Get the next 'n' hills from the list of hills
     * that are yet to be retrieved
     */
    protected List<Hill> getNHills(int n) {
        // Create a new list of hills and fill it with the next
        // 'n' hills from the list of all hills that are yet to
        // be retrieved
        List<Hill> hillSubset = new ArrayList<>();
        for (int i=pointer; i<pointer+n; i++) {
            try {
                Hill hill = hillsResults.get(i);
                hillSubset.add(hill);
                displayedHills.add(hill);
            } catch (Exception e) {
                n = i-pointer;
                break;
            }
        }
        pointer += n;
        return hillSubset;
    }

    /*
     * See if any hills are still retrievable based on the filtering applied
     */
    protected boolean reachedEnd() {
        return (pointer == hillsResults.size());
    }

    /*
     * Get 'n' hills from the list and sort then sort the retrieved
     * hills based on the comparator
     */
    protected List<Hill> getNHillsSorted(int n, Comparator<Hill> sorter) {
        List<Hill> hills = getNHills(n);
        hills.sort(sorter);
        return hills;
    }

    /*
     * Filter the list of hills based on the name of the hill and the provided
     * 'text'
     */
    protected void search(String text) {
        // Create a predicate that returns whether or not a
        // hill's name satisfies the regular expression
        Pattern pattern = Pattern.compile(".*"+text.toLowerCase()+".*");
        Predicate<Hill> p = h -> {
            Matcher matcher = pattern.matcher(h.getName().toLowerCase());
            return matcher.matches();
        };
        hillsResults = hills;
        filter(p);
    }

    /*
     * Filter the list of hills based on the provided predicate
     */
    protected void filter(Predicate<Hill> p) {
        List<Hill> filteredHills = hillsResults
                                   .stream()
                                   .filter(p)
                                   .collect(Collectors.toList());
        hillsResults = filteredHills;
        displayedHills = new ArrayList<>();
        sortHills();
    }

    /*
     * Return the currently stored sorting comparator
     */
    protected Comparator<Hill> getSorter() {
        return sorter;
    }

    /*
     * Set the global sorting comparator for the list of hills
     */
    protected void setSorter(Comparator<Hill> sorter) {
        this.sorter = sorter;
        sortHills();
    }

    /*
     * Reset the current filter and sorted list of hills
     */
    protected void resetResults() {
        pointer = 0;
        hillsResults = hills;
    }

    /*
     * Returns the number of hills that currently satisfy the filter
     */
    protected int getNumberResults() {
        return hillsResults.size();
    }

    /*
     * Return the hill that is matched by 'id'
     */
    protected Hill getHillbyID(int id) throws NoSuchHillException {
        for (Hill hill : displayedHills) {
            if (hill.getID() == id) {
                return hill;
            }
        }
        throw new NoSuchHillException();
    }


}
