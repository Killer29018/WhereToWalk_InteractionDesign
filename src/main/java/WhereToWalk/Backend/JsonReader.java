package WhereToWalk.Backend;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.FileInputStream;
import java.io.File;
import java.net.URL;
import java.net.URI;
import java.nio.charset.Charset;


public class JsonReader
{
    public static JSONObject readJsonFromFile(String filename)
    {
        try
        {
            File file = new File(filename);

            InputStream stream = new FileInputStream(file);
            JSONObject object =  JsonReader.readJsonFromInputStream(stream);
            stream.close();
            return object;
        }
        catch (java.io.FileNotFoundException e)
        {
            System.out.println("File failed to initialize");
            System.exit(-1);
        }
        catch (java.io.IOException e)
        {
            System.out.println("Failed to read file");
            System.exit(-1);
        }

        return new JSONObject();
    }

    public static JSONObject readJsonFromURL(String url)
    {
        try
        {
            InputStream is = new URI(url).toURL().openStream();
            JSONObject object = readJsonFromInputStream(is);
            is.close();
            return object;
        }
        catch (java.io.IOException e)
        {
            System.out.println("Failed to open URL");
            System.exit(-1);
        }
        catch (java.net.URISyntaxException e)
        {
            System.out.println("Malformed URL");
            System.exit(-1);
        }

        return new JSONObject();
    }

    public static JSONObject readJsonFromInputStream(InputStream stream)
    {
        try
        {
            BufferedReader rd = new BufferedReader(new InputStreamReader(stream, Charset.forName("UTF-8")));
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1)
            {
                sb.append((char)cp);
            }
            String jsonText = sb.toString();
            JSONObject json = new JSONObject(jsonText);

            return json;
        }
        catch (java.io.IOException e)
        {
            System.out.println("Failed to parse JSON");
            System.out.println(e.getMessage());
        }

        return new JSONObject();
    }

    private JsonReader() {}
        // String url = "https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&hourly=cloud_cover,cloud_cover_low,cloud_cover_mid,cloud_cover_high";
        // try
        // {
        //     InputStream is = new URL(url).openStream();
        //     try {
        //         BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        //         StringBuilder sb = new StringBuilder();
        //         int cp;
        //         while ((cp = rd.read()) != -1)
        //         {
        //             sb.append((char)cp);
        //         }
        //         String jsonText = sb.toString();
        //         // String jsonText = readAll(rd);
        //         JSONObject json = new JSONObject(jsonText);
        //
        //         System.out.println(json.toString());
        //     } finally {
        //         is.close();
        //     }
        // }
        // catch (java.io.IOException e)
        // {
        //     System.out.println("IO Exception");
        // }
        }
