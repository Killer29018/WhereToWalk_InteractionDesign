package WhereToWalk.weather;

import WhereToWalk.JsonReader;

import java.net.*;
import java.text.*;
import java.io.*;
import java.time.*;
import java.time.temporal.*;
import java.util.*;

import org.json.*;

public class WeatherForecast {
    // The furthest forecast we will make, in days.
    public static final int FORECAST_FURTHEST = 7;

    private JSONObject raw_data;
    // We avoid java.util.Date as most parts of it is deprecated.
    // Here we employ java.time.Instant from JDK 1.8.
    private HashMap<Instant, Weather> weathers;

    private double[] score = new double[7];

    // This follows ISO8601, i.e. YYYY-MM-DDTHH:MM
    // where T is the letter 'T'
    private Instant parseTime(String str) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(str).toInstant();
        } catch (ParseException ex) {
            System.out.println("Parse exception for OpenMeteo");
            return null;
        }
    }

    // Get the weather forecast nearest, while after, the specified time.
    // Returns null if the time is too far from current.
    public Weather getWeatherAt(Instant time) {
        Instant untruncated = time;
        time = time.truncatedTo(ChronoUnit.HOURS);

        // In case time is exactly aligned to an hour, we don't need to add anything
        if (!time.equals(untruncated))
            time = time.plusSeconds(3600);

        if (!weathers.containsKey(time))
            return null;

        return weathers.get(time);
    }

    public double[] getScores() {
        return score;
    }

    private double calcScore(Instant time, long duration) {
        double res = 0;
        for (int i = 0; i < duration; i++) { // current day score
            Weather weather = getWeatherAt(time.plusSeconds(i * 3600));
            double cc = weather.getCloudCoverage() / 100.;
            double p = weather.getPrecipitation();
            double t = weather.getTemperature();
            double ws = weather.getWindSpeed();
            res += (0.2 * Math.exp(-0.01 * (Math.pow((t - 17.5), 2))) +
                    0.5 * Math.exp(-0.1 * p) +
                    0.2 * Math.exp(-0.05 * ws) +
                    0.1 * Math.exp(-5 * cc));
        }
        return res / duration;
    }

    private double calcScore(Instant time) {
        return calcScore(time, 24);
    }

    @SuppressWarnings("deprecation")
    public WeatherForecast(double lat, double lon) {
        weathers = new HashMap<>();

        try {
            // Build the URL for Web API request
            // Documentation of OpenMeteo is at https://open-meteo.com/en/docs
            StringBuilder sb = new StringBuilder(String.format(
                    "https://api.open-meteo.com/v1/forecast?latitude=%.4f&longitude=%.4f&forecast_hours=%d&hourly=",
                    lat, lon, 24 * FORECAST_FURTHEST));

            for (String s : Weather.metrics) {
                sb.append(s);
                sb.append(",");
            }
            // Delete the final comma
            sb.deleteCharAt(sb.length() - 1);

            URL url = new URL(sb.toString());
            InputStream is = url.openStream();

            /**
             * Json format:
             * {
             * "hourly": {
             * "time": [...],
             * "precipitation": [...],
             * <!-- other metrics -->
             * }
             * }
             */
            raw_data = JsonReader.readJsonFromInputStream(is);
            JSONObject hourly = raw_data.getJSONObject("hourly");
            JSONArray time = hourly.getJSONArray("time");

            for (int i = 0; i < time.length(); i++) {
                HashMap<String, Double> map = new HashMap<>();
                for (String s : Weather.metrics)
                    map.put(s, hourly.getJSONArray(s).getDouble(i));

                Instant instant = parseTime(time.getString(i));
                weathers.put(instant, new Weather(map));
            }

            // Calculate scores according to weather
            Instant cur = Instant.now();
            Instant nextday = cur.truncatedTo(ChronoUnit.DAYS);
            if (!cur.equals(nextday)) {
                nextday = nextday.plusSeconds(86400);
            }

            Duration duration = Duration.between(cur, nextday);
            score[0] = calcScore(cur, duration.toHours());
            cur = nextday;
            for (int i = 1; i < 7; i++) {
                score[i] = calcScore(cur);
                cur = cur.plusSeconds(86400);
            }

        } catch (Exception ex) {
            System.out.println(ex.getClass().getName());
        }
    }
}
