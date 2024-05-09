package WhereToWalk.loc;

import java.io.*;
import java.math.BigDecimal;
import java.net.*;

import org.json.*;

// Finds current (physical) location of user
public class LocationFinder {

    // latitude
    private double lat;

    // longitude
    private double lon;
    
    public double getLatitude() {
        return lat;
    }

    public double getLongitude() {
        return lon;
    }

    // Finds current location by looking at public IP,
    // and find the location where this IP corresponds to
    public LocationFinder() {
        try {
            // This Web API can be used to get your public IP
            // I didn't use java.net.InetAddress APIs because they might only return
            // IP values starting with 127 and 172, which are private IP addresses
            URL ipify = new URL("https://api.ipify.org");
            InputStream is = ipify.openStream();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String ip = reader.readLine();

            System.out.println(ip);
            is.close();
            reader.close();

            // This Web API maps the public IP to physical address
            URL iploc = new URL(String.format("http://ip-api.com/json/%s", ip));
            is = iploc.openStream();

            reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            for (int c; (c = reader.read()) != -1;)
                sb.append((char) c);
            
            System.out.println(sb.toString());
            JSONObject json = new JSONObject(sb.toString());
            
            lat = json.getDouble("lat");
            lon = json.getDouble("lon");

            is.close();
            reader.close();
        } catch (Exception ex) {
            // TODO: more elegant way of handling exceptions
            System.out.println("Failed!");
            System.out.println(ex.getClass().getName());
            return;
        }
    }
}
