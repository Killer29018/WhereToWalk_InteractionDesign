package WhereToWalk;

import java.io.*;
import java.util.*;

import WhereToWalk.weather.WeatherForecast;
import org.json.*;


public class Hills {
    public static class ResourceMissingException extends RuntimeException{};
    private List<Hill> hills = new ArrayList<>();

    protected Hills() {
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


//                hills.add(new Hill(name, lat, lon, score, ));
            }

        } catch (Exception e) {
            System.out.println("Failed to read hills.json");
            throw new ResourceMissingException();
        }
    }




}
