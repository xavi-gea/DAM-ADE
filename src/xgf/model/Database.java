package xgf.model;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Database {
	
	private static MongoDatabase database;

	public static void connectToDatabase() {
		
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		
		try {
			
			database = mongoClient.getDatabase("casino");
			
			//MongoCollection<Document> users = database.getCollection("users");
			
		} catch (IllegalArgumentException e) {
			
			e.printStackTrace();
		}
		
	}

}
