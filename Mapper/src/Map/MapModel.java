/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Map;

import static databaseinterface.DatabaseInterface.destroyConnection;
import static databaseinterface.DatabaseInterface.getDeviceList;
import static databaseinterface.DatabaseInterface.instantiateConnection;
import databaseinterface.GPSSet;
import java.sql.Connection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author angus_000
 */
public class MapModel {
    
    private ObservableList<String> DeviceList = FXCollections.observableArrayList();
    
    public MapModel(){
        Connection conn = instantiateConnection();
        String [] r = getDeviceList(conn);
        destroyConnection(conn);
        if (r != null){
            for (String i : r){
                DeviceList.add(i);
            }
        }
    }
    
    public ObservableList<String> getDevice(){
        return DeviceList;
    }
    
    private GPSSet convertRelativePoint(GPSSet old){
        
        
        double newLat = (52.13372-(float)old.getLatitude())*((52.13372-52.12894)/720);
        double newLong = ((float)old.getLongitude()-(-106.6414))*(((-106.6276)-(-106.6414))/1280);
        GPSSet newGPS = new GPSSet(old.getTime(), newLong, newLat);
        return newGPS;
    }
    
}
