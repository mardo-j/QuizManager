package fr.epita.quiz.datamodel;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import fr.epita.logger.Logger;


public class Encryption {
	
	private Encryption() {
		
	}
	public static String md5(String input) {
		try {
			
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(input.getBytes());
			BigInteger number = new BigInteger(1, messageDigest);
			StringBuilder hashtext = new StringBuilder();
			hashtext.append(number.toString(16));
			while(hashtext.length()<32) {
				hashtext.append("0").append(hashtext);
			}
			return hashtext.toString();
		}catch(NoSuchAlgorithmException e) {
			Logger.logMessage("Error encrypting password");
		}
		return "";
	}
}
