package WhereToWalk;

import java.net.URL;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.Parent;

import javafx.fxml.FXMLLoader;

public class Window extends Application
{
    private final int mWidth = 500;
    private final float aspect = 9.f/16.f;
    private final int mHeight = (int)(mWidth * (1.0f / aspect));

    public static void main(String[] args)
    {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws java.io.IOException
    {
        // FXMLLoader loader = new FXMLLoader();
        // loader.setLocation(new URL("file://app_test.fxml"));
        // AnchorPane root = loader.load();

        Parent root= FXMLLoader.load(getClass().getResource("app_test.fxml"));

        Scene scene = new Scene(root, mWidth, mHeight);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

        // HELLO I PUSHED!
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
// }
