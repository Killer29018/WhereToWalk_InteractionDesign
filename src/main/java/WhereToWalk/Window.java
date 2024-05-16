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
    private final int mWidth = 240;
    // private final float aspect = 9.f/20.f;
    private final int mHeight = 400;

    public static void main(String[] args)
    {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws java.io.IOException
    {
        Parent root= FXMLLoader.load(getClass().getResource("fxml/hill_page.fxml"));

        Scene scene = new Scene(root, mWidth, mHeight);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
