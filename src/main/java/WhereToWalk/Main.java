package WhereToWalk;

import java.net.URL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

public class Main
{
    public static void main(String[] args)
    {
        String url = "https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&hourly=cloud_cover,cloud_cover_low,cloud_cover_mid,cloud_cover_high";
        try
        {
            InputStream is = new URL(url).openStream();
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                StringBuilder sb = new StringBuilder();
                int cp;
                while ((cp = rd.read()) != -1)
                {
                    sb.append((char)cp);
                }
                String jsonText = sb.toString();
                // String jsonText = readAll(rd);
                JSONObject json = new JSONObject(jsonText);

                System.out.println(json.toString());
            } finally {
                is.close();
            }
        }
        catch (java.io.IOException e)
        {
            System.out.println("IO Exception");
        }
    }
}
