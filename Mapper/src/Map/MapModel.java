/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Map;

import static databaseinterface.DatabaseInterface.destroyConnection;
import static databaseinterface.DatabaseInterface.getAllCoordinates;
import static databaseinterface.DatabaseInterface.getDeviceList;
import static databaseinterface.DatabaseInterface.instantiateConnection;
import databaseinterface.GPSSet;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author angus_000
 */
public class MapModel {

    private ObservableList<deviceData> DeviceList = FXCollections.observableArrayList();
    private Date startingTime, endingTime;
    private deviceData selected;
    private List<GPSSet<Date, Double, Double>> locations;

    public MapModel() {
        Connection conn = instantiateConnection();
        String[] r = getDeviceList(conn);
        destroyConnection(conn);
        for (String i : r) {
            if (r != null) {
                DeviceList.addAll(new deviceData(i));
            }
        }
        startingTime = null;
        endingTime = null;
        selected = null;
        locations= new ArrayList<>();
    }

    public ObservableList<String> getDevice() {
        ObservableList<String> temp = FXCollections.observableArrayList();
        for (deviceData i : DeviceList) {
            temp.add(i.getDeviceName());
        }
        return temp;
    }

    public void setSelect(String name) {
        for (deviceData i : DeviceList) {
            if (name.equals(i.getDeviceName())) {
                selected = i;
            }
        }
    }

    public void setTime(String start, String end) {
        try {
            DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
            startingTime = format.parse(start);
            endingTime = format.parse(end);
        } catch (ParseException ex) {
            Logger.getLogger(MapModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Time set, start:"+startingTime.getTime() + " compare:"+endingTime.getTime());
    }
    
    public List<GPSSet<Date, Double, Double>> getLocation(){
        Connection conn = instantiateConnection();
        List<GPSSet<Date, Double, Double>> temp = getAllCoordinates(conn, selected.getDeviceName(), startingTime.getTime()/1000, endingTime.getTime()/1000);
        destroyConnection(conn);
        locations.clear();
        for(GPSSet<Date, Double, Double> i : temp){
            System.out.println(i);
            locations.add(convertRelativePoint(i));
        }
        return locations;
    }

    private GPSSet<Date, Double, Double> convertRelativePoint(GPSSet<Date, Double, Double> old){
        double newLat = (52.13372-(double)old.getLatitude())/((52.13372-52.12894)/720);
        double newLong = ((double)old.getLongitude()-(-106.6414))/(((-106.6276)-(-106.6414))/1280);
        GPSSet<Date, Double, Double> newGPS = new GPSSet<>(old.getTime(), newLong, newLat);
        System.out.println(newGPS.getTime().toString() + newGPS.getLatitude() + newGPS.getLongitude());
        return newGPS;
    }
    
}
