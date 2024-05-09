package WhereToWalk.Backend;

import java.time.*;

public class Day
{
    private final LocalDate mDate;
    private final int mPrecipitationChance;
    private final float mTemperature;
    private final float mWindGust;

    public Day(LocalDate date, int precipitationChance, float temperature, float windGust)
    {
        mDate = date;
        mPrecipitationChance = precipitationChance;
        mTemperature = temperature;
        mWindGust = windGust;
    }

    @Override
    public String toString()
    {
        return String.format("Date: %s, PrecipitationChance: %d, Temperature: %.2f, Wind Gust: %.2f", mDate, mPrecipitationChance, mTemperature, mWindGust);
    }
}
