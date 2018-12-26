package com.zixu.paysapi.ext.util;

import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

public class TokenUtils {
	private static final String LANG = "utf-8";
	private static char[] HEXCHAR = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String genLtpaToken(String user, String key, long expireHour) throws Exception {
		byte[] hbts = { 1, 2, 3, 4 };
		String createTime = Long.toHexString(System.currentTimeMillis() / 1000L).toUpperCase();
		byte[] cbts = createTime.getBytes(LANG);
		String endTime = Long.toHexString(System.currentTimeMillis() / 1000L + expireHour * 3600L).toUpperCase();
		byte[] ebts = endTime.getBytes(LANG);
		byte[] ubts = user.getBytes(LANG);
		byte[] sourceBts = new byte[20 + ubts.length];
		byte[] kbts = Base64.decodeBase64(key.getBytes(LANG));
		System.arraycopy(hbts, 0, sourceBts, 0, 4);
		System.arraycopy(cbts, 0, sourceBts, 4, 8);
		System.arraycopy(ebts, 0, sourceBts, 12, 8);
		System.arraycopy(ubts, 0, sourceBts, 20, ubts.length);
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		md.update(sourceBts);
		md.update(kbts);
		byte[] resultBts = new byte[sourceBts.length + 20];
		System.arraycopy(sourceBts, 0, resultBts, 0, sourceBts.length);
		System.arraycopy(md.digest(), 0, resultBts, sourceBts.length, 20);
		String token = new String(Base64.encodeBase64(resultBts));
		return token;
	}

	public static String parseLtpaToken(String tokenStr, String key) throws Exception {
		byte[] token = Base64.decodeBase64(tokenStr.getBytes());

		byte[] curDigest = new byte[20];
		System.arraycopy(token, token.length - 20, curDigest, 0, 20);
		byte[] source = new byte[token.length];
		System.arraycopy(token, 0, source, 0, source.length - 20);
		byte[] keyBts = Base64.decodeBase64(key.getBytes());
		System.arraycopy(keyBts, 0, source, source.length - 20, 20);

		MessageDigest md = MessageDigest.getInstance("SHA-1");
		byte[] dig = md.digest(source);
		if (!MessageDigest.isEqual(curDigest, dig)) {
			throw new Exception("被篡改的令牌");
		}
		byte[] endBts = new byte[8];
		System.arraycopy(token, 12, endBts, 0, 8);
		long endTime1 = Long.parseLong(new String(endBts), 16);

		long endTime2 = System.currentTimeMillis() / 1000L;
		if (endTime1 < endTime2) {
			throw new Exception("令牌已经过期");
		}
		byte[] userBts = new byte[token.length - 40];
		System.arraycopy(token, 20, userBts, 0, userBts.length);
		return new String(userBts, LANG);
	}

	public static String genLRToken(String user, String pubKey, String priKey, long expireHour) throws Exception {
		user = user.trim();
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		long time = System.currentTimeMillis();
		String tokenInfo = dateFormat.format(new Date(time));
		tokenInfo = tokenInfo + user;
		time += expireHour * 3600L * 1000L;
		tokenInfo = tokenInfo + dateFormat.format(new Date(time));
		tokenInfo = tokenInfo + UUID.randomUUID().toString().replaceAll("-", "");
		byte[] cipherText = tokenInfo.getBytes(LANG);
		return byte2HexString(encrypt(cipherText, pubKey, priKey));
	}

	public static String parseLRToken(String token, String pubKey, String priKey) throws Exception {
		byte[] cipherText = hexString2Bytes(token);
		String tokenInfo = new String(decrypt(cipherText, pubKey, priKey), LANG);
		tokenInfo = tokenInfo.substring(0, tokenInfo.length() - 32);
		String username = tokenInfo.substring(14, tokenInfo.length() - 14);
		String expireStr = tokenInfo.substring(tokenInfo.length() - 14);

		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		long time = System.currentTimeMillis();
		Date expireTime = dateFormat.parse(expireStr);
		if (expireTime.getTime() < time) {
			throw new Exception("LRToken过期");
		}
		return username;
	}

	private static byte[] encrypt(byte[] input, String pubKey, String priKey) throws Exception {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		KeySpec keySpec = new X509EncodedKeySpec(hexString2Bytes(pubKey));
		Key publicKey = keyFactory.generatePublic(keySpec);
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(1, publicKey);
		return cipher.doFinal(input);
	}

	private static byte[] decrypt(byte[] input, String pubKey, String priKey) throws Exception {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		KeySpec keySpec = new PKCS8EncodedKeySpec(hexString2Bytes(priKey));
		Key privateKey = keyFactory.generatePrivate(keySpec);
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(2, privateKey);
		return cipher.doFinal(input);
	}

	private static final byte[] hexString2Bytes(String s) {
		byte[] bytes = new byte[s.length() / 2];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = ((byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16));
		}
		return bytes;
	}

	private static String byte2HexString(byte[] b) {
		StringBuffer sb = new StringBuffer(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEXCHAR[((b[i] & 0xF0) >>> 4)]);
			sb.append(HEXCHAR[(b[i] & 0xF)]);
		}
		return sb.toString();
	} 

	public static void main(String[] argv) throws Exception {
//		System.out.println(genLtpaToken("kkcs_01", "KW1K0UeXCYaNORi8nAKz1tFOlx8=", 100000L));
		String str = "AAECAzU4Q0ZBMDE2NThEMDQ4RDZra2NzXzAxizHFDytDMXymbASRfMSBsrS+pns";
		String key = "KW1K0UeXCYaNORi8nAKz1tFOlx8=";
		try{
			String user = TokenUtils.parseLtpaToken(str, key);
			System.out.println(user);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}