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
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author angus_000
 */
public class Cmpt352ui extends Application {

    private MapModel myModel;
    private List<GPSSet<Date, Double, Double>> toDraw;
    private Label info;
    private CheckBox drawLineToggle;

    public Cmpt352ui() {
        this.mouseHandler = (MouseEvent mouseEvent) -> {
            //System.out.println(mouseEvent.getEventType() + "\n" + "X : Y - " + mouseEvent.getX() + " : " + mouseEvent.getY() + "\n");
            GPSSet<Date, Double, Double> temp = myModel.showInfo(mouseEvent.getX(), mouseEvent.getY());
            if (temp != null) {
                //System.out.println("mouse on object");
                //System.out.println(temp.getTime().toString() + " Location: " + temp.getLatitude() + " " + temp.getLongitude());
                if (mouseEvent.getX() <= 815) {
                    info.relocate(mouseEvent.getX() + 10, mouseEvent.getY());
                } else {
                    info.relocate(mouseEvent.getX() - 425, mouseEvent.getY());
                }
                info.setText(temp.getTime().toString() /*" Location: " + temp.getLatitude() + " " + temp.getLongitude()*/);
            }
        };
    }

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
        Pane holder = new Pane();

        info = new Label();
        info.getStyleClass().add("infoLabel");

        controlPanel.setSpacing(30);

        devicePanel.setSpacing(10);
        Label deviceLabel = new Label("Device list: ");
        deviceLabel.setFont(Font.font("Arial", 14));
        ChoiceBox<String> deviceList = new ChoiceBox<>();
        deviceList.setItems(myModel.getDevice());
        deviceList.setPrefWidth(350);

        startingTimePanel.setSpacing(10);
        Label startingTimeLabel = new Label("Starting Time:");
        HBox startingTimeDate = new HBox();
        startingTimeDate.setSpacing(5);
        TextField startingHr = new TextField("8");
        startingHr.setPrefWidth(40);
        Label startingHrLabel = new Label("Hr");
        startingHrLabel.setPadding(new Insets(5, 0, 0, 0));
        TextField startingMin = new TextField("30");
        startingMin.setPrefWidth(40);
        Label startingMinLabel = new Label("Min");
        startingMinLabel.setPadding(new Insets(5, 0, 0, 0));
        TextField startingDay = new TextField("27");
        startingDay.setPrefWidth(40);
        ChoiceBox<String> startingMonth = new ChoiceBox<>();
        startingMonth.getItems().addAll("January", "Febuary", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
        startingMonth.getSelectionModel().select(2);
        TextField startingYear = new TextField("2015");
        startingYear.setMaxWidth(50);
        startingTimeDate.getChildren().addAll(startingHr, startingHrLabel, startingMin, startingMinLabel, startingDay, startingMonth, startingYear);

        endingTimePanel.setSpacing(10);
        Label endingTimeLabel = new Label("Ending Time:");
        HBox endingTimeDate = new HBox();
        endingTimeDate.setSpacing(5);
        TextField endingHr = new TextField("22");
        endingHr.setPrefWidth(40);
        Label endingHrLabel = new Label("Hr");
        endingHrLabel.setPadding(new Insets(5, 0, 0, 0));
        TextField endingMin = new TextField("00");
        endingMin.setPrefWidth(40);
        Label endingMinLabel = new Label("Min");
        endingMinLabel.setPadding(new Insets(5, 0, 0, 0));
        TextField endingDay = new TextField("27");
        endingDay.setPrefWidth(40);
        ChoiceBox<String> endingMonth = new ChoiceBox<>();
        endingMonth.getItems().addAll("January", "Febuary", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
        endingMonth.getSelectionModel().select(2);
        TextField endingYear = new TextField("2015");
        endingYear.setMaxWidth(50);
        endingTimeDate.getChildren().addAll(endingHr, endingHrLabel, endingMin, endingMinLabel, endingDay, endingMonth, endingYear);

        submitPanel.setPadding(new Insets(25, 0, 0, 0));
        submitPanel.setSpacing(10);
        drawLineToggle = new CheckBox("Draw Line");
        drawLineToggle.setSelected(true);
        Button submitButton = new Button("Submit");
        submitButton.setOnAction((event) -> {
            myModel.setSelect(deviceList.getValue());
            String startingTimeTemp = startingHr.getText() + " " + startingMin.getText() + " " + startingDay.getText() + " " + startingMonth.getValue() + " " + startingYear.getText();
            String endingTimeTemp = endingHr.getText() + " " + endingMin.getText() + " " + endingDay.getText() + " " + endingMonth.getValue() + " " + endingYear.getText();
            myModel.setTime(startingTimeTemp, endingTimeTemp);
            toDraw = myModel.getLocation();
            drawRoute(gc);
        });

        holder.setOnMouseMoved(mouseHandler);

        root.setPadding(new Insets(20, 20, 20, 20));
        root.setSpacing(20);

        holder.setMaxWidth(1280);
        holder.setMaxHeight(720);
        holder.getStyleClass().add("stackpane");

        devicePanel.getChildren().addAll(deviceLabel, deviceList);
        startingTimePanel.getChildren().addAll(startingTimeLabel, startingTimeDate);
        endingTimePanel.getChildren().addAll(endingTimeLabel, endingTimeDate);
        submitPanel.getChildren().addAll(drawLineToggle, submitButton);
        controlPanel.getChildren().addAll(devicePanel, startingTimePanel, endingTimePanel, submitPanel);
        holder.getChildren().addAll(info, myCanvas);

        root.getChildren().addAll(controlPanel, holder);

        Scene scene = new Scene(root, 1320, 830);
        File f = new File("src/Map/style/background.css");
        scene.getStylesheets().clear();
        scene.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));

        primaryStage.setTitle("CMPT352 Mapping");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    EventHandler<MouseEvent> mouseHandler;

    private void drawRoute(GraphicsContext gc) {
        gc.clearRect(0, 0, 1280, 720);
        GPSSet<Date, Double, Double> prevGPS = null;
        for (GPSSet<Date, Double, Double> i : toDraw) {
            if (myModel.inMap(i)) {
                gc.fillOval(i.getLongitude() - 2.5, i.getLatitude() - 2.5, 5, 5);
            }
            if (prevGPS == null) {
                prevGPS = i;
            } else if (drawLineToggle.isSelected()&&(myModel.inMap(i) || myModel.inMap(prevGPS))) {
                if (myModel.distRelative(i, prevGPS) > 400) {
                    gc.setStroke(Color.RED);
                    gc.strokeLine(i.getLongitude(), i.getLatitude(), prevGPS.getLongitude(), prevGPS.getLatitude());
                } else {
                    gc.setStroke(Color.BLUE);
                    gc.strokeLine(i.getLongitude(), i.getLatitude(), prevGPS.getLongitude(), prevGPS.getLatitude());
                }
                prevGPS = i;
            } else {
                prevGPS = i;
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
