package WhereToWalk;

import WhereToWalk.weather.WeatherForecast;

public class Hill {
    private String name;
    private double lat;
    private double lon;

    // Weather scores
    private double[] wscores;
    // Distance score
    private double dscore;

    private WeatherForecast forecast = null;

    protected Hill(String name, double lat, double lon, double dscore, double[] wscores) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.wscores = wscores;
        this.dscore = dscore;
    }

    // Get a 0-100 score.
    // Day 0 is the current day; max value of day is 6.
    // Calls weather api of this specific hill.
    public int getPreciseScore(int day) {
        if (forecast == null)
            forecast = new WeatherForecast(lat, lon);
            
        return (int) (forecast.getScores()[day] * 80 + dscore * 20);
    }

    // Get a 0-100 score.
    // Day 0 is the current day; max value of day is 6.
    // Based on the 100km-ranged group.
    public int getApproximateScore(int day) {
        return (int) (wscores[day] * 80 + dscore * 20);
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

}
