package xgf.model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {
	
	private String name;
	private String password;
	
	private String type;

	public User(String userName, char[] userPassword) throws NoSuchAlgorithmException {
		
		this.name = userName;
		this.password = getHash(userPassword);
		this.type = "";
	}
	
	public static String getHash(char[] password) throws NoSuchAlgorithmException {
		
		byte[] passwordInBytes = String.valueOf(password).getBytes(StandardCharsets.UTF_8);
		
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		
		byte[] hashBytes = messageDigest.digest(passwordInBytes);
		
		return getStringFromBytes(hashBytes);
	}

	private static String getStringFromBytes(byte[] hashBytes) {
		
		String result = "";
		
		// Format each byte as Hexadecimal integer with a minimum of 2 digits.
		// If needed to achieve 2 digits, precede it with 0
		
		for (byte b : hashBytes) {
			
			result += String.format("%02x", b);
		}
		
		return result;
	}
	

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
