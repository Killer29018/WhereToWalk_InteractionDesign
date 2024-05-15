package WhereToWalk;

import java.io.*;
import java.util.*;

import org.json.*;


public class Hills {
    public static class ResourceMissingException extends RuntimeException{};
    private List<Hill> hills;

    protected Hills() {
        try {
            InputStream is = new FileInputStream("../hills.json");
            JSONObject tmp = JsonReader.readJsonFromInputStream(is);
            JSONArray arr = tmp.getJSONArray("hills");

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                double lat = obj.getDouble("lat");
                double lon = obj.getDouble("lon");
                String name = obj.getString("name");
                hills.add(new Hill(name, lat, lon));
            }

        } catch (Exception e) {
            System.out.println("Failed to read csv");
            throw new ResourceMissingException();
        }
    }

}
