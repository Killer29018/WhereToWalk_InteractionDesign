package WhereToWalk;

import java.io.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import WhereToWalk.loc.LocationDistance;
import WhereToWalk.loc.LocationFinder;
import WhereToWalk.sorting.HighestScore;
import WhereToWalk.sorting.ShortestDistance;
import WhereToWalk.weather.WeatherForecast;
import org.json.*;

public class Hills {
    private List<Hill> hills = new ArrayList<>();
    private static int pointer;
    private static Hills instance = null;
    private static List<Hill> displayedHills;

    public static Hills getInstance() {
        if (instance == null)
            instance = new Hills();
        return instance;
    }

    public List<Hill> getHills() {
        return hills;
    }

    private Hills() {
        pointer=0;
        Map<Integer, WeatherForecast> weathers = new HashMap<>();
        try {
            InputStream is = new FileInputStream("./src/main/resources/WhereToWalk/groupcoords.json");
            JSONObject tmp = JsonReader.readJsonFromInputStream(is);
            for (int i = 0; i < tmp.length(); i++) {
                JSONObject obj = tmp.getJSONObject(String.valueOf(i));
                double glat = obj.getDouble("lat");
                double glon = obj.getDouble("lon");

                WeatherForecast forecast = new WeatherForecast(glat, glon);
                weathers.put(i, forecast);
            }
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
        }

        try {
            InputStream is = new FileInputStream("./src/main/resources/WhereToWalk/hills.json");
            JSONObject tmp = JsonReader.readJsonFromInputStream(is);
            JSONArray arr = tmp.getJSONArray("hills");

            LocationFinder finder = new LocationFinder();
            double ulat = finder.getLatitude();
            double ulon = finder.getLongitude();

            double mindist = Double.MAX_VALUE;

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                double lat = obj.getDouble("lat");
                double lon = obj.getDouble("lon");
                mindist = Math.min(mindist, LocationDistance.distance(lat, ulat, lon, ulon, 0, 0));
            }

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                double lat = obj.getDouble("lat");
                double lon = obj.getDouble("lon");
                int gid = obj.getInt("groupid");
                String name = obj.getString("name");

                double distscore = mindist / LocationDistance.distance(lat, ulat, lon, ulon, 0, 0);
                double[] scores = weathers.get(gid).getScores();
                hills.add(new Hill(name, lat, lon, distscore, scores));
            }
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
        }
    }

    protected void sortHills(Comparator<Hill> sorter) {
        LocationFinder.LocationFinder();
        hills.sort(sorter);
        pointer = 0;
    }

    protected List<Hill> getNHills(int n) {
        List<Hill> hillSubset = new ArrayList<>();
        for (int i=pointer; i<pointer+n;i++) {
            hillSubset.add(hills.get(i));
        }
        pointer += n;
        return hillSubset;
    }

    protected  List<Hill> getNHillsSorted(int n, Comparator<Hill> sorter) {
        List<Hill> hills = getNHills(n);
        hills.sort(sorter);
        return hills;
    }

    protected List<Hill> search(String text) {
        Pattern pattern = Pattern.compile(".*"+text+".*");
        Predicate<Hill> p = h -> {
            Matcher matcher = pattern.matcher(h.getName());
            return matcher.matches();
        };
        return filter(p);
    }

    protected List<Hill> filter(Predicate<Hill> p) {
        LocationFinder.LocationFinder();
        List<Hill> filteredHills = hills
                .stream()
                .filter(p)
                .collect(Collectors.toList());
        return filteredHills;
    }

    public static void main(String[] args) {
        Comparator<Hill> sorter = new ShortestDistance();
        Hills hills = new Hills();
        hills.sortHills(new ShortestDistance());
        List<Hill> h = hills.search("Crag");
        for (Hill hill : h) {
            System.out.println(hill.getName());
        }
    }
}
