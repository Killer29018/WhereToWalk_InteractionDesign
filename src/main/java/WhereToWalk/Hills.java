package WhereToWalk;

import java.io.*;
import java.util.*;

import WhereToWalk.loc.LocationDistance;
import WhereToWalk.loc.LocationFinder;
import WhereToWalk.weather.WeatherForecast;
import org.json.*;

public class Hills {
    private List<Hill> hills = new ArrayList<>();

    private static Hills instance = null;

    public static Hills getInstance() {
        if (instance == null)
            instance = new Hills();
        return instance;
    }

    public static List<Hill> getHills() {
        return Hills.getInstance().hills;
    }

    private Hills() {
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

}
