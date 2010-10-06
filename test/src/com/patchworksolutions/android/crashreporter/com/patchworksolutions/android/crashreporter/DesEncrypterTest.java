package com.patchworksolutions.android.crashreporter;

import javax.crypto.SecretKey;

import com.patchworksolutions.android.crashreporter.DesEncrypter;

import junit.framework.TestCase;

public class DesEncrypterTest extends TestCase {

    @Override
    public void setUp() throws Exception {
      super.setUp();
    }
    
    public void testBuildSecretKey() throws Exception {
    	byte[] testData = { (byte)0x01, (byte)0x02, (byte)0x03, (byte)0x04, (byte)0x05, (byte)0x06, (byte)0x07, (byte)0x08 };
        SecretKey testKey = DesEncrypter.buildSecretKey(testData);
        byte[] compareData = testKey.getEncoded();
        for(int i = 0; i < testData.length; i++) {
        	assertTrue(testData[i] == compareData[i]);
        }
    }
    
    public void testEncryptDecrypt() throws Exception {
    	byte[] testData = { (byte)0x01, (byte)0x02, (byte)0x03, (byte)0x04, (byte)0x05, (byte)0x06, (byte)0x07, (byte)0x08 };
        SecretKey testKey = DesEncrypter.buildSecretKey(testData);
        DesEncrypter testEncrypter = new DesEncrypter(testKey);
        String secretString = "Hello, secret world!";
        String encryptedString = testEncrypter.encrypt(secretString);
        String decryptedString = testEncrypter.decrypt(encryptedString);
        assertTrue(decryptedString.compareTo(secretString) == 0);
    }
}