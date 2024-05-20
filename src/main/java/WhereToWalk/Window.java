package WhereToWalk;

import WhereToWalk.sorting.*;
import WhereToWalk.weather.*;

import java.io.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.FXMLLoader;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.stage.Stage;

public class Window extends Application {
    private final int mWidth = 480;
    // private final float aspect = 9.f/20.f;
    private final int mHeight = 800;

    private final int hillCount = 100;

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

    ObservableList<String> pieStylesList =
        FXCollections.observableArrayList(
            "@../css/pie_style_high.css",
            "@../css/pie_style_low.css",
            "@../css/pie_style_medium_high.css",
            "@../css/pie_style_medium_low.css");


    public static void main(String[] args) {
        launch();
    }

    /*
     * Entry point for the window, executed once the window has opened
     */
    @Override
    public void start(Stage primaryStage) {
        // Load the FXML Files
        try {
            hillRecommendationsParent =
                FXMLLoader.load(getClass().getResource("fxml/hill_recommendations.fxml"));
            hillPageParent = FXMLLoader.load(getClass().getResource("fxml/hill_page.fxml"));
        } catch (java.io.IOException e) {
            System.out.println("Failed to load FXML files");
        }

        // Set an initial sort for the hills
        Hills hills = Hills.getInstance();
        hills.setSorter(new ShortestDistance());
        this.hills = hills;

        // Load the initial landing page
        loadLandingPage(primaryStage);

        // Setup a primary stage settings and a basic key event handler
        // to close the window
        primaryStage.setResizable(false);
        primaryStage.addEventHandler(
                        KeyEvent.KEY_PRESSED,
        new EventHandler<KeyEvent>() {
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ESCAPE) {
                    primaryStage.close();
                }
                event.consume();
            }
        });
    }

    /*
     * Load the landing page and all the buttons required for the hills
     */
    public void loadLandingPage(Stage primaryStage) {
        // Load the FXML file
        try {
            landingPageParent = FXMLLoader.load(getClass().getResource("fxml/landing_page.fxml"));
        } catch (java.io.IOException e) {
            System.out.println(e.getStackTrace());
        }

        // If we have not loaded the page before then create a new scene
        if (!loadedLandingPage) {
            landingPage = new Scene(landingPageParent);
            loadedLandingPage = true;
        }

        // Set the scene to the landing page
        primaryStage.setScene(landingPage);
        primaryStage.show();

        // Retrieve the scroll pane from the scene
        ScrollPane hillScroller = (ScrollPane) landingPageParent.lookup("#HillButtonScroller");
        VBox buttons = (VBox) hillScroller.getContent();

        // Setup a callback for the search bar so that when the user
        // has searched for a hill, the matching hills are displayed
        TextField searchBar = (TextField) landingPageParent.lookup("#SearchBarField");
        EventHandler<ActionEvent> search =
        new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                hills.search(searchBar.getText().toLowerCase());
                try {
                    buttons.getChildren().clear();
                    loadNHillButtons(primaryStage, 10, hillScroller, buttons);
                    hillScroller.setVvalue(0);
                } catch (Exception e) {
                }
            }
        };
        searchBar.setOnAction(search);

        // Dynamically load more buttons when the user has scrolled
        // to the bottom of the scroll pane
        hillScroller
        .vvalueProperty()
        .addListener(
        new ChangeListener<Number>() {
            @Override
            public void changed(
                ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.doubleValue() == 1.0) {
                    loadNHillButtons(primaryStage, 10, hillScroller, buttons);
                }
            }
        });

        // Load 10 hills and create the buttons for those hills
        loadNHillButtons(primaryStage, 10, hillScroller, buttons);

        // Setup a callback for when the user clicks on the sort button
        MenuButton sortButton = (MenuButton) landingPage.lookup("#SortButton");
        MenuItem nameItem = sortButton.getItems().get(0);
        nameItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                buttons.getChildren().clear();
                Hills.getInstance().setSorter(new NameComparator());
                loadNHillButtons(primaryStage, 10, hillScroller, buttons);
                hillScroller.setVvalue(0);
            }
        });

        MenuItem distanceItem = sortButton.getItems().get(1);
        distanceItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                buttons.getChildren().clear();
                Hills.getInstance().setSorter(new ShortestDistance());
                loadNHillButtons(primaryStage, 10, hillScroller, buttons);
                hillScroller.setVvalue(0);
            }
        });

        MenuItem rankingItem = sortButton.getItems().get(2);
        rankingItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                buttons.getChildren().clear();
                Hills.getInstance().setSorter(new HighestScore());
                loadNHillButtons(primaryStage, 10, hillScroller, buttons);
                hillScroller.setVvalue(0);
            }
        });
        // sortButton.setOnAction(
        // new EventHandler<ActionEvent>() {
        // @Override
        // public void handle(ActionEvent actionEvent) {
        // // loadSortMenu(landingPage);
        // }
        // });
    }

    /*
     * Load 'n' more buttons into the 'buttons' vbox located within 'hillScroller'
     */
    public void loadNHillButtons(Stage primaryStage, int n, ScrollPane hillScroller, VBox buttons) {
        // Retreive 'n' hills that have not been loaded yet
        List<Hill> nHills = hills.getNHills(n);

        // Create the buttons for the newly loaded hills
        for (Hill hill : nHills) {
            try {
                landingPageParent = FXMLLoader.load(getClass().getResource("fxml/landing_page.fxml"));
            } catch (java.io.IOException e) {
                System.out.println(e.getStackTrace());
            }

            // Retrieve the relevant information about the hill and place that
            // information into the button for the user to see

            Button hillButton = (Button) landingPageParent.lookup("#HillButton");
            Node hillButtonVBox = (VBox) hillButton.getGraphic().lookup("#HillButtonVBox");

            hillButton.setLayoutX(0);
            hillButton.setLayoutY(0);
            // Set the ID of the button to be a unique identifier that relates
            // to the id of the hill
            hillButton.setId("AutoButton" + hill.getID());

            int score = artificialScoreSpread(hill.getPreciseScore());

            Text hillName = (Text) hillButtonVBox.lookup("#HillName");
            hillName.setText(hill.getName());

            Text regionName = (Text) hillButtonVBox.lookup("#RegionName");
            regionName.setText(hill.getCounty());

            Text hillScore = (Text) hillButtonVBox.lookup("#HillScore");
            hillScore.setText("" + score);

            ImageView hillIcon = (ImageView) hillButtonVBox.lookup("#WeatherIcon");

            Weather weather = hill.getHillWeatherMetric().getWeatherNow();

            String weatherIconString = pickWeatherIcon(weather);
            hillIcon.setImage(
                        new Image(
                            "file:src/main/resources/WhereToWalk/assets/weather_condition_icons/"
                            + weatherIconString));

            PieChart hillScoreDial = (PieChart) hillButtonVBox.lookup("#HillButtonScoreDial");
            hillScoreDial.setLayoutX(-49);
            hillScoreDial.setLayoutY(2);
            hillScoreDial.setStartAngle(90);

            ObservableList<PieChart.Data> scoreData = setPieData(score);

            hillScoreDial.setData(scoreData);

            // Setup a callback for the button so that when the user clicks
            // on a hill the relevant hill page is loaded
            hillButton.setOnAction(
            new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    try {
                        loadHillPage(primaryStage, hillButton);
                    } catch (Exception e) {
                        System.out.println(e.getStackTrace());
                    }
                }
            });

            // Add the new button to the list of existing buttons
            buttons.getChildren().add(hillButton);
        }

        // Set the vbox within hillScroller to the newly modified one
        hillScroller.setContent(buttons);
    }

    /*
     * Load the hill page that 'btn' directed too
     */
    public void loadHillPage(Stage primaryStage, Button btn) {
        try {
            hillPageParent = FXMLLoader.load(getClass().getResource("fxml/hill_page.fxml"));
        } catch (java.io.IOException e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
        }

        // Create a new hill page scene
        hillPage = new Scene(hillPageParent);

        // Set the scene
        primaryStage.setScene(hillPage);
        primaryStage.show();

        // Retrieve the ID of the hill was stored within the ID of the button
        // Format: AutoButton$ID$
        int id = Integer.parseInt(btn.getId().substring(10));

        try {
            // Retrieve the specified hill and retrieve the required information
            // and load it into the relevant fields of the hill page
            Hill hill = hills.getHillbyID(id);

            Weather weather = hill.getHillWeatherMetric().getWeatherNow();
            int score = artificialScoreSpread(hill.getPreciseScore());

            Label hillName = (Label) hillPageParent.lookup("#MountainName");
            hillName.setText(hill.getName());

            Label date = (Label) hillPageParent.lookup("#Date");
            LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.systemDefault());
            date.setText(DateTimeFormatter.ofPattern("dd/MM").format(dateTime));

            Label region = (Label) hillPageParent.lookup("#Region");
            region.setText(String.format("%s", hill.getCounty()));

            Label height = (Label) hillPageParent.lookup("#Height");
            height.setText(String.format("%.2fm", hill.getAltitude()));

            Label temp = (Label) hillPageParent.lookup("#Temp");
            temp.setText(String.format("%.1f", weather.getTemperature()));

            Label cloudCover = (Label) hillPageParent.lookup("#CloudCover");
            cloudCover.setText(String.format("%.1f", weather.getCloudCoverage()));

            Label rain = (Label) hillPageParent.lookup("#Rain");
            rain.setText(
                    String.format(
                        "%.1f", weather.getPrecipitation())); // millimetres where 0<x<2 is light shower

            Label windSpeed = (Label) hillPageParent.lookup("#Windspeed");
            windSpeed.setText(String.format("%.1f", weather.getWindSpeed()));

            Label recommendation = (Label) hillPageParent.lookup("#Recommendation");
            recommendation.setText(getRecommendationsText(weather, hill));

            System.out.println(score);
            setPieChart(hillPageParent, "MainScoreDial", score);
            setScoreText(hillPageParent, score);

            Button closeButton = (Button) hillPageParent.lookup("#CloseButton");

            // When the close button is clicked redirect back to the landing page
            closeButton.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        loadLandingPage(primaryStage);
                    } catch (Exception e) {
                    }
                }
            });
        } catch (Hills.NoSuchHillException e) {
            // No hill could be found that was identified by an ID
            // Shouldn't occur
            System.out.println("No such hill!");
            primaryStage.close();
        }
    }

    /*
     * Setup a specific pie chart within a page
     */
    public void setPieChart(Parent pageParent, String pie_id, int score) {
        PieChart scoreChart = (PieChart) pageParent.lookup("#" + pie_id);
        scoreChart.setLayoutX(-20);
        scoreChart.setLayoutY(-5);
        scoreChart.setStartAngle(90);

        ObservableList<PieChart.Data> scoreData = setPieData(score);
        scoreChart.setData(scoreData);
    }

    /*
     * Change the look of the pie chart depending on the score
     */
    public ObservableList<PieChart.Data> setPieData(int score) {
        ObservableList<PieChart.Data> scoreData;
        if (score <= 25) {
            scoreData =
                FXCollections.observableArrayList(
                    new PieChart.Data("Score", score),
                    new PieChart.Data("Blank", 0),
                    new PieChart.Data("Blank", 0),
                    new PieChart.Data("Blank", 0),
                    new PieChart.Data("Excess", 100 - score));
        } else if (score <= 50) {
            scoreData =
                FXCollections.observableArrayList(
                    new PieChart.Data("Blank", 0),
                    new PieChart.Data("Score", score),
                    new PieChart.Data("Blank", 0),
                    new PieChart.Data("Blank", 0),
                    new PieChart.Data("Excess", 100 - score));
        } else if (score <= 75) {
            scoreData =
                FXCollections.observableArrayList(
                    new PieChart.Data("Blank", 0),
                    new PieChart.Data("Blank", 0),
                    new PieChart.Data("Score", score),
                    new PieChart.Data("Blank", 0),
                    new PieChart.Data("Excess", 100 - score));
        } else {
            scoreData =
                FXCollections.observableArrayList(
                    new PieChart.Data("Blank", 0),
                    new PieChart.Data("Blank", 0),
                    new PieChart.Data("Blank", 0),
                    new PieChart.Data("Score", score),
                    new PieChart.Data("Excess", 100 - score));
        }
        return scoreData;
    }

    /*
     * Set the score text of a pie chart
     */
    public void setScoreText(Parent pageParent, int score) {
        Label scoreText = (Label) pageParent.lookup("#MainScoreNum");

        scoreText.setText(Integer.toString(score));
    }

    /*
     * Load the sort menu when the button has been pressed
     */
    public void loadSortMenu(Scene pageParent) {
        // MenuBar menuBar = (MenuBar) pageParent.lookup("#SortMenu");
        // if (menuBar.getLayoutX() != 280) {
        // menuBar.setLayoutX(280);
        // menuBar.setLayoutY(110);
        //
        // MenuItem name = (MenuItem)sortMenu.getItems().get(0);
        // name.setOnAction(new EventHandler<ActionEvent>() {
        // @Override
        // public void handle(ActionEvent event) {
        // System.out.println("Name");
        // }
        // });
        // } else {
        // menuBar.setLayoutX(-280);
        // }
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
        } else if (cloudCover < 62.0) {
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

    /*
     * TODO: Remove?
     * Modify the score spread so that hills appear different
     */
    public int artificialScoreSpread(int res) {
        // slightly cheeky code just to make things look a bit nicer for now:
        // res = res - 30;
        // res = (int) (res * 1.43);
        // if (res > 99) {
        // res = 99;
        // }
        // if (res < 0) {
        // res = 0;
        // }
        return res;
    }

    /*
     * Depending on the current weather, get a string that describes
     * the conditions and any recommendations if you were to walk that hill
     */
    public String getRecommendationsText(Weather hillWeather, Hill hill) {

        double cloudCover = hillWeather.getCloudCoverage();
        double rain = hillWeather.getPrecipitation();
        double windSpeed = hillWeather.getWindSpeed();

        if (hill.getAltitude() > 350.0) {

            if (windSpeed > 20.0) {
                return "Conditions today are particularly windy - take extra care around steep cliffs and"
                       + " make sure to pack a windproof jacket to protect against windchill.";
            }

            if (cloudCover < 20.0) {
                return "Expect glorious sunshine! Pack a sunhat and apply plenty of SPF! Don't forget that"
                       + " in the hills weather conditions can still change rapidly.";
            } else if (cloudCover < 62.0) {
                if (rain > 1.0) {
                    return "It's going to be a bit of a damp day but with the right equipment (waterproof"
                           + " jacket and trousers) you can still have a great day on the hills.";
                } else {
                    return "Conditions are fair - still a reasonable chance of a cloud-free summit.";
                }
            } else {
                if (rain > 1.0) {
                    return "Right - today is going to be a soggy day. Make the most of it by packing a good"
                           + " set of waterproofs and some spare dry clothes!";
                } else {

                    return "Don't expect brilliant views at the summit. Consider bringing a map and compass"
                           + " in case of particularly poor visibility.";
                }
            }

        } else {
            if (windSpeed > 20.0) {
                return "It's going to be blustery - for maximum comfort make sure to pack a windproof"
                       + " jacket and some extra layers.";
            }

            if (cloudCover < 20.0) {
                return "Expect glorious sunshine! Pack a sunhat and don't forget to reapply suncream"
                       + " throughout the day.";
            } else if (cloudCover < 62.0) {
                if (rain > 1.0) {
                    return "Slightly rainy conditions but with the right equipment (waterproof jacket and"
                           + " trousers) you'll still have a great day out. Expect the occasional patch"
                           + " of sunlight!";
                } else {
                    return "Conditions are fair - cloudy but with patches of sun! Enjoy the dry conditions!";
                }
            } else {
                if (rain > 1.0) {
                    return "Right - today is going to be a soggy day. Make the most of it by packing a good"
                           + " set of waterproofs and some spare dry clothes!";
                } else {
                    return "Don't expect much sun, but don't let this discourage you from having a great"
                           + " walk! Happiness comes from within :)";
                }
            }
        }
    }
}
