package cn.ffcs.shequ.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加密util
 * 
 * @Desctiption ehome Encrypt.java
 * @Copyright Copyright (c) 2012 FFCS All Rights Reserved
 * @Company 北京福富软件有限公司
 * @author 李德昌
 * @date 2012-5-17
 * @version 1.0
 * @history
 * @param
 */
public class EncryptUtil {

	static String DES = "DES/ECB/NoPadding";
	static String TriDes = "DESede/ECB/NoPadding";

	private Key key;

	/**
	 * 
	 * @param strSrc
	 * @param encName
	 *            MD5 SHA-1 SHA-256
	 * @return
	 */
	private static String encrypt(String strSrc, String encName) {
		return encrypt(strSrc.getBytes(), encName);
	}
	
	private static String encrypt(byte[] encryptSrc, String encName) {
		MessageDigest md = null;
		try {
			if (encName == null || encName.equals("")) {
				encName = "MD5";
			}
			md = MessageDigest.getInstance(encName);
			md.update(encryptSrc);
			return bytes2Hex(md.digest());
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Invalid algorithm.");
			return null;
		}
	}

	/**
	 * 
	 * @param strSrc
	 * @return
	 */
	public static String encryptMd5(String strSrc) {
		return encrypt(strSrc, "MD5").toString();
	}
	
	public static String encryptMd5(byte[] encryptSrc) {
		return encrypt(encryptSrc, "MD5").toString();
	}
	
	public static String encryptMd5Upcase(String strSrc) {
		return encrypt(strSrc, "MD5").toString().toUpperCase();
	}
	
	

	/**
	 * 
	 * @param strSrc
	 * @return
	 */
	public static String encryptSHA1(String strSrc) {
		return encrypt(strSrc, "SHA-1").toString();
	}

	/**
	 * 
	 * @param strSrc
	 * @return
	 */
	public static String encryptSHA256(String strSrc) {
		return encrypt(strSrc, "SHA-256").toString();
	}

