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
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import static com.mongodb.client.model.Sorts.*;
import static com.mongodb.client.model.Filters.*;

/**
 * @author Xavi
 */
public class Database {
	
	private static MongoClient mongoClient;
	private static MongoDatabase database;
	private static MongoClientURI connectionString;

	/**
	 * Set the static variable "connectionString" with the connection string that will be used to connect to the database 
	 */
	public static void setConnectionString() {
		
		final String textTemplate = "mongodb://%s:%s@localhost:%s/";
        
		connectionString = new MongoClientURI(String.format(textTemplate, Config.getUser(),Config.getPass(),Config.getPort()));
	}

	/**
	 * Tries to connect to the MongoDB
	 */
	public static void connectToDatabase() {
		
		try {
			
			mongoClient = new MongoClient(connectionString);
			
			database = mongoClient.getDatabase(Config.getDatabase());
			
		} catch (IllegalArgumentException e) {
			
			e.printStackTrace();
		}
	}
	
	/**
	 * Closes the MongoDB instance
	 */
	public static void disconnectFromDatabase() {
		
		mongoClient.close();
	}

	/**
	 * Insert the given list of cards in the specified collection
	 * @param cards List of cards to be inserted
	 * @param collectionName Name of the collection that will receive the insertion
	 */
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

	/**
	 * Checks if there is an user on the database with the same name and password 
	 * @param name Name of the user
	 * @param password Password of the user
	 * @return True or False to confirm if the user exists
	 */
	public static boolean userExists(String name, String password) {
		
		connectToDatabase();
		
		Bson queryUserPassword = and(
				eq("user", name), 
				eq("pass", password)
		);
				
		MongoCollection<Document> users = database.getCollection(Config.getCollections().getString("users"));
		
		MongoCursor<Document> usersCursor = users.find(queryUserPassword).iterator();
		
		disconnectFromDatabase();
		
		return usersCursor.hasNext() ? true : false;
	}

	/**
	 * Inserts a user in the database with the provided name and password
	 * @param name Name of the user
	 * @param password Password of the user
	 */
	public static void setUpUser(String name, String password) {
		
		connectToDatabase();
		
		MongoCollection<Document> users = database.getCollection(Config.getCollections().getString("users"));
		
		Document doc = new Document();
		doc.append("user", name);
		doc.append("pass", password);
		
		users.insertOne(doc);
		
		disconnectFromDatabase();
	}

	
	/**
	 * From the provided collection get and return a list of the cards that it contains
	 * @param collectionName Name of the collection that contains the cards
	 * @return List of cards obtained from the collection
	 */
	public static List<Card> getCardsFromCollection(String collectionName) {
		
		List<Card> cardList = new ArrayList<Card>();
		
		connectToDatabase();
		
		MongoCollection<Document> collection = database.getCollection(collectionName);
		
		MongoCursor<Document> cardCursor = collection.find().iterator();
		
		while (cardCursor.hasNext()) {
			
			JSONObject cardJson = new JSONObject(cardCursor.next().toJson());
			
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
	
	/**
	 * From the specified card, return it's Base64 contained in the collection
	 * @param card Card to obtain it's Base64 from
	 * @param collectionName Name of the collection that contains the cards
	 * @return Base64 of the provided card
	 */
	public static String getBase64FromCard(Card card, String collectionName) {
		
		String cardBase64 = "";
		
		connectToDatabase();
		
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
	
	/**
	 * Insert the score of the User in the database
	 * @param userName Name of the User
	 * @param suit Suit that the user player with
	 * @param points Points that the User obtained
	 * @param collectionName Name of the collection that contains the score
	 */
	public static void insertScore(String userName, String suit, Integer points, String collectionName) {
		
		connectToDatabase();
		
		MongoCollection<Document> collection = database.getCollection(collectionName);
		
		Document doc = new Document();
		doc.append("user", userName);
		doc.append("suit", suit);
		doc.append("points", points);
		doc.append("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
		
		collection.insertOne(doc);
		
		disconnectFromDatabase();
	}
	
	/**
	 * Return the contents of the score collection as a list of strings and with a specific format
	 * @param collectionName Name of the collection that contains the score
	 * @return List of strings that contains formatted text related to the content of the score collection
	 */
	public static List<String> getScoresFromCollection(String collectionName) {
		
		String textTemplate = "%s %o points (Suit %S, %s)";
		
		List<String> scoreList = new ArrayList<String>();
		
		connectToDatabase();
		
		MongoCollection<Document> collection = database.getCollection(collectionName);
		
		MongoCursor<Document> cardCursor = collection.find().sort(descending("points")).iterator();
		
		while (cardCursor.hasNext()) {
			
			JSONObject cardJson = new JSONObject(cardCursor.next().toJson());
			
			scoreList.add(
					String.format(
							textTemplate, 
							cardJson.getString("user"), 
							cardJson.getInt("points"),
							cardJson.getString("suit"), 
							cardJson.getString("timestamp")
					)
			);
		}
		
		disconnectFromDatabase();
		
		return scoreList;
	}
}
