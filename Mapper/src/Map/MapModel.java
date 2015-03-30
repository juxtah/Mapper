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

    private ObservableList<String> DeviceList = FXCollections.observableArrayList();
    private Date startingTime, endingTime;
    private String selected;
    private List<GPSSet<Date, Double, Double>> locations;
    private List<GPSSet<Date, Double, Double>> convertedLocations;

    public MapModel() {
        Connection conn = instantiateConnection();
        String[] r = getDeviceList(conn);
        destroyConnection(conn);
            if (r != null){    //If you check for r after doing a for loop, you're going to have a bad time.
            for (String i : r)
                DeviceList.addAll(i);
        }
        
        startingTime = null;
        endingTime = null;
        selected = null;
        locations= new ArrayList<>();
        convertedLocations = new ArrayList<>();
    }

    public ObservableList<String> getDevice() {
        return DeviceList;
    }

    public void setSelect(String name) {
        DeviceList.stream().filter((i) -> (name.equals(i))).forEach((i) -> {
            selected = i;
        });
    }

    public void setTime(String start, String end) {
        try {
            DateFormat format = new SimpleDateFormat("H m d MMMM yyyy", Locale.ENGLISH);
            startingTime = format.parse(start);
            endingTime = format.parse(end);
        } catch (ParseException ex) {
            Logger.getLogger(MapModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Time set, start:"+startingTime.getTime() + " end:"+endingTime.getTime());
    }
    
    public List<GPSSet<Date, Double, Double>> getLocation(){
        Connection conn = instantiateConnection();
        locations.clear();
        convertedLocations.clear();
        getAllCoordinates(conn, selected, startingTime.getTime()/1000, endingTime.getTime()/1000).stream().map((i) -> {
            System.out.println(i);
            return i;
        }).map((i) -> {
            locations.add(i);
            return i;
        }).forEach((i) -> {
            convertedLocations.add(convertRelativePoint(i));
        });
        destroyConnection(conn);
        return convertedLocations;
    }
    
    public void removeObsoletePoints(){
        int distanceEstimated = 360;
        GPSSet<Date, Double, Double> prefGPS = null;
        List<GPSSet<Date, Double, Double>> toRemove = new ArrayList<>();
        int count = 1;
        for(GPSSet<Date, Double, Double> i : locations){
            
            if(prefGPS == null){
                prefGPS=i;
            }else if(distRealGPS(i, prefGPS)>=distanceEstimated*count){
                toRemove.add(i);
                count++;
                //System.out.println("i removed");
            }else{
                prefGPS=i;
                count = 1;
            }
        }
        toRemove.stream().map((i) -> {
            convertedLocations.remove(locations.indexOf(i));
            return i;
        }).forEach((i) -> {
            locations.remove(i);
        });
    }
    
    public double distRealGPS(GPSSet<Date, Double, Double> x, GPSSet<Date, Double, Double> y){
        double xdiff = (x.getLongitude() - y.getLongitude())*100000/1.1;
        double ydiff = (x.getLatitude() - y.getLatitude())*100000/1.1;
        //System.out.println("xdiff:" + xdiff + " ydiff:" + ydiff + " dist:" + Math.sqrt(xdiff*xdiff+ydiff*ydiff));
        return Math.sqrt(xdiff*xdiff+ydiff*ydiff);
    }
    
    public double distRelative(GPSSet<Date, Double, Double> x, GPSSet<Date, Double, Double> y){
        double xdiff = (x.getLongitude() - y.getLongitude());
        double ydiff = (x.getLatitude() - y.getLatitude());
        //System.out.println("xdiff:" + xdiff + " ydiff:" + ydiff + " dist:" + Math.sqrt(xdiff*xdiff+ydiff*ydiff));
        return Math.sqrt(xdiff*xdiff+ydiff*ydiff);
    }

    public GPSSet<Date, Double, Double> showInfo(double X, double Y){
        for(GPSSet<Date, Double, Double> i : convertedLocations){
            if(X<=i.getLongitude()+5&&X>=i.getLongitude()-5&&Y<=i.getLatitude()+5&&Y>=i.getLatitude()-5){
                return locations.get(convertedLocations.indexOf(i));
            }
        }
        return null;
    }
    
    private GPSSet<Date, Double, Double> convertRelativePoint(GPSSet<Date, Double, Double> old){
        double newLat = (52.133684-(double)old.getLatitude())/((52.133684-52.128953)/720);
        double newLong = ((double)old.getLongitude()-(-106.641304))/(((-106.627518)-(-106.641304))/1280);
        GPSSet<Date, Double, Double> newGPS = new GPSSet<>(old.getTime(), newLong, newLat);
        System.out.println(newGPS.getTime().toString() + newGPS.getLatitude() + newGPS.getLongitude());
        return newGPS;
    }
    
    public boolean inMap(GPSSet<Date, Double, Double> loc){
        return (loc.getLatitude()>=0&&loc.getLatitude()<=720&&loc.getLongitude()>=0&&loc.getLongitude()<=1280);
    }
    
}