	public static String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}
	
	/**
     * 十六进制字符串转二进制
     * @param str 十六进制串
     * @return
     */
    public static byte[] hex2byte(String str) { //字符串转二进制
        int len = str.length();
        String stmp = null;
        byte bt[] = new byte[len / 2];
        for (int n = 0; n < len / 2; n++) {
            stmp = str.substring(n * 2, n * 2 + 2);
            bt[n] = (byte) (java.lang.Integer.parseInt(stmp, 16));
        }
        return bt;
    }

	/**
	 * base64encode
	 * 
	 * @param source
	 * @return
	 */
	public static String encryptBase64(String source) {
		return new sun.misc.BASE64Encoder().encode(source.getBytes());
	}

	/**
	 * base64decode
	 * 
	 * @param encoded
	 * @return
	 */
	public static String decodeBase64(String encoded) {
		sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
		try {
			byte[] decodeBts = decoder.decodeBuffer(encoded);
			return new String(decodeBts,"UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * 加密String明文输入,String密文输出
	 * 
	 * @param strMing
	 * @return
	 */
	public static String encryptDes(String source, String key) {
		String strMi = "";
		String algorithm = "DES";
		try {
			return bytes2Hex(encryptDesToByte(source.getBytes(), key,algorithm));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return strMi;
	}
	
	/**
	 * 3Des 加密
	 * @param source
	 * @param key
	 * @return
	 */
	public static String encrypt3Des(String source, String key) {
		String strMi = "";
		String algorithm = "DESede";
		try {
			return bytes2Hex(encryptDesToByte(source.getBytes(), key,algorithm));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return strMi;
	}
	

	/**
	 * 解密 以String密文输入,String明文输出
	 * 
	 * @param strMi
	 * @return
	 */
	public static String decodeDes(String encoded, String key) {
		String strMing = "";
		String algorithm = "DES";
		try {
			return new String(
					decodeDesToByte(hex2byte(encoded.getBytes()), key,algorithm),"utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return strMing;
	}

	
	/**
	 * 3des 解密
	 * @param encoded
	 * @param key
	 * @return
	 */
	public static String decode3Des(String encoded, String key) {
		String strMing = "";
		String algorithm = "DESede";
		try {
			return new String(
					decodeDesToByte(hex2byte(encoded.getBytes()), key,algorithm),"utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return strMing;
	}
	

	/**
	 * 加密以byte[]明文输入,byte[]密文输出
	 * 
	 * @param byteS
	 * @return
	 */
	private static byte[] encryptDesToByte(byte[] byteS, String key,String algorithm) {
		byte[] byteFina = null;
		Cipher cipher;
		try {
			cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.ENCRYPT_MODE, generateKey(key,algorithm));
			byteFina = cipher.doFinal(byteS);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cipher = null;
		}
		return byteFina;
	}

	/**
	 * 
	 * @param key
	 * @param algorithm 算法 DES,DESede,Blowfish
	 * @return
	 */
	private static Key generateKey(String key,String algorithm) {
		KeyGenerator _generator = null;
		try {
			_generator = KeyGenerator.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		_generator.init(new SecureRandom(key.getBytes()));
		return _generator.generateKey();
	}

	/**
	 * 解密以byte[]密文输入,以byte[]明文输出
	 * 
	 * @param byteD
	 * @return
	 */
	private static byte[] decodeDesToByte(byte[] byteD, String key,String algorithm) {
		Cipher cipher;
		byte[] byteFina = null;
		try {
			cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.DECRYPT_MODE, generateKey(key,algorithm));
			byteFina = cipher.doFinal(byteD);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cipher = null;
		}
		return byteFina;
	}

	public static byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0)
			throw new IllegalArgumentException("长度不是偶数");
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			// 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}

		return b2;
	}
	
	/**
     * 二进制转十六进制字符串
     * @param b
     * @return
     */
    public static String byte2hex(byte[] b) { //二行制转字符串
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
            if (n < b.length - 1) {
                hs = hs + "";
            }
        }
        return hs.toUpperCase();
    }

    public static byte[] encryptHMAC(String data, String secret) throws IOException {
		byte[] bytes = null;
		try {
			SecretKey secretKey = new SecretKeySpec(secret.getBytes("UTF-8"), "HmacMD5");
			Mac mac = Mac.getInstance(secretKey.getAlgorithm());
			mac.init(secretKey);
			bytes = mac.doFinal(data.getBytes("UTF-8"));
		} catch (GeneralSecurityException gse) {
			String msg=getStringFromException(gse);
			throw new IOException(msg);
		}
		return bytes;
	}
	
	private static String getStringFromException(Throwable e){
		String result="";
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		PrintStream ps=new PrintStream(bos);
		e.printStackTrace(ps);
		try {
			result=bos.toString("utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return result;
	}
	
	public static byte[] encryptMd5ToByte(String data) throws IOException {
		byte[] bytes = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			bytes = md.digest(data.getBytes("UTF-8"));
		} catch (GeneralSecurityException gse) {
			String msg=getStringFromException(gse);
			throw new IOException(msg);
		}
		return bytes;
	}
	


	/**
	 * @param args
	 * @throws UnsupportedEncodingException 
	 */
	public static void main(String[] args) throws UnsupportedEncodingException {
		String temp="112233";
//		temp=EncryptUtil.encryptBase64(temp);
//		System.out.println(temp);
		    //111111accounttest1app_keytest_keyendTime2016-12-10 15:53:55formatjsonmethodzzQuerypasswordMTExMTEsign_methodmd5startTime2016-12-10 15:53:45timestamp2016-12-10 15:53:55v1.0111111
		temp="111111accounttest1app_keytest_keyendTime2016-12-10 15:53:55formatjsonmethodzzQuerypasswordMTExMTEsign_methodmd5startTime2016-12-10 15:53:45timestamp2016-12-10 15:53:55v1.0111111";
//		temp="111111accounttest1app_keytest_keyformatjsonmethodzzQuerypasswordMTExMTEsign_methodmd5timestamp2016-09-21 20:00:00v1.0111111";
		temp=EncryptUtil.encrypt(temp, "MD5");
		System.out.println(temp.toUpperCase());
	}

}