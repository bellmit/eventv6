package cn.ffcs.zhsq.utils;

import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class AuxiliaryPolice {
    protected static MessageDigest messageDigest = null;

    public static String md5(String str) {
        if (str == null) {
            return null;
        }
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (Exception e) {
            return str;
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return md5StrBuff.toString().toUpperCase();
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        String name = "测试";
        String json = "{\"datas\":[{\"MPX\":\"\",\"MPY\":\"\"}],\"pages\":[{\"psize\": \"10\",\"tcount\": \"\",\"pno\":\"1\",\"tsize\": \"\"}]}";
        String b = AESUtil.encryptStr(json, "AAEDB13034626B2F66A70DE25A5B234C");

        HttpPost post = new HttpPost("http://lg.fjgat.gov.cn/ywxzservice/dbClient.do");
        String appid = "CPMSSI4V89IJ5RKF";
        post.setHeader("appid", appid);
        String secre = "43BA5EA7C31138F86011C8F90777BAFE";
        String currdate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String token = md5(appid + secre + currdate + json.replaceAll("\r\n", ""));
        post.setHeader("token", token);
        post.setHeader("tranId", "12312321312321321321312");
        post.setHeader("serviceId", "shhcj_sjfw_fjdzcx");
        post.setHeader("serviceValue", "附近地址");
        post.setHeader("versionCode", "");

        String result = httpPOST2(post, b);
        System.out.println(result);
    }

    public static List<String> getAddrs(String mapx, String mapy) {
        List<String> addrs = new ArrayList<String>();

        String json = "{\"datas\":[{\"MPX\":\"" + mapx + "\",\"MPY\":\"" + mapy
                + "\"}],\"pages\":[{\"psize\": \"10\",\"tcount\": \"\",\"pno\":\"1\",\"tsize\": \"\"}]}";
        String b = AESUtil.encryptStr(json, "AAEDB13034626B2F66A70DE25A5B234C");

        HttpPost post = new HttpPost("http://lg.fjgat.gov.cn/ywxzservice/dbClient.do");
        String appid = "CPMSSI4V89IJ5RKF";
        post.setHeader("appid", appid);
        String secre = "43BA5EA7C31138F86011C8F90777BAFE";
        String currdate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String token = md5(appid + secre + currdate + json.replaceAll("\r\n", ""));
        post.setHeader("token", token);
        post.setHeader("tranId", "12312321312321321321312");
        post.setHeader("serviceId", "shhcj_sjfw_fjdzcx");
        post.setHeader("serviceValue", "附近地址");
        post.setHeader("versionCode", "");

        String result = httpPOST2(post, b);
        JSONObject jsonResult = JSONObject.fromObject(result);
        JSONObject obj = jsonResult.getJSONObject("sta");
        String code = (String) obj.get("code");
        if ("0000".equals(code)) {
            JSONArray arys = jsonResult.getJSONArray("datas");
            for (int i = 0; i < arys.size(); i++) {
                obj = arys.getJSONObject(i);
                String addr = (String) obj.get("DZMC");
                if (StringUtils.isNotBlank(addr)) {
                    addrs.add(addr);
                }
            }

        }
        return addrs;
    }

    public static String encrypt(String content, String secureKey) {
        try {
            if (content == null || secureKey == null) {
                return null;
            }
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(secureKey.getBytes());
            kgen.init(128, secureRandom);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器 
            byte[] byteContent = content.getBytes();
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化 
            byte[] result = cipher.doFinal(byteContent);
            return encodeBASE64(result); // 加密 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encodeBASE64(byte[] content) throws Exception {
        if (content == null || content.length == 0)
            return null;
        try {
            return Base64.encode(content);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String httpPOST2(HttpPost post, String json) {
        HttpClient client = new DefaultHttpClient();
        String body = "";
        try {
            StringEntity s = new StringEntity(json, "UTF-8");
            s.setContentType("application/json");
            post.setEntity(s);
            HttpResponse res = client.execute(post);
            HttpEntity entity = res.getEntity();
            if (entity != null) {
                body = EntityUtils.toString(entity, "utf-8");
            }
            return URLDecoder.decode(body, "utf-8");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

}
