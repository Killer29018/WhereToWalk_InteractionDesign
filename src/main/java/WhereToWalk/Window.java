package WhereToWalk;

import java.net.URL;
import java.io.*;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.Parent;

import javafx.event.*;
import javafx.scene.input.*;

import javafx.fxml.FXMLLoader;

public class Window extends Application
{
    private final int mWidth = 480;
    // private final float aspect = 9.f/20.f;
    private final int mHeight = 800;

    Parent landingPage;
    Parent hillRecommendations;
    Parent hillPage;

    Scene mainScene;

    public static void main(String[] args)
    {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws java.io.IOException
    {
        landingPage = FXMLLoader.load(getClass().getResource("fxml/landing_page.fxml"));
        hillRecommendations = FXMLLoader.load(getClass().getResource("fxml/hill_recommendations.fxml"));
        hillPage = FXMLLoader.load(getClass().getResource("fxml/hill_page.fxml"));

        // mainScene = new Scene(landingPage, mWidth, mHeight);
        loadLandingPage(primaryStage);
        // primaryStage.setScene(mainScene);
        // primaryStage.show();

        // setupHillPage(primaryStage);
        // setupHillRecommendations(primaryStage);
        // setupLandingPage(primaryStage);

        primaryStage.setResizable(false);

        mainScene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            public void handle(KeyEvent event)
            {
                if (event.getCode() == KeyCode.ESCAPE)
                {
                    primaryStage.close();
                }
                event.consume();
            }
        });

    }

    public void loadLandingPage(Stage primaryStage) throws java.io.IOException
    {
        primaryStage.setScene(new Scene(landingPage));
        primaryStage.show();

        // ScrollPane = (ScrollPane)FXMLLoader.
        ScrollPane hillScroller = (ScrollPane)landingPage.lookup("#HillButtonScroller");
        Button hillButton = (Button)hillScroller.lookup("#HillButton");

        hillButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event)
            {
                primaryStage.setScene(new Scene(hillPage, mWidth, mHeight));
            }
        });
        // Button filterBtn = (Button)landingPage.lookup("#Filter");
        // filterBtn.setOnAction(new EventHandler<ActionEvent>() {
            // public void handle(ActionEvent event)
            // {
            //     System.out.print("Filter");
            // }
        // });
    }

    public void setupHillRecommendations(Stage primaryStage) throws java.io.IOException
    {
    }

    public void setupHillPage(Stage primaryStage) throws java.io.IOException
    {
    }
}
