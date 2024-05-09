package WhereToWalk;

import java.io.InputStream;
import java.io.*;

import org.json.JSONObject;

public class Main
{
    public static void main(String[] args)
    {
        try
        {
            File file = new File("example_hill.json");
            InputStream stream = new FileInputStream(file);
            JSONObject object = JsonReader.readJsonFromInputStream(stream);

            stream.close();

            System.out.println(object.toString());
        }
        catch(java.io.IOException e)
        {
            System.out.println("Not found file");
        }

        Window.main(args);
    }
}
