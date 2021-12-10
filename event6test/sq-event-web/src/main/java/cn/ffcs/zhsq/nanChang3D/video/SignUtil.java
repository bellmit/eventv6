package cn.ffcs.zhsq.nanChang3D.video;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

public class SignUtil
{
  public static String sign(String secret, String method, String path, Map<String, String> headers, Map<String, String> querys, Map<String, String> bodys, List<String> signHeaderPrefixList)
  {
    try
    {
      Mac hmacSha256 = Mac.getInstance("HmacSHA256");
      byte[] keyBytes = secret.getBytes("UTF-8");
      hmacSha256.init(new SecretKeySpec(keyBytes, 0, keyBytes.length, "HmacSHA256"));
      
      return new String(Base64.encodeBase64(hmacSha256
        .doFinal(buildStringToSign(method, path, headers, querys, bodys, signHeaderPrefixList)
        .getBytes("UTF-8"))), "UTF-8");
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }
  
  private static String buildStringToSign(String method, String path, Map<String, String> headers, Map<String, String> querys, Map<String, String> bodys, List<String> signHeaderPrefixList)
  {
    StringBuilder sb = new StringBuilder();
    
    sb.append(method.toUpperCase()).append("\n");
    if (null != headers)
    {
      if (null != headers.get("Accept"))
      {
        sb.append((String)headers.get("Accept"));
        sb.append("\n");
      }
      if (null != headers.get("Content-MD5"))
      {
        sb.append((String)headers.get("Content-MD5"));
        sb.append("\n");
      }
      if (null != headers.get("Content-Type"))
      {
        sb.append((String)headers.get("Content-Type"));
        sb.append("\n");
      }
      if (null != headers.get("Date"))
      {
        sb.append((String)headers.get("Date"));
        sb.append("\n");
      }
    }
    sb.append(buildHeaders(headers, signHeaderPrefixList));
    sb.append(buildResource(path, querys, bodys));
    return sb.toString();
  }
  
  private static String buildResource(String path, Map<String, String> querys, Map<String, String> bodys)
  {
    StringBuilder sb = new StringBuilder();
    if (!StringUtils.isBlank(path)) {
      sb.append(path);
    }
    Map<String, String> sortMap = new TreeMap();
    if (null != querys) {
      for (Map.Entry<String, String> query : querys.entrySet()) {
        if (!StringUtils.isBlank((CharSequence)query.getKey())) {
          sortMap.put(query.getKey(), query.getValue());
        }
      }
    }
    Map.Entry<String, String> body;
    if (null != bodys) {
      for (Iterator e = bodys.entrySet().iterator(); e.hasNext();)
      {
        body = (Map.Entry)e.next();
        if (!StringUtils.isBlank((CharSequence)body.getKey())) {
          sortMap.put(body.getKey(), body.getValue());
        }
      }
    }
    
    StringBuilder sbParam = new StringBuilder();
    for (Map.Entry<String, String> item : sortMap.entrySet()) {
      if (!StringUtils.isBlank((CharSequence)item.getKey()))
      {
        if (0 < sbParam.length()) {
          sbParam.append("&");
        }
        sbParam.append((String)item.getKey());
        if (!StringUtils.isBlank((CharSequence)item.getValue())) {
          sbParam.append("=").append((String)item.getValue());
        }
      }
    }
    if (0 < sbParam.length())
    {
      sb.append("?");
      sb.append(sbParam);
    }
    return sb.toString();
  }
  
  private static String buildHeaders(Map<String, String> headers, List<String> signHeaderPrefixList)
  {
    StringBuilder sb = new StringBuilder();
    if (null != signHeaderPrefixList)
    {
      signHeaderPrefixList.remove("x-ca-signature");
      signHeaderPrefixList.remove("Accept");
      signHeaderPrefixList.remove("Content-MD5");
      signHeaderPrefixList.remove("Content-Type");
      signHeaderPrefixList.remove("Date");
      Collections.sort(signHeaderPrefixList);
    }
    if (null != headers)
    {
      Map<String, String> sortMap = new TreeMap();
      sortMap.putAll(headers);
      StringBuilder signHeadersStringBuilder = new StringBuilder();
      for (Map.Entry<String, String> header : sortMap.entrySet()) {
        if (isHeaderToSign((String)header.getKey(), signHeaderPrefixList))
        {
          sb.append((String)header.getKey());
          sb.append(":");
          if (!StringUtils.isBlank((CharSequence)header.getValue())) {
            sb.append((String)header.getValue());
          }
          sb.append("\n");
          if (0 < signHeadersStringBuilder.length()) {
            signHeadersStringBuilder.append(",");
          }
          signHeadersStringBuilder.append((String)header.getKey());
        }
      }
      headers.put("x-ca-signature-headers", signHeadersStringBuilder.toString());
    }
    return sb.toString();
  }
  
  private static boolean isHeaderToSign(String headerName, List<String> signHeaderPrefixList)
  {
    if (StringUtils.isBlank(headerName)) {
      return false;
    }
    if (headerName.startsWith("x-ca-")) {
      return true;
    }
    if (null != signHeaderPrefixList) {
      for (String signHeaderPrefix : signHeaderPrefixList) {
        if (headerName.equalsIgnoreCase(signHeaderPrefix)) {
          return true;
        }
      }
    }
    return false;
  }
}
