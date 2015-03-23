/*
 * University of Saskatchewan
 * Computer Science
 * 
 */
package databaseinterface;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ychen
 */
public class DatabaseInterface {
	private final static String USER = "ychen";
	private final static String PASS = "th0ught";

	/**
	 * We have an empty constructor so someone doesn't accidentally instantiate this class
	 */
	public DatabaseInterface(){}
	
	/**
	 * Internal method only. Cannot be called otherwise.
	 * Obtains the number of rows returned in the query.
	 * result should always be not null. But in the off chance that it is, it will return -1
	 * @param result the result set of a query
	 * @return the size of the result set
	 */
	private static int getResultSize(ResultSet result){
		try{
			result.last();
			int size = result.getRow();
			// we must reset the cursor to the beginning before returning
			result.beforeFirst();
			return size;
		}
		catch (SQLException e){
			System.err.println("ERROR: " + e.getMessage());
			return -1;
		}

	}
	
	public static Connection instantiateConnection(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			return DriverManager.getConnection("jdbc:mysql://projectindigo.ca:3306", USER, PASS);
		}
		catch (SQLException e){
			System.err.println("ERROR: " + e.getMessage());
			return null;
		}
		catch (ClassNotFoundException e){
			return null;
		}
	}
	
	/**
	 * Returns a String [] containing all the devices registered in the database.
	 * If the connection is null, it throw an IllegalStateException
	 * @param conn The instantiated SQL connection
	 * @return an array containing all devices registered in the database
	 * @precond the connection cannot be null
	 * @throws IllegalStateException
	 */
	public static String [] getDeviceList(Connection conn) throws IllegalStateException{
		if (conn == null) throw new IllegalStateException("ERROR: The connection was not instantiated or dropped!");
		String [] r = null;
		try {
			ResultSet result = conn.createStatement().executeQuery("SELECT DISTINCT device_id FROM bigbrother.gps_data");
			int size = getResultSize(result);
			r = new String[size];
			for (int i=0; i<size; i++){
				result.next();
				r[i] = result.getString("device_id");
			}
		} catch (SQLException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
		return r;
	}
	
	/**
	 * converts a given integer epoch representation of the date into a human readable format
	 * @param epoch the UNIX epoch representation of the time
	 * @return d :: date object
	 */
	private static Date convertEpochToReadable(long epoch){
		Date d = new Date(epoch*1000);
		return d;
	}
	
	/**
	 * Queries the database to obtain ALL timestamp and GPS coordinates for a specific device_id. 
	 * @param conn An already instantiated connection.
	 * @param device_id The string representation of the device_id column
	 * @param start_time The UNIX epoch representation of the start time.
	 * @param end_time The UNIX epoch representation of the end time.
	 * @return An array of triples (GPSSet) containing <Time, Longitude, Latitude> ordered ascending by time
	 * NOTE: Time is a readable string representation of the time. Do NOT use epoch time to store.
	 * Time format should be standard Date (complete the conversion method first)
	 * 
	 * Columns: Time = time | longitude = longitude | latitude = lat
	 */
	public static List<GPSSet<Date, Double, Double>> getAllCoordinates(Connection conn, String device_id, long start_time, long end_time){
		if (conn == null){
			System.err.println("ERROR: Connection has not been instantiated!");
			return null;
		}
		List<GPSSet<Date, Double, Double>> set = null;
		// get the time range
		String s = "SELECT * FROM bigbrother.gps_data WHERE device_id = ? AND time >= ? AND time <= ? ORDER BY time ASC";
		try{
			PreparedStatement q = conn.prepareStatement(s);		
			q.setString(1, device_id);
			q.setLong(2, start_time);
			q.setLong(3, end_time);
			ResultSet r = q.executeQuery();
			int size = getResultSize(r);
			set = new ArrayList<>(size);
			for (int i=0; i<size; i++){
				r.next();
				set.add(new GPSSet<>(convertEpochToReadable(r.getLong("time")), r.getDouble("longitude"), r.getDouble("lat")));
			}	
		}
		catch (SQLException e){
			System.err.println(e.getMessage());
		}
		catch (Exception e){
			System.err.println(e.getMessage());
		}
		return set;		// remove this when finished writing. Only here to prevent compiler errors
	}

	public static void destroyConnection(Connection conn){
		try{
			conn.close();
		}
		catch (SQLException e){
			System.err.println("ERROR: " + e.getMessage());
		}
		catch (Exception e){
			System.err.println("ERROR: " + e.getMessage());
		}
	}
	
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		// TODO code application logic here
		Connection conn = instantiateConnection();
		String [] r = getDeviceList(conn);
		if (r != null){
			for (String i : r){
				System.out.println(i);
			}
		}
		
		Date d = convertEpochToReadable(1426869568L);
		System.out.println(d);
	}
	
}
