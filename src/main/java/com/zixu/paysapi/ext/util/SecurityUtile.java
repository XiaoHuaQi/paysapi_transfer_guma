/**   
 *
 * @Title: SecurityUtile.java 
 * @Package com.zixu.paysapi.ext.util 
 * @Description:  
 * @author lqc   
 * @version V1.0   
 *
 */
package com.zixu.paysapi.ext.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/** 
 *
 * @ClassName: SecurityUtile 
 * @Description: TODO
 * @author lqc 
 * @date 2016年9月7日 下午2:16:45 
 *
 */
public class SecurityUtile {
	protected static Log logger = LogFactory.getLog(SecurityUtile.class);
	private static byte[] keys = { 1, -1, 1, -1, 1, -1, 1, -1 };

	private static String key = "helpbaseinfoutil";

	public static String getKey() {
		return key;
	}

	public static void setKey(String key) {
		SecurityUtile.key = key;
	}

	/**
	 * 
	 * <p>
	 * 对password进行MD5加密
	 * @param source
	 * @return 
	 * @return byte[]    
	 * author: Heweipo
	 */
	public static byte[] getMD5(byte[] source) {
		byte tmp[] = null;
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			md.update(source);
			tmp = md.digest();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tmp;
	}

	/**
	 * 
	 * <p>
	 * 采用JDK内置类进行真正的加密操作
	 * @param byteS
	 * @param password
	 * @return 
	 * @return byte[]    
	 * author: Heweipo
	 */
	private static byte[] encryptByte(byte[] byteS, byte password[]) {
		byte[] byteFina = null;
		try {// 初始化加密/解密工具
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			DESKeySpec desKeySpec = new DESKeySpec(password);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
			IvParameterSpec iv = new IvParameterSpec(keys);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
			byteFina = cipher.doFinal(byteS);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return byteFina;
	}

	/**
	 * 
	 * <p>
	 * 采用JDK对应的内置类进行解密操作
	 * @param byteS
	 * @param password
	 * @return 
	 * @return byte[]    
	 * author: Heweipo
	 */
	private static byte[] decryptByte(byte[] byteS, byte password[]) {
		byte[] byteFina = null;
		try {// 初始化加密/解密工具
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			DESKeySpec desKeySpec = new DESKeySpec(password);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
			IvParameterSpec iv = new IvParameterSpec(keys);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
			byteFina = cipher.doFinal(byteS);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return byteFina;
	}

	/**
	 * 
	 * <p>
	 * Des加密strMing，然后base64转换
	 * @param strMing
	 * @param md5key
	 * @return 
	 * @return String    
	 * author: Heweipo
	 */
	public static String encryptStr(String strMing,String key) {
		key = StringUtil.isNull(key)?getKey():key;
		byte[] byteMi = null;
		byte[] byteMing = null;
		String strMi = "";
		try {
			byteMing = strMing.getBytes("utf-8");
			byteMi = encryptByte(byteMing, getMD5(key.getBytes("utf-8")));
			Base64 base64Encoder = new Base64();
			strMi = base64Encoder.encodeToString(byteMi);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			byteMing = null;
			byteMi = null;
		}
		return strMi;
	}

	/**
	 * 
	 * <p>
	 * Base64转换strMi,然后进行des解密
	 * @param strMi
	 * @param md5key
	 * @return 
	 * @return String    
	 * author: Heweipo
	 */
	public static String decryptStr(String strMi,String key) {
		key = StringUtil.isNull(key)?getKey():key;
		byte[] byteMing = null;
		String strMing = "";
		try {
			Base64 decoder = new Base64();
			byteMing =decoder.decode(strMi);
			byteMing = decryptByte(byteMing, getMD5(key.getBytes("utf-8")));
			strMing = new String(byteMing);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			byteMing = null;
		}
		return strMing;
	}
}
