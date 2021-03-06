package com.kolon.sign2.utils;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public final class CipherUtils {

	private final static String TRANSFORMATION = "AES";
	private final static String HEX  = "0123456789ABCDEF";
	public static byte[] ivBytes = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

	public static String encrypt(String seed, String cleartext)
			throws Exception {
		byte[] rawKey = getRawKey(seed.getBytes("UTF-8"));
		byte[] result = encrypt(rawKey, cleartext.getBytes("UTF-8"));
		
		return toHex(result);
	}

	public static String decrypt(String seed, String encrypted)
			throws Exception {
		byte[] rawKey = getRawKey(seed.getBytes("UTF-8"));
		byte[] enc = toByte(encrypted);
		byte[] result = decrypt(rawKey, enc);
		
		return new String(result);
	}

//	private static byte[] getRawKey(byte[] seed) throws Exception {
//		KeyGenerator kgen = KeyGenerator.getInstance(TRANSFORMATION);
//		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
//		sr.setSeed(seed);
//		kgen.init(256, sr); // 192 and 256 bits may not be available
//		SecretKey skey = kgen.generateKey();
//		byte[] raw = skey.getEncoded();
//		return raw;
//	}

	private static byte[] getRawKey(byte[] seed) throws Exception {
		byte[] digest = MessageDigest.getInstance("MD5").digest(seed);
		
		byte[] rawKey = new byte[16];
		for(int i = 0; i < digest.length; i++) {
			rawKey[i] = digest[i];
		}
		
		return rawKey;
	}

	private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, TRANSFORMATION);
		Cipher cipher = Cipher.getInstance(TRANSFORMATION);
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(clear);
		
		return encrypted;
	}

	private static byte[] decrypt(byte[] raw, byte[] encrypted)
			throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, TRANSFORMATION);
		Cipher cipher = Cipher.getInstance(TRANSFORMATION);
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] decrypted = cipher.doFinal(encrypted);
		
		return decrypted;
	}

	public static String toHex(String txt) throws Exception {
		return toHex(txt.getBytes("UTF-8"));
	}

	public static String fromHex(String hex) throws Exception {
		return new String(toByte(hex));
	}

	public static byte[] toByte(String hexString) {
		int len = hexString.length() / 2;
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++) {
			result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
		}

		return result;
	}

	public static String toHex(byte[] buf) {
		if (buf == null) {
			return "";
		}
		StringBuffer result = new StringBuffer(2 * buf.length);
		for (int i = 0; i < buf.length; i++) {
			appendHex(result, buf[i]);
		}
		return result.toString();
	}

	private static void appendHex(StringBuffer sb, byte b) {
		sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
	}
}