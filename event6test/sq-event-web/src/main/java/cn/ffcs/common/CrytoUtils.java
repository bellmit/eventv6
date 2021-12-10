package cn.ffcs.common;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 *<p>
 * Copyright: Copyright (c) 2014-1-16
 *</p>
 *<p>
 * Company: Ffcs. Co.,Ltd
 *</p>
 * @ClassName: CrytoUtils.java
 * @Description: TODO功能描述 
 * @author: Linguisen
 * @version: V1.0 
 * @Date: 2014-1-16 下午5:57:05 
 */
public class CrytoUtils {
	private static final String TRANSFORMATION = "DESede/ECB/PKCS5Padding";

	private final static String CHARSET = "UTF-8";

	private final static String SEPARATOR = "$";
	
	public final static String MD5KEY = "75BD2E98AC17564B2DB7C74B064F5084C6557FDDF3E4C286";
	
	public final static String DESKEY = "b5eefe0437d945b98e82f46fbff8d3552c2ff6f7f8acd8de";

	private CrytoUtils() {
	}

	private static SecretKey initKey(String key) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		byte[] keyBytes = new byte[24];
		byte[] temp = key.getBytes(CHARSET);
		if (keyBytes.length > temp.length) {
			System.arraycopy(temp, 0, keyBytes, 0, temp.length);
		} else {
			System.arraycopy(temp, 0, keyBytes, 0, keyBytes.length);
		}
		return new SecretKeySpec(keyBytes, "DESede");
	}

	private static String encodeBy3DES(String key, String input) throws Exception {
		Cipher cipher = Cipher.getInstance(TRANSFORMATION);
		cipher.init(Cipher.ENCRYPT_MODE, initKey(key));
		byte[] encBytes = cipher.doFinal(input.getBytes(CHARSET));
		byte[] encBase64Bytes = new Base64().encode(encBytes);
		return new String(encBase64Bytes, CHARSET);
	}

	private static String decodeBy3DES(String key, String input) throws Exception {
		Cipher cipher = Cipher.getInstance(TRANSFORMATION);
		cipher.init(Cipher.DECRYPT_MODE, initKey(key));
		byte[] desBase64Bytes = new Base64().decode(input);
		byte[] decBytes = cipher.doFinal(desBase64Bytes);
		return new String(decBytes, CHARSET);
	}
	
	private static String joinStrings(String... array) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < array.length; i++) {
			sb.append(array[i]);
			if (i < array.length - 1) {
				sb.append(SEPARATOR);
			}
		}
		return sb.toString();
	}
	
	private static String[] splitString(String input){
		return input.split(Pattern.quote(SEPARATOR));
	}

	/**
	 * 加密.
	 * 
	 * @param key
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public static String encode(String key,String... args) throws Exception {
		String encString = encodeBy3DES(key, joinStrings(args));
		return URLEncoder.encode(encString, CHARSET);
	}

	/**
	 * 解密.
	 * 
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public static String[] decode(String key,String input) throws Exception {
		String raw = decodeBy3DES(key, URLDecoder.decode(input, CHARSET));
		return splitString(raw);
	}
	
	/**
	 * md5加密.
	 * 
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public static String md5(String... args) throws Exception{
		String tmp=joinStrings(args);
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		messageDigest.reset();
		messageDigest.update(tmp.getBytes(CHARSET));
		byte[] byteArray = messageDigest.digest();
		StringBuffer md5StrBuff = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}
		return md5StrBuff.toString();
		
	}
	
	
    public static void main(String[] args) throws Exception {
		
		//加密
		String telephone = "Global_Eyes";
		String md5Key = "75BD2E98AC17564B2DB7C74B064F5084C6557FDDF3E4C286";
		String timestamp = "2017-01-20 14:30:00";
		String params = telephone;
		String md5 = CrytoUtils.md5(params,md5Key,timestamp);
		System.out.println("md5加密后="+md5);
		String desKey = "b5eefe0437d945b98e82f46fbff8d3552c2ff6f7f8acd8de";
		String sign = CrytoUtils.encode(desKey,timestamp, md5);
		System.out.println("3des加密后的sign="+sign);
		
		//解密
//		String[] pa = CrytoUtils.decode(desKey,sign);
//		System.out.println("解密后的timestamp:"+pa[0]);
//		System.out.println("解密后的md5:"+pa[1]);
		
	}

}
