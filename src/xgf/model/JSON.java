package xgf.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Xavi
 */
public class JSON {

	private Path jsonFileLocation = Paths.get("." + File.separator + "config_local.json");
	private static String user;
	private static String pass;
	private static String ip;
	private static String port;
	private static String database;
	private static JSONArray collections;
	
	/**
	 * Set up static variables with data obtained from JSON
	 */
	public JSON(){
		
		String jsonString = "";
		
		try {
			
			jsonString = Files.readString(jsonFileLocation);
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}	
		
		JSONObject jsonContent = new JSONObject(jsonString);
		
		user = jsonContent.getString("user");
		pass = jsonContent.getString("pass");
		port = jsonContent.getString("port");
		database = jsonContent.getString("database");
		collections = jsonContent.getJSONArray("collections");
	}

	public static String getUser() {
		return user;
	}

	public static String getPass() {
		return pass;
	}

	public static String getIp() {
		return ip;
	}

	public static String getPort() {
		return port;
	}

	public static String getDatabase() {
		return database;
	}

	public static JSONArray getCollections() {
		return collections;
	}
}
