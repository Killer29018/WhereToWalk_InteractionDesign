package WhereToWalk.Backend;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Window extends Application
{
    public static void main(String[] args)
    {
        launch();
    }

    @Override
    public void start(Stage primaryStage)
    {
        primaryStage.setTitle("Hello, World!");
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event)
            {
                System.out.println("Hello, World");
            }
        });

        StackPane root = new StackPane();
        root.getChildren().add(btn);
        primaryStage.setScene(new Scene(root, 300, 250));
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
