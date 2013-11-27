package eit.nl.utwente.sdm.test;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import eit.nl.utwente.sdm.encryptsql.helpers.EncryptionHelper;

public class TestEncDec {

	@Test
	public void testEncDec() {
		Random rn = new Random();
		byte key[] = new byte[16];
		for (int i = 0; i < 16; i++) {
			key[i] = (byte)(rn.nextDouble() * 255);
		}
		String encryptedB64 = EncryptionHelper.encrypt("Abracadabra", key);
		String decrypt = EncryptionHelper.decrypt(encryptedB64, key);
		Assert.assertTrue(decrypt.equals("Abracadabra"));
	}
	
}
