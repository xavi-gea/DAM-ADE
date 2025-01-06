package xgf.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class Database {
	
	private static MongoDatabase database;

	public static void connectToDatabase() {
		
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		
		try {
			
			database = mongoClient.getDatabase("casino");
			
		} catch (IllegalArgumentException e) {
			
			e.printStackTrace();
		}
		
	}

	public static void insertCardsToCollection(List<Card> cards, String collectionName) {
		
		database.getCollection(collectionName).drop();
		
		database.createCollection(collectionName);
		
		MongoCollection<Document> collection = database.getCollection(collectionName);
		
		List<Document> cardDocuments = new ArrayList<Document>();
		
		for (Card card : cards) {
			
			Document doc = new Document();
			doc.append("suit", card.getSuit());
			doc.append("points", card.getPoints());
			doc.append("base64", card.getBase64());
			
			cardDocuments.add(doc);
		}
			
		collection.insertMany(cardDocuments);		
	}

//	public static boolean userExists(String name, String password) {
//		
//		Bson queryUserPassword = and();
//		
//		MongoCollection<Document> users = database.getCollection("users");
//		
//		MongoCursor<Document> usersCursor = users.find(new Bson
//				and(
//						eq("user",name),
//						eq("pass",password)
//				)
//		);
//		
//		return false;
//	}

}
