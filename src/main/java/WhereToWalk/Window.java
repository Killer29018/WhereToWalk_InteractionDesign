package WhereToWalk;

import WhereToWalk.weather.*;

import java.net.URL;
import java.io.*;
import java.util.*;
import java.time.*;
import java.time.format.*;

import javafx.application.Application;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.stage.*;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.geometry.*;
import javafx.collections.*;

import javafx.event.*;
import javafx.scene.input.*;

import javafx.fxml.FXMLLoader;

public class Window extends Application
{
    private final int mWidth = 480;
    // private final float aspect = 9.f/20.f;
    private final int mHeight = 800;

    private final int hillCount =  100;

    Parent landingPageParent;
    Parent hillRecommendationsParent;
    Parent hillPageParent;

    Scene landingPage;
    Scene hillRecommendations;
    Scene hillPage;

    Scene mainScene;

    Boolean loadedLandingPage = false;
    Boolean loadedHillRecommendations = false;

    List<Hill> hills;
    Instant currentTime;
    Boolean loadedHills = false;

    public static void main(String[] args)
    {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws java.io.IOException
    {
        hillRecommendationsParent = FXMLLoader.load(getClass().getResource("fxml/hill_recommendations.fxml"));
        hillPageParent = FXMLLoader.load(getClass().getResource("fxml/hill_page.fxml"));

        Hills.getInstance().setSorter((h1, h2) ->
                 -(h1.getPreciseScore(0) - h2.getPreciseScore(0)));

        loadLandingPage(primaryStage);

        primaryStage.setResizable(false);

        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
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

    public void loadHills()
    {
        hills = Hills.getInstance().getHills();
        loadedHills = true;
    }

    public void loadLandingPage(Stage primaryStage) throws java.io.IOException
    {
        try
        {
            landingPageParent = FXMLLoader.load(getClass().getResource("fxml/landing_page.fxml"));
        }
        catch (java.io.IOException e)
        {
            System.out.println(e.getStackTrace());
            // System.out.println(e.getMessage());
        }

        if (!loadedLandingPage)
        {
            landingPage = new Scene(landingPageParent);
            loadedLandingPage = true;
        }
        primaryStage.setScene(landingPage);
        primaryStage.show();

        if (!loadedHills)
        {
            loadHills();
        }

        TextField searchBar = (TextField)landingPageParent.lookup("#SearchBarField");
        searchBar.textProperty().addListener((obs, oldV, newV) ->
                {
                    // System.out.println(newV);
                    if (newV != "")
                    {
                        Hills.getInstance().search(newV);
                        loadHills();

                        try {
                            loadLandingPage(primaryStage);
                        } catch (java.io.IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                });

        loadHillButtons(primaryStage);
    }

    public void loadHillButtons(Stage primaryStage)
    {
        ScrollPane hillScroller = (ScrollPane)landingPageParent.lookup("#HillButtonScroller");

        VBox buttons = (VBox)hillScroller.getContent();
        buttons.getChildren().clear();

        currentTime = Instant.now();

        for (int i = 0; i < hills.size(); i++)
        {
            try {
                landingPageParent = FXMLLoader.load(getClass().getResource("fxml/landing_page.fxml"));
            } catch (java.io.IOException e) {
                System.out.println(e.getStackTrace());
            }

            Button hillButton = (Button)landingPageParent.lookup("#HillButton");
            Node hillButtonVBox = (VBox)hillButton.getGraphic().lookup("#HillButtonVBox");

            Hill hill = hills.get(i);

            hillButton.setLayoutX(0);
            hillButton.setLayoutY(0);
            hillButton.setId("AutoButton" + i);

            Text hillName = (Text)hillButtonVBox.lookup("#HillName");
            hillName.setText(hill.getName());

            Text regionName = (Text)hillButtonVBox.lookup("#RegionName");
            regionName.setText(hill.getCounty());

            Text hillScore = (Text)hillButtonVBox.lookup("#HillScore");
            hillScore.setText("" + hill.getPreciseScore(0));

            hillButton.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event)
                {
                    try
                    {
                        loadHillPage(primaryStage, hillButton);
                    } catch(Exception e) {
                        System.out.println(e.getStackTrace());
                        System.out.println(e.getMessage());
                    }
                }
            });

            buttons.getChildren().add(hillButton);
        }

        hillScroller.setContent(buttons);
    }

    public void loadHillRecommendations(Stage primaryStage)
    {
    }

    public void loadHillPage(Stage primaryStage, Button btn)
    {
        try
        {
            hillPageParent = FXMLLoader.load(getClass().getResource("fxml/hill_page.fxml"));
        }
        catch (java.io.IOException e)
        {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
        }

        hillPage = new Scene(hillPageParent);

        primaryStage.setScene(hillPage);
        primaryStage.show();

        int index = Integer.parseInt(btn.getId().substring(10));
        System.out.println(index);
        Hill hill = hills.get(index);

        Weather weather = hill.getHillWeatherMetric().getWeatherAt(currentTime);

        Label hillName = (Label)hillPageParent.lookup("#MountainName");
        hillName.setText(hill.getName());

        Label date = (Label)hillPageParent.lookup("#Date");
        LocalDateTime dateTime = LocalDateTime.ofInstant(currentTime, ZoneOffset.systemDefault());
        date.setText(DateTimeFormatter.ofPattern("dd/MM").format(dateTime));

        Label region = (Label)hillPageParent.lookup("#Region");
        region.setText(String.format("%s", hill.getCounty()));

        Label height = (Label)hillPageParent.lookup("#Height");
        height.setText(String.format("%.2fm", hill.getAltitude()));

        Label temp = (Label)hillPageParent.lookup("#Temp");
        temp.setText(String.format("%.1f", weather.getTemperature()));

        Label cloudCover = (Label)hillPageParent.lookup("#CloudCover");
        cloudCover.setText(String.format("%.1f", weather.getCloudCoverage()));

        Label rain = (Label)hillPageParent.lookup("#Rain");
        rain.setText(String.format("%.1f", weather.getPrecipitation()));

        Label windSpeed = (Label)hillPageParent.lookup("#Windspeed");
        windSpeed.setText(String.format("%.1f", weather.getWindSpeed()));

        int score = hill.getPreciseScore(0);

        setPieChart(hillPageParent, score);
        setScoreText(hillPageParent, score);

        Button closeButton = (Button)hillPageParent.lookup("#CloseButton");

        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event)
            {
                try
                {
                    loadLandingPage(primaryStage);
                } catch (Exception e) {}
            }
        });
    }

    public void setPieChart(Parent pageParent, int score) {
        PieChart scoreChart = (PieChart) pageParent.lookup("#MainScoreDial");
        scoreChart.setLayoutX(-20);
        scoreChart.setLayoutY(-5);
        scoreChart.setStartAngle(90);


        ObservableList<PieChart.Data> scoreData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Score", score),
                        new PieChart.Data("Excess", 100 - score));
        scoreChart.setData(scoreData);
    }

    public void setScoreText(Parent pageParent, int score) {
        Label scoreText = (Label) pageParent.lookup("#MainScoreNum");

        scoreText.setText(Integer.toString(score));
    }
}
