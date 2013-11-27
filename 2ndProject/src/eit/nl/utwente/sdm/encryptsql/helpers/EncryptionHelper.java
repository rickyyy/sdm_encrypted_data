package eit.nl.utwente.sdm.encryptsql.helpers;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class EncryptionHelper {

	 public static String encrypt(String strToEncrypt, byte key[]) {
	        try {
	            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
	            final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
	            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
	            final String encryptedString = Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes()));
	            return encryptedString;
	        } catch (Exception e) {
	        	return null;
	        }
	    }

	    public static String decrypt(String strToDecrypt, byte key[]) {
	        try {
	            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
	            final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
	            cipher.init(Cipher.DECRYPT_MODE, secretKey);
	            final String decryptedString = new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt)));
	            return decryptedString;
	        } catch (Exception e) {
	            e.printStackTrace();
	        	return null;
	        }
	    }


	
}
