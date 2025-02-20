package model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Xavi
 */
public class User {

	private String name;
	private String password;

	public User(String userName, char[] userPassword) throws NoSuchAlgorithmException {
		
		this.name = userName;
		this.password = getHash(userPassword);
	}

	/**
	 * From the provided password, return a generated hash in SHA-256 algorithm
	 * @param password Password to be converted
	 * @return Hashed password
	 * @throws NoSuchAlgorithmException When the specified algorithm cannot be found
	 */
	public static String getHash(char[] password) throws NoSuchAlgorithmException {
		
		byte[] passwordInBytes = String.valueOf(password).getBytes(StandardCharsets.UTF_8);
		
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		
		byte[] hashBytes = messageDigest.digest(passwordInBytes);
		
		return getStringFromBytes(hashBytes);
	}

	/**
	 * Formats each byte from hashBytes as an Hexadecimal integer with a minimum of 2 digits.
	 * If needed to achieve 2 digits, precede it with 0
	 * @param hashBytes Array that contains the bytes to be formatted
	 * @return String with each byte formatted to it's Hexadecimal integer equivalent
	 */
	private static String getStringFromBytes(byte[] hashBytes) {
		
		String result = "";
		
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
}