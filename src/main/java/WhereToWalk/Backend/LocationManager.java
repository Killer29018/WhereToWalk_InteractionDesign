package WhereToWalk.Backend;

import java.util.Set;
import java.util.HashMap;

import org.json.JSONObject;
import org.json.JSONArray;

public class LocationManager
{
    private static HashMap<String, Location> sLocations;

    public static final HashMap<String, Location> getLocations()
    {
        return sLocations;
    }

    public static final Location getLocation(String name)
    {
        return sLocations.get(name);
    }

    public static final Set<String> getLocationNames()
    {
        return sLocations.keySet();
    }

    public static void loadLocations()
    {
        sLocations = new HashMap<String, Location>();

        JSONObject locations = JsonReader.readJsonFromFile("example_hill.json");

        JSONArray hills = locations.getJSONArray("Hills");
        for (int i = 0; i < hills.length(); i++)
        {
            Location loc = new Location(hills.getJSONObject(i));
            sLocations.put(loc.getName(), loc);

            System.out.println(loc.getDayOffset(-1));
        }
    }

    private LocationManager() {}
}
