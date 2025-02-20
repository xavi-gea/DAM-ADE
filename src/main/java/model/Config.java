package model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.JSONObject;

public class Config {

	private final Path jsonFileLocation = Paths.get("." + File.separator + "config_local.json");
	private static String user;
	private static String pass;
	private static String ip;
	private static String port;
	private static String database;
	private static JSONObject collections;
	
	/**
	 * Set up static variables with data obtained from JSON
	 */
	public Config(){
		
		String jsonString = "";
		
		try {
			
			jsonString = Files.readString(jsonFileLocation);
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		JSONObject jsonContent = new JSONObject(jsonString);
		
		user = jsonContent.getString("user");
		pass = jsonContent.getString("pass");
		ip = jsonContent.getString("ip");
		port = jsonContent.getString("port");
		database = jsonContent.getString("database");
		collections = jsonContent.getJSONObject("collections");
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

	public static JSONObject getCollections() {
		return collections;
	}
}
