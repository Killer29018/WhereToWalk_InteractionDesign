package WhereToWalk;

import WhereToWalk.weather.*;

public class Hill {
    private String name;
    private double lat;
    private double lon;

    private Weather weather;

    protected Hill(String name, double lat, double lon) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }


    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
    
    public Weather getWeather() {
        return weather;
    }

}
