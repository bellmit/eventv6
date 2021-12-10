package cn.ffcs.shequ.utils;

import javax.crypto.Cipher;
import java.security.Key;


//csk
public class DesUtil {

    /** 对称加解密DES密钥Key*/
    public final static String KEY = "27d963fb";

    private static Cipher mEncryptCipher = null;
    private static Cipher mDecryptCipher = null;


    public DesUtil() throws Exception {
        //初始化加密和解密密码提供类
        mEncryptCipher = Cipher.getInstance("DES");
        mEncryptCipher.init(Cipher.ENCRYPT_MODE,getKey(KEY.getBytes()));
        mDecryptCipher = Cipher.getInstance("DES");
        mDecryptCipher.init(Cipher.DECRYPT_MODE,getKey(KEY.getBytes()));
    }

//   ****** 加密 ******

    /**
     * 对 字符串 加密
     * */
    public String encrypt(String strIn) throws Exception {
        return byte2HexStr(encrypt(strIn.getBytes()));
    }

    /**
     * 对 字节数组 加密
     */
    public byte[] encrypt(byte[] arrB) throws Exception {
        return mEncryptCipher.doFinal(arrB);
    }


//   ****** 解密 ******

    /**
     * 解密 字符串
     * */
    public String decrypt(String strIn) throws Exception {
        return new String(decrypt(hexStr2Byte(strIn)));
    }

    /**
     * 解密 字节数组
     */
    public byte[] decrypt(byte[] arrB) throws Exception {
        return mDecryptCipher.doFinal(arrB);
    }


    /**
     * 解密用的密钥（字节数组）长度必须为8个字节否则返回null, 不足8位时后面补0，超出8位只取前8位
     *
     * @param arrBTmp 构成该字符串的字节数组
     * @return 生成的密钥
     * @throws Exception
     */

    private Key getKey(byte[] arrBTmp) throws Exception {
        // 创建一个空的8位字节数组（默认值为0）
        byte[] arrB = new byte[8];

        // 将原始字节数组转换为8位
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }

        // 生成密钥
        Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");

        return key;
    }

    /**
     * HEX转码 String to Byte
     */
    public static byte[] hexStr2Byte(String strIn) throws Exception {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;

        // 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }

    /**
     * HEX转码 Byte to  String
     */
    public static String byte2HexStr(byte[] arrB) throws Exception {
        int iLen = arrB.length;
        // 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = arrB[i];
            // 把负数转换为正数
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            // 小于0F的数需要在前面补0
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }

}
