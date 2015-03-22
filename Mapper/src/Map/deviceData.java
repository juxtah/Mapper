/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Map;

import databaseinterface.GPSSet;
import java.util.Date;

/**
 *
 * @author angus_000
 */
public class deviceData {
    private String deviceName;
    private GPSSet[] GPSdata;
    
    public deviceData(String name, GPSSet[] data){
        deviceName = name;
        GPSdata = data;
        for(GPSSet i : GPSdata){
            i = convertRelativePoint(i);
        }
    }
    
    public deviceData(String name){
        deviceName = name;
        GPSdata = null;
    }
    
    public String getDeviceName(){
        return deviceName;
    }
    
    public GPSSet[] getGPSdata(){
        return GPSdata;
    }
    
    private GPSSet convertRelativePoint(GPSSet old){
        double newLat = (52.13372-(double)old.getLatitude())*((52.13372-52.12894)/720);
        double newLong = ((double)old.getLongitude()-(-106.6414))*(((-106.6276)-(-106.6414))/1280);
        GPSSet <Date, Double, Double> newGPS = new GPSSet<>((Date)old.getTime(), newLong, newLat);
        return newGPS;
    }
}
