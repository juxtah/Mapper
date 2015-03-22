/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Map;

import java.io.File;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
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
    
    @Override
    public void start(Stage primaryStage) {
        
        myModel = new MapModel();
        
        Label deviceLabel = new Label("Device list: ");
        deviceLabel.setFont(Font.font("Arial", 14));
        
        ChoiceBox <String> deviceList = new ChoiceBox<>();
        deviceList.setItems(myModel.getDevice());
        
        Canvas myCanvas = new Canvas(1280, 720);        
        
        VBox root = new VBox();
        HBox controlPanel = new HBox();
        VBox devicePanel = new VBox();
        StackPane holder = new StackPane();
        
        controlPanel.setSpacing(20);
        
        devicePanel.setSpacing(10);
        
        root.setPadding(new Insets(20, 20, 20, 20));
        root.setSpacing(20);
        
        
        //holder.setStyle("-fx-background-image: url('UofSMap.jpg')");  
        //holder.setStyle("-fx-background-color: red");
        holder.setMaxWidth(1280);
        holder.setMaxHeight(720);
        holder.getStyleClass().add("stackpane");
        
        devicePanel.getChildren().addAll(deviceLabel, deviceList);
        controlPanel.getChildren().addAll(devicePanel);
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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
