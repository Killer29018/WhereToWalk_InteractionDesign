package WhereToWalk;

import org.json.*;

import java.io.BufferedReader;
// import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
// import java.io.Reader;
// import java.net.URL;
import java.nio.charset.Charset;

public class JsonReader
{
    public static JSONObject readJsonFromInputStream(InputStream stream)
    {
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(stream, Charset.forName("UTF-8")));
            StringBuilder sb = new StringBuilder();
            int cp;

            while ((cp = rd.read()) != -1)
            {
                sb.append((char)cp);
            }

            String jsonText = sb.toString();
            JSONObject json = new JSONObject(jsonText);

            System.out.println(json.toString());
            stream.close();
            return json;

        } catch (Exception e) {
            return null;
        }
    }

    private JsonReader() {}
}
