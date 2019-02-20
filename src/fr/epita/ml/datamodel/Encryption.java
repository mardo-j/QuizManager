package fr.epita.ml.datamodel;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import fr.epita.logger.Logger;

/**
 * Encryption singleton class with private constructor
 * @author Mardo.Lucas
 *
 */

public class Encryption {
	/**
	 * private constructor
	 */
	private Encryption() {
		
	}
	/**
	 * MD5 method to encrypt passwords
	 * @param input String password
	 * @return hashText String password hashed
	 */
	public static String md5(String input) {
		try {
			
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(input.getBytes());
			BigInteger number = new BigInteger(1, messageDigest);
			StringBuilder hashText = new StringBuilder();
			hashText.append(number.toString(16));
			while(hashText.length()<32) {
				hashText.append("0").append(hashText);
			}
			return hashText.toString();
		}catch(NoSuchAlgorithmException e) {
			Logger.logMessage("Error encrypting password");
		}
		return "";
	}
}
