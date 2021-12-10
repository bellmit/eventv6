package cn.ffcs.zhsq.bus.util;

import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Locale;

public class ByteUtils {

	/**
	 * 获取socket校验码
	 * @method 校验方法: 校验码是指从协议类型开始，同后一字节异或，直到校验码前一个字节，占用一个字节
	 * @param byteArr
	 * @return
	 */
	public static byte getSocketJudgeCode(byte[] byteArr){
		//byte[] byteArr = {1, 35,  0, 3,  0, -103, 51, -119, 0, -103, 51, -119, 1};
		byteArr = getNeededJudgeCodeArr(byteArr);
		byte judgeCode = 0;
		for(int i=0,len=byteArr.length;i<len;i++){
			if(i != 0){
				judgeCode = (byte)(judgeCode^byteArr[i]);//异或上一个值
			}else if(i==0){
				judgeCode = byteArr[i];
			}
		}
		return judgeCode;
	}
	private static byte[] getNeededJudgeCodeArr(byte[] byteArr){
		if(byteArr[0] == 91){//如果初始值为[,则去除该字节
			return Arrays.copyOfRange(byteArr, 1,byteArr.length);
		}
		return byteArr;
	}
	
	/**
	 * 合并两个字节数组
	 * @param first
	 * @param second
	 * @return
	 */
	public static byte[] concatByte(byte[] first, byte[] second) {
		byte[] result = Arrays.copyOf(first, first.length + second.length);//扩展长度
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}
	
	/**
	 * byte转16进制字符串,中间使用" "隔开
	 * @param src
	 * @return
	 */
	public static String bytes2HexString(byte[] src){
	    StringBuilder stringBuilder = new StringBuilder("");
	    if (src == null || src.length <= 0) {
	        return null;
	    }
	    for (int i = 0; i < src.length; i++) {
	        int v = src[i] & 0xFF;
	        String hv = Integer.toHexString(v);
	        if (hv.length() < 2) {
	            stringBuilder.append(0);
	        }
	        stringBuilder.append(hv);
	        stringBuilder.append(" ");
	    }
	    String result = stringBuilder.toString();
	    result = result.trim();
	    return result;
	}
	
	/**
	 * 单个byte转16进制值
	 * @param bi
	 * @return
	 */
	public static String byte2HexString(byte bi){
		int v = bi & 0xFF;
		String hv = Integer.toHexString(v);
		if (hv.length() < 2){
			hv = "0"+hv;
		}
		return hv;
	}
	
	/**
	 * 合并2个数组
	 * @param first
	 * @param second
	 * @return
	 */
	public static <T> T[] concat(T[] first, T[] second) {
		T[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}
	
	/**  
	  * 将int转为低字节在前，高字节在后的byte数组  
	  * @param n int  
	  * @return byte[]  
	  */  
	public static byte[] toLH(int n) {
	  byte[] b = new byte[4];
	  b[0] = (byte) (n & 0xff);
	  b[1] = (byte) (n >> 8 & 0xff);
	  b[2] = (byte) (n >> 16 & 0xff);
	  b[3] = (byte) (n >> 24 & 0xff);
	  return b;
	}
	  
	/**  
	  * 将int转为高字节在前，低字节在后的byte数组  (Big-Endian)
	  * @param n int  
	  * @return byte[]  
	  */  
	public static byte[] toHH(int n) {
	  byte[] b = new byte[4];
	  b[3] = (byte) (n & 0xff);
	  b[2] = (byte) (n >> 8 & 0xff);
	  b[1] = (byte) (n >> 16 & 0xff);
	  b[0] = (byte) (n >> 24 & 0xff);
	  return b;
	}
	  
	/**  
	  * 将short转为低字节在前，高字节在后的byte数组  
	  * @param n short  
	  * @return byte[]  
	  */  
	public static byte[] toLH(short n) {
	  byte[] b = new byte[2];
	  b[0] = (byte) (n & 0xff);
	  b[1] = (byte) (n >> 8 & 0xff);
	  return b;
	}
	  
	/**  
	  * 将short转为高字节在前，低字节在后的byte数组    (Big-Endian)
	  * @param n short  
	  * @return byte[]  
	  */  
	public static byte[] toHH(short n) {
	  byte[] b = new byte[2];
	  b[1] = (byte) (n & 0xff);
	  b[0] = (byte) (n >> 8 & 0xff);
	  return b;
	}
	
	public static byte[] longToBytes(long n) {
        byte[] b = new byte[8];
        b[7] = (byte) (n & 0xff);
        b[6] = (byte) (n >> 8  & 0xff);
        b[5] = (byte) (n >> 16 & 0xff);
        b[4] = (byte) (n >> 24 & 0xff);
        b[3] = (byte) (n >> 32 & 0xff);
        b[2] = (byte) (n >> 40 & 0xff);
        b[1] = (byte) (n >> 48 & 0xff);
        b[0] = (byte) (n >> 56 & 0xff);
        return b;
    }
    
	/**
	 * 字符串转16进制字符串
	 * @param sourceStr
	 * @return
	 */
	public static String md5ToHex(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
                //buf.append(i).append("");
            }
            result = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        return result;
    }
	
