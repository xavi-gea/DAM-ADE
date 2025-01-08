package xgf.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONObject;

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

	
	public static List<Card> getCardsFromCollection(String collectionName) {
		
		List<Card> cardList = new ArrayList<Card>();
		
		connectToDatabase();
		
		MongoCollection<Document> collection = database.getCollection(collectionName);
		
		MongoCursor<Document> cardCursor = collection.find().iterator();
		
		while (cardCursor.hasNext()) {
			
			JSONObject cardJson = new JSONObject(cardCursor.next().toJson());
			
			//System.out.println(cardJson.getJSONObject("_id").getString("$oid"));
			//System.out.println(cardJson.get("_id").toString());
			
			cardList.add(
					new Card(
							cardJson.getJSONObject("_id").getString("$oid"),
							cardJson.getString("suit"), 
							cardJson.getInt("points"),
							""
					)
			);
		}
		
		disconnectFromDatabase();
		
		return cardList;		
	}
	
	public static String getBase64FromCard(Card card, String collectionName) {
		
		String cardBase64 = "";
		
		connectToDatabase();
		
		//System.out.println(new ObjectId(card.getId()));
		
		Bson queryCardID = eq("_id", new ObjectId(card.getId()));
		
		MongoCollection<Document> cards = database.getCollection(collectionName);
		
		MongoCursor<Document> cardCursor = cards.find(queryCardID).iterator();
		
		if (cardCursor.hasNext()) {
			
			JSONObject cardJson = new JSONObject(cardCursor.next().toJson());
			
			cardBase64 = cardJson.getString("base64");
		}
		
		disconnectFromDatabase();
		
		return cardBase64;
	}
	
	public static void insertScore(String userName, String suit, Integer points) {
		
		connectToDatabase();
		
		MongoCollection<Document> collection = database.getCollection("scores");
		
		Document doc = new Document();
		doc.append("user", userName);
		doc.append("suit", suit);
		doc.append("points", points);
		doc.append("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
		
		collection.insertOne(doc);
		
		disconnectFromDatabase();
	}
}
