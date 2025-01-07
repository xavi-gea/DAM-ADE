package xgf.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import static com.mongodb.client.model.Filters.*;

public class Database {
	
	private static MongoClient mongoClient;
	private static MongoDatabase database;

	public static void connectToDatabase() {
		
		try {
			
			mongoClient = new MongoClient("localhost", 27017);
			
			database = mongoClient.getDatabase("casino");
			
		} catch (IllegalArgumentException e) {
			
			e.printStackTrace();
		}
	}
	
	public static void disconnectFromDatabase() {
		
		mongoClient.close();
	}

	public static void insertCardsToCollection(List<Card> cards, String collectionName) {
		
		connectToDatabase();
		
		database.getCollection(collectionName).drop();
		
		database.createCollection(collectionName);
		
		MongoCollection<Document> collection = database.getCollection(collectionName);
		
		List<Document> cardDocuments = new ArrayList<Document>();
		
		for (Card card : cards) {
			
			Integer cardPoints = card.getPoints();
			 
			if (cardPoints >= 11) {
				
				cardPoints = 10;
			}
			
			Document doc = new Document();
			doc.append("suit", card.getSuit());
			doc.append("points", cardPoints);
			doc.append("base64", card.getBase64());
			
			cardDocuments.add(doc);
		}
			
		collection.insertMany(cardDocuments);
		
		disconnectFromDatabase();
	}

	public static boolean userExists(String name, String password) {
		
		connectToDatabase();
		
		Bson queryUserPassword = and(
				eq("user", name), 
				eq("pass", password)
		);
		
		MongoCollection<Document> users = database.getCollection("users");
		
		MongoCursor<Document> usersCursor = users.find(queryUserPassword).iterator();
		
		disconnectFromDatabase();
		
		return usersCursor.hasNext() ? true : false;
	}

	public static void setUpUser(String name, String password) {
		
		connectToDatabase();
		
		MongoCollection<Document> users = database.getCollection("users");
		
		Document doc = new Document();
		doc.append("user", name);
		doc.append("pass", password);
		
		users.insertOne(doc);
		
		disconnectFromDatabase();
	}

}
