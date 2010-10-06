package com.patchworksolutions.android.crashreporter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import net.iharder.Base64.Base64;

public class DesEncrypter {
	Cipher ecipher;
	Cipher dcipher;

	DesEncrypter(SecretKey key) {
		try {
			ecipher = Cipher.getInstance("DES");
			dcipher = Cipher.getInstance("DES");
			ecipher.init(Cipher.ENCRYPT_MODE, key);
			dcipher.init(Cipher.DECRYPT_MODE, key);
		} catch (javax.crypto.NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (java.security.NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (java.security.InvalidKeyException e) {
			e.printStackTrace();
		}
	}

	public String encrypt(String str) {
		try { // Encode the string into bytes using utf-8
			byte[] utf8 = str.getBytes("UTF-8"); // Encrypt
			byte[] enc = ecipher.doFinal(utf8);
			return Base64.encodeBytes(enc); // Encode bytes to base64 to get a
											// string
		} catch (javax.crypto.BadPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String decrypt(String str) {
		try { // Decode base64 to get bytes
			byte[] dec = Base64.decode(str);
			byte[] utf8 = dcipher.doFinal(dec); // Decode using utf-8
			return new String(utf8, "UTF8");
		} catch (javax.crypto.BadPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static SecretKey buildSecretKey(byte[] secretDesKeyData) {
		SecretKey secretKey = null;
		try {
			DESKeySpec desKeySpec = new DESKeySpec(secretDesKeyData);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			secretKey = keyFactory.generateSecret(desKeySpec);
		} catch (NoSuchAlgorithmException e) {
		} catch (InvalidKeyException e) {
		} catch (InvalidKeySpecException e) {
		}
		return secretKey;
	}
}