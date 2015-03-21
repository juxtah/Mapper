/*
 * University of Saskatchewan
 * Computer Science
 * 
 */
package databaseinterface;

/**
 * 
 * @author ychen
 * @param <A> The first element
 * @param <B> The second element
 * @param <C> The third element
 */
public class GPSSet <A, B, C>{
	A time;
	B longitude;
	C latitude;
	
	public GPSSet(A t, B longitude, C latitude){
		this.time = t;
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	public A getTime(){
		return this.time;
	}
	
	public B getLongitude(){
		return this.longitude;
	}
	
	public C getLatitude(){
		return this.latitude;
	}
}
