package WhereToWalk;

import WhereToWalk.sorting.ShortestDistance;
import WhereToWalk.weather.*;

import java.net.URL;
import java.io.*;
import java.util.*;
import java.time.*;
import java.time.format.*;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.util.Duration;


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

    Hills hills;
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

        Hills hills = Hills.getInstance();
        hills.setSorter(new ShortestDistance());
        this.hills = hills;

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
        ScrollPane hillScroller = (ScrollPane) landingPageParent.lookup("#HillButtonScroller");
        VBox buttons = (VBox)hillScroller.getContent();

        TextField searchBar = (TextField)landingPageParent.lookup("#SearchBarField");
        EventHandler<ActionEvent> search = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                hills.search(searchBar.getText().toLowerCase());
                try {
                    buttons.getChildren().clear();
                    loadNHillButtons(primaryStage, 10, hillScroller, buttons);
                }
                catch (Exception e) {};
            }
        };
        searchBar.setOnAction(search);

        hillScroller.vvalueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.doubleValue() == 1.0) {
                    loadNHillButtons(primaryStage, 10, hillScroller, buttons);
                }
            }
        });

        loadNHillButtons(primaryStage, 10, hillScroller, buttons);
    }

    public void loadNHillButtons(Stage primaryStage, int n, ScrollPane hillScroller, VBox buttons)
    {
//        ScrollPane hillScroller = (ScrollPane)landingPageParent.lookup("#HillButtonScroller");
//
//        VBox buttons = (VBox)hillScroller.getContent();
        // buttons.setMinHeight(600);
//        buttons.getChildren().clear();

        List<Hill> nHills = hills.getNHills(n);
        for (Hill hill : nHills)
        {
            try {
                landingPageParent = FXMLLoader.load(getClass().getResource("fxml/landing_page.fxml"));
            } catch (java.io.IOException e) {
                System.out.println(e.getStackTrace());
            }

            Button hillButton = (Button)landingPageParent.lookup("#HillButton");
            Node hillButtonVBox = (VBox)hillButton.getGraphic().lookup("#HillButtonVBox");


            hillButton.setLayoutX(0);
            hillButton.setLayoutY(0);
            hillButton.setId("AutoButton" + hill.getID());

            int score = hill.getPreciseScore();

            Text hillName = (Text)hillButtonVBox.lookup("#HillName");
            hillName.setText(hill.getName());

            Text regionName = (Text)hillButtonVBox.lookup("#RegionName");
            regionName.setText(hill.getCounty());

            Text hillScore = (Text)hillButtonVBox.lookup("#HillScore");
            hillScore.setText("" + score);

            ImageView hillIcon = (ImageView) hillButtonVBox.lookup("#WeatherIcon");

            Weather weather = hill.getHillWeatherMetric().getWeatherNow();

            String weatherIconString = pickWeatherIcon(weather);
            hillIcon.setImage(new Image("file:src/main/resources/WhereToWalk/assets/weather_condition_icons/" + weatherIconString));


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

        int id = Integer.parseInt(btn.getId().substring(10));
        try {
        Hill hill = hills.getHillbyID(id);


        Weather weather = hill.getHillWeatherMetric().getWeatherNow();
        int score = hill.getPreciseScore();

        Label hillName = (Label)hillPageParent.lookup("#MountainName");
        hillName.setText(hill.getName());

        Label date = (Label)hillPageParent.lookup("#Date");
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.systemDefault());
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
        rain.setText(String.format("%.1f", weather.getPrecipitation())); //millimetres where 0<x<2 is light shower

        Label windSpeed = (Label)hillPageParent.lookup("#Windspeed");
        windSpeed.setText(String.format("%.1f", weather.getWindSpeed()));

        Label recommendation = (Label)hillPageParent.lookup("#Recommendation");
        recommendation.setText(getRecommendationsText(weather));

        System.out.println(score);
        setPieChart(hillPageParent, "MainScoreDial", score);
        setScoreText(hillPageParent, score);

        Button closeButton = (Button)hillPageParent.lookup("#CloseButton");

        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event)
            {
                try
                {
                    System.out.println("Ye");
                    loadLandingPage(primaryStage);
                } catch (Exception e) {}
            }
        }); } catch (Hills.NoSuchHillException e) {
            System.out.println("No such hill!");
            primaryStage.close();
        }


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

        if (windSpeed > 20.0) {
            return "wind.png";
        }

        if (cloudCover < 20.0) {
            return "sun.png";
        } else if (cloudCover < 62.0){
            if (rain > 1.0) {
                return "sunshine_and_rainclouds.png";
            } else {
                return "sunshine_and_clouds.png";
            }
        } else {
            if (rain > 1.0) {
                return "raincloud.png";
            } else {
                return "cloud.png";
            }
        }

    }


    public String getRecommendationsText(Weather hillWeather) {

        double cloudCover = hillWeather.getCloudCoverage();
        double rain = hillWeather.getPrecipitation();
        double windSpeed = hillWeather.getWindSpeed();

        if (windSpeed > 20.0) {
            return "Conditions today are particularly windy - take extra care around steep cliffs and make sure to pack a windproof jacket to protect against windchill.";
        }

        if (cloudCover < 20.0) {
            return "Expect glorious sunshine! Pack a sunhat and apply plenty of SPF! Don't forget that in the hills weather conditions can still change rapidly.";
        } else if (cloudCover < 62.0){
            if (rain > 1.0) {
                return "It's going to be a bit of a damp day but with the right equipment (waterproof jacket and trousers) you still have a great day on the hills.";
            } else {
                return "Conditions are fair - still a reasonable chance of a cloud-free summit.";
            }
        } else {
            if (rain > 1.0) {
                return "Right - today is going to be a soggy day. Make the most of it by packing a good set of waterproofs and some spare dry clothes!";
            } else {
                //TODO factor in hill altitude to these recommendations
                return "Don't expect brilliant views at the summit. Consider bringing a map and compass in case of particularly poor visibility.";
            }
        }

    }
}
