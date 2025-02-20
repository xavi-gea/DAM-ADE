package model;

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
			// either this
			//mongoClient = new MongoClient("localhost", 27017);
			mongoClient = new MongoClient(Config.getIp(), Integer.parseInt(Config.getPort()));
			
			// or this
			//mongoClient = new MongoClient(connectionString);
			
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
	 * Insert the given film in the specified collection
	 * @param film to be inserted
	 * @param collectionName Name of the collection that will receive the insertion
	 */
	public static void insertFilmToCollection(Film film, String collectionName) {
		
		connectToDatabase();
		
		//database.getCollection(collectionName).drop();
		
		database.createCollection(collectionName);
		
		MongoCollection<Document> collection = database.getCollection(collectionName);
			
		Document doc = new Document();
		
		long numberOfFilms = database.getCollection(collectionName).countDocuments();
		
		if (numberOfFilms > 0) {
			
			doc.append("identifier", numberOfFilms + 1);
			
		}else {
			
			doc.append("identifier", 1);
		}
		
		doc.append("titulo", film.getTitulo());
		doc.append("director", film.getDirector());
		doc.append("genero", film.getGenero());
		doc.append("base64", film.getBase64());
		
		collection.insertOne(doc);
		
		disconnectFromDatabase();
	}

	/**
	 * Insert the given list of cards in the specified collection
	 * @param cards List of cards to be inserted
	 * @param collectionName Name of the collection that will receive the insertion
	 */
	public static void insertFilmsToCollection(List<Film> films, String collectionName) {
		
		connectToDatabase();
		
		database.getCollection(collectionName).drop();
		
		database.createCollection(collectionName);
		
		MongoCollection<Document> collection = database.getCollection(collectionName);
		
		List<Document> cardDocuments = new ArrayList<Document>();
		
		for (Film film : films) {
			
			Document doc = new Document();
			
			long numberOfFilms = database.getCollection(collectionName).countDocuments();
			
			if (numberOfFilms > 0) {
				
				doc.append("identifier", numberOfFilms + 1);
				
			}else {
				
				doc.append("identifier", 1);
			}
			
			doc.append("titulo", film.getTitulo());
			doc.append("director", film.getDirector());
			doc.append("genero", film.getGenero());
			doc.append("base64", film.getBase64());
			
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
	 * From the provided collection get and return a film that it contains
	 * @param collectionName Name of the collection that contains the film
	 * @return film obtained from the collection
	 */
	public static Film getFilmFromCollection(Integer filmID, String collectionName) {
		
		Film film = null;
		
		connectToDatabase();
		
		Bson queryFilmID = eq("identifier", filmID);
				
		MongoCollection<Document> collection = database.getCollection(collectionName);
		
		MongoCursor<Document> filmCursor = collection.find(queryFilmID).iterator();
		
		if (filmCursor.hasNext()) {
			
			JSONObject filmJson = new JSONObject(filmCursor.next().toJson());
			
			film = new Film(
				filmJson.getInt("identifier"),
				filmJson.getString("titulo"),
				filmJson.getString("director"),
				filmJson.getString("genero"),
				filmJson.getString("base64")
			);
		}
		
		disconnectFromDatabase();
		
		return film;		
	}
	
	/**
	 * From the provided collection get and return a list of the cards that it contains
	 * @param collectionName Name of the collection that contains the cards
	 * @return List of cards obtained from the collection
	 */
	public static List<Film> getFilmsFromCollection(String collectionName) {
		
		List<Film> filmList = new ArrayList<Film>();
		
		connectToDatabase();
		
		MongoCollection<Document> collection = database.getCollection(collectionName);
		
		MongoCursor<Document> filmCursor = collection.find().iterator();
		
		while (filmCursor.hasNext()) {
			
			JSONObject filmJson = new JSONObject(filmCursor.next().toJson());
			
			filmList.add(
					new Film(
							filmJson.getInt("identifier"),
							filmJson.getString("titulo"),
							filmJson.getString("director"),
							filmJson.getString("genero"),
							filmJson.getString("base64")
					)
			);
		}
		
		disconnectFromDatabase();
		
		return filmList;		
	}
	
	public static boolean updateFilmFromCollection(Film film, String collectionName) {

		boolean documentUpdated = false;
		
		connectToDatabase();
		
		//database.getCollection(collectionName).drop();
		
		database.createCollection(collectionName);
		
		Bson queryFilmID = eq("identifier", film.getIdentifier());
		
		MongoCollection<Document> collection = database.getCollection(collectionName);
			
		Document doc = new Document();
		
		doc.append("identifier", film.getIdentifier());
		doc.append("titulo", film.getTitulo());
		doc.append("director", film.getDirector());
		doc.append("genero", film.getGenero());
		doc.append("base64", film.getBase64());
		
		documentUpdated = (
				collection.updateOne(
						queryFilmID, 
						new Document(
								"$set",
								doc
						)
				).getModifiedCount()) > 0;
		
		disconnectFromDatabase();
		
		return documentUpdated;
	}

	/**
	 * With the provided film name remove the corresponding film from the colletion
	 * @param filmName Name of the film
	 * @param collectionName Name of the collection that contains the film
	 * @return If the film has been deleted
	 */
	public static boolean removeFilmFromCollection(String filmName, String collectionName) {
		
		boolean documentDeleted = false;
		
		connectToDatabase();
		
		Bson queryFilm = eq("titulo", filmName);
				
		MongoCollection<Document> films = database.getCollection(collectionName);
		
		documentDeleted = (films.deleteOne(queryFilm).getDeletedCount()) > 0;
		
		disconnectFromDatabase();
		
		return documentDeleted;
	}
	
	/**
	 * From the specified card, return it's Base64 contained in the collection
	 * @param card Card to obtain it's Base64 from
	 * @param collectionName Name of the collection that contains the cards
	 * @return Base64 of the provided card
	 */
	public static String getBase64FromFilm(Film film, String collectionName) {
		
		String filmBase64 = "";
		
		connectToDatabase();
		
		Bson queryFilmID = eq("identifier", film.getIdentifier());
		
		MongoCollection<Document> films = database.getCollection(collectionName);
		
		MongoCursor<Document> filmCursor = films.find(queryFilmID).iterator();
		
		if (filmCursor.hasNext()) {
			
			JSONObject filmJson = new JSONObject(filmCursor.next().toJson());
			
			filmBase64 = filmJson.getString("base64");
		}
		
		disconnectFromDatabase();
		
		return filmBase64;
	}
}
