/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Map;

import databaseinterface.GPSSet;
import java.io.File;
import java.util.Date;
import java.util.List;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author angus_000
 */
public class Cmpt352ui extends Application {

    private MapModel myModel;
    private List<GPSSet<Date, Double, Double>> toDraw;
    
    @Override
    public void start(Stage primaryStage) {

        myModel = new MapModel();

        Canvas myCanvas = new Canvas(1280, 720);
        GraphicsContext gc = myCanvas.getGraphicsContext2D();

        VBox root = new VBox();
        HBox controlPanel = new HBox();
        VBox devicePanel = new VBox();
        VBox startingTimePanel = new VBox();
        VBox endingTimePanel = new VBox();
        VBox submitPanel = new VBox();
        StackPane holder = new StackPane();

        controlPanel.setSpacing(20);

        devicePanel.setSpacing(10);
        Label deviceLabel = new Label("Device list: ");
        deviceLabel.setFont(Font.font("Arial", 14));
        ChoiceBox <String> deviceList = new ChoiceBox<>();
        deviceList.setItems(myModel.getDevice());

        startingTimePanel.setSpacing(10);
        Label startingTimeLabel = new Label("Starting Time:");
        TextField startingTimeField = new TextField("March 20, 2015");

        endingTimePanel.setSpacing(10);
        Label endingTimeLabel = new Label("Ending Time:");
        TextField endingTimeField = new TextField("April 2, 2015");

        submitPanel.setPadding(new Insets(25, 0, 0, 0));
        Button submitButton = new Button("Submit");
        submitButton.setOnAction((event) -> {
            myModel.setSelect(deviceList.getValue());
            myModel.setTime(startingTimeField.getText(), endingTimeField.getText());
            toDraw = myModel.getLocation();
            drawRoute(gc);
        });

        root.setPadding(new Insets(20, 20, 20, 20));
        root.setSpacing(20);

        holder.setMaxWidth(1280);
        holder.setMaxHeight(720);
        holder.getStyleClass().add("stackpane");

        devicePanel.getChildren().addAll(deviceLabel, deviceList);
        startingTimePanel.getChildren().addAll(startingTimeLabel, startingTimeField);
        endingTimePanel.getChildren().addAll(endingTimeLabel, endingTimeField);
        submitPanel.getChildren().addAll(submitButton);
        controlPanel.getChildren().addAll(devicePanel, startingTimePanel, endingTimePanel, submitPanel);
        holder.getChildren().addAll(myCanvas);
        root.getChildren().addAll(controlPanel, holder);

        Scene scene = new Scene(root, 1320, 830);
        File f = new File("src/Map/style/background.css");
        scene.getStylesheets().clear();
        scene.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));

        primaryStage.setTitle("CMPT352 Mapping");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void drawRoute(GraphicsContext gc){
        gc.clearRect(0, 0, 1280, 720);
        for(GPSSet<Date, Double, Double> i : toDraw){
            gc.fillOval(i.getLongitude(), i.getLatitude(), 5, 5);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
