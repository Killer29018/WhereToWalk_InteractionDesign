package WhereToWalk;

import WhereToWalk.weather.WeatherForecast;

public class Hill {
    private String name;
    private double lat;
    private double lon;
    private String county;
    private double altitude;
    private int id;

    // Weather scores
    private double wscore;
    // Distance score
    private double dscore;

    // Weather metrics
    private WeatherForecast forecast = null;

    protected Hill(int id, String name, double lat, double lon, String county, double altitude, double dscore, double wscore) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.wscore = wscore;
        this.dscore = dscore;
        this.county = county;
        this.altitude = altitude;
    }

    // Get a 0-100 score.
    // Day 0 is the current day; max value of day is 6.
    // Calls weather api of this specific hill.
    public int getPreciseScore() {
        if (forecast == null)
            forecast = new WeatherForecast(lat, lon);
            
        return (int) (forecast.getScore() * 80 + dscore * 20);
    }

    // Get a 0-100 score.
    // Day 0 is the current day; max value of day is 6.
    // Based on the 100km-ranged group.
    public int getApproximateScore(int day) {
        return (int) (wscore * 80 + dscore * 20);
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

    public int getID() {return id;}

    public double getAltitude() {
        return altitude;
    }

    public String getCounty() {
        return county;
    }

    public WeatherForecast getHillWeatherMetric() {
        if (forecast == null)
            forecast = new WeatherForecast(lat, lon);
        return forecast;
    }

}
