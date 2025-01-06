package xgf.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

public class Card {

	private String suit;
	private String points;
	private String base64;
	
	public Card(String suit, String points, String base64) {
		super();
		this.suit = suit;
		this.points = points;
		this.base64 = base64;
	}
	
	
	public static List<Card> getCardsfromFolder(File folder) {
		
		List<Card> cardList = new ArrayList<Card>();
		
		for (File element : folder.listFiles()) {
			
			if (!element.isDirectory()) {
				
				String fileName = removeExtension(element.getName());
				String[] splitFileName = fileName.split("_");
				String encodedString = "";
				
				try {
					
					encodedString = Base64.encodeBase64String(Files.readAllBytes(element.toPath()));
					
				} catch (IOException e) {

					e.printStackTrace();
				}
				
				cardList.add(new Card(splitFileName[0], splitFileName[1], encodedString));
			}
		}
		
		return cardList;		
	}
	
	
	
	private static String removeExtension(String fileName) {
		
		String extensionArchivo = "";
		
		int extensionPosition = fileName.lastIndexOf(".");
		
		if (extensionPosition != -1) {
		
			extensionArchivo = fileName.substring(extensionPosition);
		}
		
		return fileName.replace(extensionArchivo,"");
	}

	public String getSuit() {
		return suit;
	}

	public String getPoints() {
		return points;
	}

	public String getBase64() {
		return base64;
	}
}
