package WhereToWalk.weather;

import java.util.*;

public class Weather {
    public static final List<String> metrics = Arrays.asList(
        // 0-3km cloud coverage
        // We don't need cloud coverage data for higher altitudes;
        // The hills themselves are not likely to be that high
        "cloud_cover_low",

        // temperature (Celsius), 2m above ground
        // Note: for every 100m higher, the temperature lowers 0.6 degree on average
        "temperature_2m",

        // wind speed (km/h), 10m above ground
        "wind_speed_10m",

        // precipitation of the following hour
        "precipitation"
    );

    
    private double cloud;
    private double rain;
    private double temp;
    private double windspeed;

    // Unit: %
    public double getCloudCoverage() {
        return cloud;
    }

    // Unit: mm
    public double getPrecipitation() {
        return rain;
    }

    // Unit: degree Celsius
    public double getTemperature() {
        return temp;
    }

    // Unit: km/h
    public double getWindSpeed() {
        return windspeed;
    }

    // Composition of raw_data:
    // String must be an element of metrics, while Double is the corresponding value
    // In this way, we can change metrics without affecting WeatherForecast.java 
    protected Weather(HashMap<String, Double> raw_data) {
        this.cloud = raw_data.get("cloud_cover_low");
        this.rain = raw_data.get("precipitation");
        this.temp = raw_data.get("temperature_2m");
        this.windspeed = raw_data.get("wind_speed_10m");
    }
}
