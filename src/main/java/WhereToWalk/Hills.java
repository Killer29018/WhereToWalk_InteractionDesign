package WhereToWalk;

import java.io.*;
import java.util.*;

import WhereToWalk.loc.LocationFinder;
import WhereToWalk.weather.WeatherForecast;
import org.json.*;


public class Hills {
    public static class ResourceMissingException extends RuntimeException{};
    private List<Hill> hills = new ArrayList<>();
    private int pointer;

    protected Hills() {
        pointer = 0;
        Map<Integer, WeatherForecast> groupWeathers = new HashMap<>();
        try {
            InputStream is = new FileInputStream("..groupcoords.json");
            JSONObject tmp = JsonReader.readJsonFromInputStream(is);
            for (int i=0; i<tmp.length();i++) {
                JSONObject obj = tmp.getJSONObject(String.valueOf(i));
                double glat = obj.getDouble("lat");
                double glon = obj.getDouble("lon");

                WeatherForecast weatherForecast = new WeatherForecast(glat, glon);
                groupWeathers.put(i, weatherForecast);
            }




        } catch (Exception e) {
            System.out.println("Failed to read group.json");
            throw new ResourceMissingException();
        }


        try {
            InputStream is = new FileInputStream("../hills.json");
            JSONObject tmp = JsonReader.readJsonFromInputStream(is);
            JSONArray arr = tmp.getJSONArray("hills");

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                double lat = obj.getDouble("lat");
                double lon = obj.getDouble("lon");
                int gid = obj.getInt("groupid");
                String name = obj.getString("name");


            }

        } catch (Exception e) {
            System.out.println("Failed to read hills.json");
            throw new ResourceMissingException();
        }
    }

    protected void sortHills(Comparator<Hill> sorter) {
        LocationFinder.LocationFinder();
        this.hills.sort(sorter);
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

    protected List<Hill> getNHillsSorted(int n, Comparator<Hill> sorter) {
        List<Hill> hills = getNHills(n);
        hills.sort(sorter);
        return hills;
    }
}
