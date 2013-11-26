package eit.nl.utwente.sdm.test;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import eit.nl.utwente.sdm.encryptsql.EncryptionHelper;

public class TestEncDec {

	@Test
	public void testEncDec() {
		Random rn = new Random();
		byte key[] = new byte[32];
		for (int i = 0; i < 32; i++) {
			key[i] = (byte)(rn.nextDouble() * 32);
		}
		String encryptedB64 = EncryptionHelper.encrypt("Abracadabra", key);
		String decrypt = EncryptionHelper.decrypt(encryptedB64, key);
		Assert.assertTrue(decrypt.equals("Abracadabra"));
	}
	
}
