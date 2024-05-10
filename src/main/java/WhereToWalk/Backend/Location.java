package WhereToWalk.Backend;

import org.json.JSONObject;
import org.json.JSONArray;

import java.time.*;
import java.time.format.*;
import java.util.Locale;
import java.util.ArrayList;

public class Location
{
    private static final String weatherAPIFormat = "https://api.open-meteo.com/v1/forecast?latitude=%.2f&longitude=%.2f&daily=temperature_2m_max,precipitation_probability_max,wind_gusts_10m_max";

    private ArrayList<Day> mDays;

    private final String mName;
    private final double mLat;
    private final double mLon;

    public String getName() { return mName; }
    public double getLat() { return mLat; }
    public double getLon() { return mLon; }

    public final Day getDayOffset(int offset)
    {
        int wrappedValue = offset % mDays.size();
        if (wrappedValue < 0) wrappedValue += mDays.size();
        return mDays.get(wrappedValue);
    }
    public final int getNoOfDays() { return mDays.size(); }

    public Location() {
        mDays = new ArrayList<Day>();

        mName = "";
        mLat = 0.0;
        mLon = 0.0;
    }

    public Location(JSONObject object) {
        mDays = new ArrayList<Day>();

        mName = object.getString("name");
        mLat = object.getJSONArray("lat_long").getDouble(0);
        mLon = object.getJSONArray("lat_long").getDouble(1);

        JSONObject weatherData = JsonReader.readJsonFromURL(String.format(weatherAPIFormat, getLat(), getLon()));
        JSONObject dailyWeather = weatherData.getJSONObject("daily");

        for (int i = 0; i < dailyWeather.getJSONArray("temperature_2m_max").length(); i++)
        {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            timeFormatter = timeFormatter.withLocale(Locale.UK);
            LocalDate date = LocalDate.parse(dailyWeather.getJSONArray("time").getString(i), timeFormatter);
            int precipitation = dailyWeather.getJSONArray("precipitation_probability_max").getInt(i);
            float temperature = dailyWeather.getJSONArray("temperature_2m_max").getFloat(i);
            float windGust = dailyWeather.getJSONArray("wind_gusts_10m_max").getFloat(i);

            mDays.add(new Day(date, precipitation, temperature, windGust));
        }
    }

    @Override
    public String toString()
    {
        String str;
        str = String.format("Name: %s\nLat Long : %.5f %.5f", getName(), getLat(), getLon());
        str = String.format("%s\nDays: \n",str);
        for (int i = 0; i < getNoOfDays(); i++)
        {
            str = String.format("%s\t%s\n", str, getDayOffset(i).toString());
        }
        return str;

    }
}
