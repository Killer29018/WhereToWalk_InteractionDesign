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

    /*
     * Constructor of a Hill to retrieve all the needed information
     */
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

    /*
     * Get a 0-100 score.
     * Day 0 is the current day; max value of day is 6.
     * Calls weather api of this specific hill.
     */
    public int getPreciseScore() {
        if (forecast == null) {
            forecast = new WeatherForecast(lat, lon);
        }

        return (int) (forecast.getScore() * 80 + dscore * 20);
    }

    /*
     * Get a 0-100 score.
     * Day 0 is the current day; max value of day is 6.
     * Based on the 100km-ranged group.
     */
    public int getApproximateScore(int day) {
        return (int) (wscore * 80 + dscore * 20);
    }

    /*
     * Return the name of the hill
     */
    public String getName() {
        return name;
    }

    /*
     * Return the latitude of the hill
     */
    public double getLat() {
        return lat;
    }

    /*
     * Return the longitude of the hill
     */
    public double getLon() {
        return lon;
    }

    /*
     * Return the ID of the hill
     */
    public int getID() {
        return id;
    }

    /*
     * Return the altitude of the hill
     */
    public double getAltitude() {
        return altitude;
    }

    /*
     * Return the county of the hill
     */
    public String getCounty() {
        return county;
    }

    /*
     * Get the weather forecast for the hill
     */
    public WeatherForecast getHillWeatherMetric() {
        if (forecast == null) {
            forecast = new WeatherForecast(lat, lon);
        }
        return forecast;
    }

}