	/**
	 * 判断当前系统是否是大端模式(BIG_ENDIAN)
	 * @return
	 */
	public static boolean isBigEndian(){
		return ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN;
	}
	
	/**
	 * 网络字节序: byte数组转int
	 * @param b
	 * @return
	 */
	public static int bytes2Int(byte[] b) {
        return    b[3] & 0xff 
               | (b[2] & 0xff) << 8 
               | (b[1] & 0xff) << 16
               | (b[0] & 0xff) << 24;
    }

	/**
	 * 网络字节序: byte数组转int
	 * @param b
	 * @param offset 偏移量
	 * @return
	 */
    public static int bytes2Int(byte[] b, int offset) {
        return    b[offset+3] & 0xff 
               | (b[offset+2] & 0xff) << 8 
               | (b[offset+1] & 0xff) << 16
               | (b[offset] & 0xff) << 24;
    }
    
    /**
	 * 网络字节序: byte数组转无符号int
	 * @param b
	 * @param offset 偏移量
	 * @return
	 */
    public static long bytes2Uint(byte[] array, int offset) {   
        return ((long) (array[offset+3] & 0xff))  
              | ((long) (array[offset+2] & 0xff)) << 8  
             | ((long) (array[offset+1] & 0xff)) << 16  
             | ((long) (array[offset]   & 0xff)) << 24;  
    }
	
    /**
	 * 网络字节序: byte数组转short
	 * @param b
	 * @return
	 */
    public static short bytes2Short(byte[] b){
        return (short)( b[1] & 0xff
                      |(b[0] & 0xff) << 8 ); 
    }    

    /**
	 * 网络字节序: byte数组转short
	 * @param b
	 * @param offset 偏移量
	 * @return
	 */
    public static short bytes2Short(byte[] b, int offset){
        return (short)( b[offset+1] & 0xff
                      |(b[offset]    & 0xff) << 8 ); 
    }
    
    /**
	 * 网络字节序: byte数组转无符号short
	 * @param b
	 * @param offset 偏移量
	 * @return
	 */
    public static int bytes2Ushort(byte b[], int offset) {
        return    b[offset+1] & 0xff 
               | (b[offset]   & 0xff) << 8;
    }
    
    /** 
     * bytes字符串转换为Byte值 
     * @param src String Byte字符串，每个Byte之间没有分隔符(字符范围:0-9 A-F) 
     * @return byte[] 
     */  
    public static byte[] hexString2ByteArray(String src){
        /*对输入值进行规范化整理*/
        src = src.trim().replace(" ", "").toUpperCase(Locale.US);
        //处理值初始化  
        int m=0,n=0;
        int iLen=src.length()/2; //计算长度  
        byte[] ret = new byte[iLen]; //分配存储空间  
        for (int i = 0; i < iLen; i++){
            m=i*2+1;
            n=m+1;
            ret[i] = (byte)(Integer.decode("0x"+ src.substring(i*2, m) + src.substring(m,n)) & 0xFF);
        }
        return ret;
    }
    
    /** 
     * @功能: BCD码转为10进制串(阿拉伯数据) 
     * @参数: BCD码 
     * @结果: 10进制串 
     */  
    public static String bcd2Str(byte[] bytes) {  
        StringBuffer temp = new StringBuffer(bytes.length * 2);  
        for (int i = 0; i < bytes.length; i++) {  
            temp.append((byte) ((bytes[i] & 0xf0) >>> 4));  
            temp.append((byte) (bytes[i] & 0x0f));  
        }  
        return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp  
                .toString().substring(1) : temp.toString();  
    }
    
    public static String bcd2StrTime(byte[] bytes) {  
    	String temp = bcd2Str(bytes);
    	if(temp.length()!=12){
    		return null;
    	}
    	StringBuilder sbl = new StringBuilder();
    	sbl.append("20").append(temp.substring(0, 2)).append("-").append(temp.substring(2, 4)).append("-").append(temp.substring(4, 6));
    	sbl.append(" ").append(temp.substring(6,8)).append(":").append(temp.substring(8,10)).append(":").append(temp.substring(10,12));
    	return sbl.toString();
    }
    
}
