package WhereToWalk;

import WhereToWalk.weather.*;

import java.net.URL;
import java.io.*;
import java.util.*;
import java.time.*;

import javafx.application.Application;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    Parent landingPageParent;
    Parent hillRecommendationsParent;
    Parent hillPageParent;

    Scene landingPage;
    Scene hillRecommendations;
    Scene hillPage;

    Scene mainScene;

    Boolean loadedLandingPage = false;
    Boolean loadedHillRecommendations = false;
    Boolean loadedHillPage = false;

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
        // landingPageParent = FXMLLoader.load(getClass().getResource("fxml/landing_page.fxml"));
        hillRecommendationsParent = FXMLLoader.load(getClass().getResource("fxml/hill_recommendations.fxml"));
        hillPageParent = FXMLLoader.load(getClass().getResource("fxml/hill_page.fxml"));

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

    public void loadLandingPage(Stage primaryStage) throws java.io.IOException
    {
        landingPageParent = FXMLLoader.load(getClass().getResource("fxml/landing_page.fxml"));
        if (!loadedLandingPage)
        {
            landingPage = new Scene(landingPageParent);
            loadedLandingPage = true;
        }
        primaryStage.setScene(landingPage);
        primaryStage.show();

        // ScrollPane = (ScrollPane)FXMLLoader.
        if (!loadedHills)
        {
            ScrollPane hillScroller = (ScrollPane)landingPageParent.lookup("#HillButtonScroller");

            VBox buttons = (VBox)hillScroller.lookup("#HillButtonScrollerVBOX");
            buttons.getChildren().clear();

            hills = Hills.getInstance().getNHills(100);
            currentTime = Instant.now();

            for (int i = 0; i < hills.size(); i++)
            {
                landingPageParent = FXMLLoader.load(getClass().getResource("fxml/landing_page.fxml"));
                Button hillButton = (Button)landingPageParent.lookup("#HillButton");
                // System.out.println(hillButton.getChildrenUnmodifiable().size());
                Node hillButtonVBox = (VBox)hillButton.getGraphic().lookup("#HillButtonVBox");

                Hill hill = hills.get(i);
                WeatherForecast forecast = hill.getHillWeatherMetric();
                Weather weather = forecast.getWeatherAt(currentTime);

                hillButton.setLayoutX(0);
                hillButton.setLayoutY(0);
                hillButton.setId("AutoButton" + i);

                Text hillName = (Text)hillButtonVBox.lookup("#HillName");
                hillName.setText(hill.getName());

                Text regionName = (Text)hillButtonVBox.lookup("#RegionName");
                regionName.setText(hill.getCounty());

                Text hillScore = (Text)hillButtonVBox.lookup("#HillScore");
                hillScore.setText("" + hill.getPreciseScore(0));

                ImageView hillIcon = (ImageView) hillButtonVBox.lookup("#WeatherIcon");

                String weatherIconString = pickWeatherIcon(weather);
                hillIcon.setImage(new Image("file:src/main/resources/WhereToWalk/assets/weather_condition_icons/" + weatherIconString));

                int score = hill.getPreciseScore(0);

                PieChart hillScoreDial = (PieChart) hillButtonVBox.lookup("#HillButtonScoreDial");
                hillScoreDial.setLayoutX(-49);
                hillScoreDial.setLayoutY(2);
                hillScoreDial.setStartAngle(90);


                ObservableList<PieChart.Data> scoreData =
                        FXCollections.observableArrayList(
                                new PieChart.Data("Score", score),
                                new PieChart.Data("Excess", 100 - score));
                hillScoreDial.setData(scoreData);

                hillButton.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event)
                    {
                        try
                        {
                            loadHillPage(primaryStage, hillButton);
                        } catch(Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                });

                buttons.getChildren().add(hillButton);
            }

            hillScroller.setContent(buttons);

            loadedHills = true;
        }
    }

    public void loadHillRecommendations(Stage primaryStage)
    {
    }

    public void loadHillPage(Stage primaryStage, Button btn) throws java.io.IOException
    {
        hillPageParent = FXMLLoader.load(getClass().getResource("fxml/hill_page.fxml"));
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
        date.setText(currentTime.toString());

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

        setPieChart(hillPageParent, "MainScoreDial", score);
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

    public void setPieChart(Parent pageParent, String pie_id, int score) {
        PieChart scoreChart = (PieChart) pageParent.lookup("#" + pie_id);
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


    public String pickWeatherIcon(Weather hillWeather) {

        double cloudCover = hillWeather.getCloudCoverage();
        double rain = hillWeather.getPrecipitation();
        double windSpeed = hillWeather.getWindSpeed();

        if (windSpeed > 8.0) {
            return "wind.png";
        }

        if (cloudCover < 20.0) {
            return "sun.png";
        } else if (cloudCover < 62.0){
            if (rain > 15.0) {
                return "sunshine_and_rainclouds.png";
            } else {
                return "sunshine_and_clouds.png";
            }
        } else {
            if (rain > 15.0) {
                return "raincloud.png";
            } else {
                return "cloud.png";
            }
        }

    }
}
